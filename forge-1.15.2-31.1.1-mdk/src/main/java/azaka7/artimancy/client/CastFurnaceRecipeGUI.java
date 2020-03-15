package azaka7.artimancy.client;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;

import azaka7.artimancy.common.crafting.CastingRecipe;
import azaka7.artimancy.common.crafting.CastingRecipeBookPage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.client.gui.recipebook.IRecipeUpdateListener;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ClientRecipeBook;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipePlacer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.play.client.CRecipeInfoPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CastFurnaceRecipeGui extends AbstractGui implements IRenderable, IGuiEventListener, IRecipeUpdateListener, IRecipePlacer<Ingredient> {
	protected static final ResourceLocation RECIPE_BOOK = new ResourceLocation("textures/gui/recipe_book.png");
	private int xOffset;
	private int width;
	private int height;
	protected RecipeBookContainer<?> container;
	protected Minecraft mc;
	protected ClientRecipeBook recipeBook;
	protected final CastingRecipeBookPage recipeBookPage = new CastingRecipeBookPage();
	protected final RecipeItemHelper stackedContents = new RecipeItemHelper();
	private int timesInventoryChanged;
		
	public void init(int w, int h, Minecraft mc, boolean collapsed, RecipeBookContainer<?> container) {
		this.mc = mc;
		this.width = w;
		this.height = h;
		this.container = container;
		mc.player.openContainer = container;
		this.recipeBook = mc.player.getRecipeBook();
		this.timesInventoryChanged = mc.player.inventory.getTimesChanged();
		if (this.isVisible()) {
			this.renderUI(collapsed);
		}
	}

	public void renderUI(boolean collapsed) {
		this.xOffset = collapsed ? 0 : 86;
		int i = (this.width - 147) / 2 - this.xOffset;
		int j = (this.height - 166) / 2;
		this.stackedContents.clear();
		this.mc.player.inventory.func_201571_a(this.stackedContents);
		this.container.func_201771_a(this.stackedContents);
		this.recipeBookPage.init(this.mc, i, j);
		this.recipeBookPage.addListener(this);
		
		this.updateCollections(false);
	}

	public boolean changeFocus(boolean p_changeFocus_1_) {
		return false;
	}

	public void removed() {}

	public int updateScreenPosition(boolean collapsed, int x, int y) {
		int i;
		if (this.isVisible() && !collapsed) {
			i = 177 + (x - y - 200) / 2;
		} else {
			i = (x - y) / 2;
		}

		return i;
	}

	public void toggleVisibility() {
		this.setVisible(!this.isVisible());
	}

	public boolean isVisible() {
		return this.recipeBook.isGuiOpen();
	}

	protected void setVisible(boolean p_193006_1_) {
		this.recipeBook.setGuiOpen(p_193006_1_);
		if (!p_193006_1_) {
			this.recipeBookPage.setInvisible();
		}

		this.sendUpdateSettings();
	}

	public void slotClicked(@Nullable Slot slotIn) {
		if (slotIn != null && slotIn.slotNumber < this.container.getSize()) {
			if (this.isVisible()) {
				this.updateStackedContents();
			}
		}

	}

	private void updateCollections(boolean goToFirstPage) {
		List<RecipeList> list = this.recipeBook.getRecipes();
		list.forEach((group) -> {
	 	  if(group.getRecipes().get(0) instanceof CastingRecipe)
	 		  group.canCraft(this.stackedContents, this.container.getWidth(), this.container.getHeight(), this.recipeBook);
		});
		List<RecipeList> list1 = Lists.newArrayList(list);
		list1.removeIf((group) -> {
			return !group.isNotEmpty();
		});
		list1.removeIf((group) -> {
			return !group.containsValidRecipes();
		});
		list1.removeIf((group) -> {
			return !(group.getRecipes().get(0) instanceof CastingRecipe);
		});
		this.recipeBookPage.updateLists(list1, goToFirstPage);
	}

	public void tick() {
		if (this.isVisible()) {
			if (this.timesInventoryChanged != this.mc.player.inventory.getTimesChanged()) {
				this.updateStackedContents();
				this.timesInventoryChanged = this.mc.player.inventory.getTimesChanged();
			}

		}
	}

	private void updateStackedContents() {
		this.stackedContents.clear();
		this.mc.player.inventory.func_201571_a(this.stackedContents);
		this.container.func_201771_a(this.stackedContents);
		this.updateCollections(false);
	}

	public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
		if (this.isVisible()) {
			RenderSystem.pushMatrix();
			RenderSystem.translatef(0.0F, 0.0F, 100.0F);
			this.mc.getTextureManager().bindTexture(RECIPE_BOOK);
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			int i = (this.width - 147) / 2 - this.xOffset;
			int j = (this.height - 166) / 2;
			this.blit(i, j, 1, 1, 147, 166);
			
			this.recipeBookPage.render(i, j, p_render_1_, p_render_2_, p_render_3_);
			RenderSystem.popMatrix();
		}
	}

	public void renderTooltip(int p_191876_1_, int p_191876_2_, int p_191876_3_, int p_191876_4_) {
		if (this.isVisible()) {
			this.recipeBookPage.renderTooltip(p_191876_3_, p_191876_4_);
		}
	}

	protected String func_205703_f() {
		return I18n.format("gui.recipebook.toggleRecipes.all");
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int buttonID) {
		if (this.isVisible() && !this.mc.player.isSpectator()) {
			if (this.recipeBookPage.func_198955_a(mouseX, mouseY, buttonID, (this.width - 147) / 2 - this.xOffset, (this.height - 166) / 2, 147, 166)) {
				IRecipe<?> irecipe = this.recipeBookPage.getLastClickedRecipe();
				RecipeList recipelist = this.recipeBookPage.getLastClickedRecipeList();
				if (irecipe != null && recipelist != null) {
					this.mc.playerController.func_203413_a(this.mc.player.openContainer.windowId, irecipe, Screen.hasShiftDown());
					if (!this.isOffsetNextToMainGUI()) {
						this.setVisible(false);
					}
				}

				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	protected boolean toggleCraftableFilter() {
		boolean flag = !this.recipeBook.isFilteringCraftable();
		this.recipeBook.setFilteringCraftable(flag);
		return flag;
	}

	public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
		if (this.isVisible() && !this.mc.player.isSpectator()) {
			if (p_keyPressed_1_ == 256 && !this.isOffsetNextToMainGUI()) {
				this.setVisible(false);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean keyReleased(int p_223281_1_, int p_223281_2_, int p_223281_3_) {
		return IGuiEventListener.super.keyReleased(p_223281_1_, p_223281_2_, p_223281_3_);
	}

	public boolean charTyped(char p_charTyped_1_, int p_charTyped_2_) {
		if (this.isVisible() && !this.mc.player.isSpectator()) {
	 	  return IGuiEventListener.super.charTyped(p_charTyped_1_, p_charTyped_2_);
		} else {
			return false;
		}
	}

	public boolean isMouseOver(double p_isMouseOver_1_, double p_isMouseOver_3_) {
		return false;
	}

	private boolean isOffsetNextToMainGUI() {
		return this.xOffset == 86;
	}

	public void recipesUpdated() {
		if (this.isVisible()) {
			this.updateCollections(false);
		}

	}

	public void recipesShown(List<IRecipe<?>> recipes) {
		for(IRecipe<?> irecipe : recipes) {
			this.mc.player.removeRecipeHighlight(irecipe);
		}

	}
	
	//TODO
	public void setSlotContents(Iterator<Ingredient> ingredients, int slotIn, int maxAmount, int y, int x) {}

	protected void sendUpdateSettings() {
		if (this.mc.getConnection() != null) {
			this.mc.getConnection().sendPacket(new CRecipeInfoPacket(this.recipeBook.isGuiOpen(), this.recipeBook.isFilteringCraftable(), this.recipeBook.isFurnaceGuiOpen(), this.recipeBook.isFurnaceFilteringCraftable(), this.recipeBook.func_216758_e(), this.recipeBook.func_216761_f()));
		}

	}
}