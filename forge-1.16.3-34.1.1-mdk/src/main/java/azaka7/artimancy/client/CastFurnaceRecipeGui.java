package azaka7.artimancy.client;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import azaka7.artimancy.common.crafting.CastingRecipe;
import azaka7.artimancy.common.crafting.CastingRecipeBookPage;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.client.gui.recipebook.IRecipeUpdateListener;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.ClientRecipeBook;
import net.minecraft.client.util.SearchTreeManager;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipePlacer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeBookCategory;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.play.client.CUpdateRecipeBookStatusPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CastFurnaceRecipeGui extends AbstractGui implements IRenderable, IGuiEventListener, IRecipeUpdateListener, IRecipePlacer<Ingredient> {
	protected static final ResourceLocation RECIPE_BOOK = new ResourceLocation("textures/gui/recipe_book.png");
	private static final ITextComponent SEARCH_TEXT = (new TranslationTextComponent("gui.recipebook.search_hint")).func_240699_a_(TextFormatting.ITALIC).func_240699_a_(TextFormatting.GRAY);
	
	private int xOffset;
	private int width;
	private int height;
	protected RecipeBookContainer<?> container;
	protected Minecraft mc;
	protected ClientRecipeBook recipeBook;
	private TextFieldWidget searchBar;
	protected final CastingRecipeBookPage recipeBookPage = new CastingRecipeBookPage();
	protected final RecipeItemHelper stackedContents = new RecipeItemHelper();
	private int timesInventoryChanged;
	private boolean searching;
	private String lastSearch;
		
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
		
		mc.keyboardListener.enableRepeatEvents(true);
	}

	public void renderUI(boolean collapsed) {
		this.xOffset = collapsed ? 0 : 86;
		int i = (this.width - 147) / 2 - this.xOffset;
		int j = (this.height - 166) / 2;
		this.stackedContents.clear();
		this.mc.player.inventory.accountStacks(this.stackedContents);
		this.container.fillStackedContents(this.stackedContents);
		this.recipeBookPage.init(this.mc, i, j);
		this.recipeBookPage.addListener(this);
		
		String s = this.searchBar != null ? this.searchBar.getText() : "";
	    this.searchBar = new TextFieldWidget(this.mc.fontRenderer, i + 25, j + 14, 80, 9 + 5, new TranslationTextComponent("itemGroup.search"));
	    this.searchBar.setMaxStringLength(50);
	    this.searchBar.setEnableBackgroundDrawing(false);
	    this.searchBar.setVisible(true);
	    this.searchBar.setTextColor(16777215);
	    this.searchBar.setText(s);
		
		this.updateCollections(false);
	}

	public boolean changeFocus(boolean p_changeFocus_1_) {
		return false;
	}

	public void removed() {
		this.searchBar = null;
		this.mc.keyboardListener.enableRepeatEvents(false);
	}

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
		return this.recipeBook.func_242142_a(RecipeBookCategory.FURNACE); //TODO Maybe add a new recipe book category
	}

	protected void setVisible(boolean flag) {
		this.recipeBook.func_242143_a(RecipeBookCategory.FURNACE, flag);
		if (!flag) {
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
		
		String s = this.searchBar.getText();
		if (!s.isEmpty()) {
			ObjectSet<RecipeList> objectset = new ObjectLinkedOpenHashSet<>(this.mc.getSearchTree(SearchTreeManager.RECIPES).search(s.toLowerCase(Locale.ROOT)));
			list1.removeIf((p_193947_1_) -> {
				return !objectset.contains(p_193947_1_);
			});
		}
		
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
		this.mc.player.inventory.accountStacks(this.stackedContents);
		this.container.fillStackedContents(this.stackedContents);
		this.updateCollections(false);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void func_230430_a_(MatrixStack matrix, int x, int y, float f) {
		if (this.isVisible()) {
	         RenderSystem.pushMatrix();
	         RenderSystem.translatef(0.0F, 0.0F, 100.0F);
	         this.mc.getTextureManager().bindTexture(RECIPE_BOOK);
	         RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
	         int i = (this.width - 147) / 2 - this.xOffset;
	         int j = (this.height - 166) / 2;
	         this.func_238474_b_(matrix, i, j, 1, 1, 147, 166);
	         if (!this.searchBar.func_230999_j_() && this.searchBar.getText().isEmpty()) {
	            func_238475_b_(matrix, this.mc.fontRenderer, SEARCH_TEXT, i + 25, j + 14, -1);
	         } else {
	            this.searchBar.func_230430_a_(matrix, x, y, f);
	         }
	         this.recipeBookPage.render(matrix, i, j, x, y, f);
	         RenderSystem.popMatrix();
	      }
	}

	public void renderTooltip(MatrixStack matrix, int p_191876_1_, int p_191876_2_, int p_191876_3_, int p_191876_4_) {
		if (this.isVisible()) {
			this.recipeBookPage.renderTooltip(matrix, p_191876_3_, p_191876_4_);
		}
	}

	//For use in toggle craftable recipes functionality
	/*protected String func_205703_f() {
		return I18n.format(this.toggleRecipesBtn.isStateTriggered() ? "gui.recipebook.toggleRecipes.craftable" : "gui.recipebook.toggleRecipes.all");
	}*/
	
	@Override
	public boolean func_231044_a_(double mouseX, double mouseY, int buttonID) {
		if (this.isVisible() && !this.mc.player.isSpectator()) {
			if (this.recipeBookPage.mouseClick(mouseX, mouseY, buttonID /*, (this.width - 147) / 2 - this.xOffset, (this.height - 166) / 2, 147, 166*/)) {
				IRecipe<?> irecipe = this.recipeBookPage.getLastClickedRecipe();
				RecipeList recipelist = this.recipeBookPage.getLastClickedRecipeList();
				if (irecipe != null && recipelist != null) {
					this.mc.playerController.sendPlaceRecipePacket(this.mc.player.openContainer.windowId, irecipe, Screen.func_231173_s_());
					if (!this.isOffsetNextToMainGUI()) {
						this.setVisible(false);
					}
				}

				return true;
			} else if (this.searchBar.func_231044_a_(mouseX, mouseY, buttonID)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	protected boolean toggleCraftableFilter() {
		RecipeBookCategory recipebookcategory = this.container.func_241850_m();
	      boolean flag = !this.recipeBook.func_242145_b(recipebookcategory);
	      this.recipeBook.func_242146_b(recipebookcategory, flag);
	      return flag;
	}

	@Override
	public boolean func_231046_a_(int key, int scanCode, int modifiers) {
		this.searching = false;
		if (this.isVisible() && !this.mc.player.isSpectator()) {
			if (key == 256 && !this.isOffsetNextToMainGUI()) {
				this.setVisible(false);
				return true;
			} else if (this.searchBar.func_231046_a_(key, scanCode, modifiers)) {
				this.updateSearch();
				return true;
			} else if (this.searchBar.func_230999_j_() && this.searchBar.getVisible() && key != 256) {
				return true;
	        } else if (this.mc.gameSettings.keyBindChat.matchesKey(key, scanCode) && !this.searchBar.func_230999_j_()) {
	        	this.searching = true;
	        	this.searchBar.setFocused2(true);
	        	return true;
	        } else {
	        	return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean keyReleased(int p_223281_1_, int p_223281_2_, int p_223281_3_) {
		this.searching = false;
		return IGuiEventListener.super.keyReleased(p_223281_1_, p_223281_2_, p_223281_3_);
	}
	
	public boolean clickedAway(double mouseX, double mouseY, int left, int top, int width, int height, int button) {
		if (!this.isVisible()) {
	         return true;
	      } else {
	         boolean flag = mouseX < (double)left || mouseY < (double)top || mouseX >= (double)(left + width) || mouseY >= (double)(top + height);
	         boolean flag1 = (double)(left - 147) < mouseX && mouseX < (double)left && (double)top < mouseY && mouseY < (double)(top + height);
	         return flag && !flag1;
	      }
	   }

	@Override
	public boolean func_231042_a_(char character, int modifiers) {
		if (this.searching) {
			return false;
		} else if (this.isVisible() && !this.mc.player.isSpectator()) {
			if (this.searchBar.func_231042_a_(character, modifiers)) {
				this.updateSearch();
				return true;
			} else {
				return IGuiEventListener.super.func_231042_a_(character, modifiers);
	        }
		} else {
			return false;
		}
	}
	
	private void updateSearch() {
		String s = this.searchBar.getText().toLowerCase(Locale.ROOT);
		if (!s.equals(this.lastSearch)) {
			this.updateCollections(false);
			this.lastSearch = s;
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
	
	//TODO fill from recipe
	public void setSlotContents(Iterator<Ingredient> ingredients, int slotIn, int maxAmount, int y, int x) {}

	protected void sendUpdateSettings() {
		if (this.mc.getConnection() != null) {
	         RecipeBookCategory recipebookcategory = this.container.func_241850_m();
	         boolean flag = this.recipeBook.func_242139_a().func_242151_a(recipebookcategory);
	         boolean flag1 = this.recipeBook.func_242139_a().func_242158_b(recipebookcategory);
	         this.mc.getConnection().sendPacket(new CUpdateRecipeBookStatusPacket(recipebookcategory, flag, flag1));
	      }

	}
}