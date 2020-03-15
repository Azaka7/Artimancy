package azaka7.artimancy.common.crafting;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import azaka7.artimancy.client.CastFurnaceRecipeGui;
import azaka7.artimancy.client.CastRecipeWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.recipebook.IRecipeUpdateListener;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.client.gui.recipebook.RecipeOverlayGui;
import net.minecraft.client.gui.widget.ToggleWidget;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeBook;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CastingRecipeBookPage{
	protected static final ResourceLocation RECIPE_BOOK = new ResourceLocation("textures/gui/recipe_book.png");
	private final List<CastRecipeWidget> buttons = Lists.newArrayListWithCapacity(20);
	private CastRecipeWidget hoveredButton;
	private final RecipeOverlayGui overlay = new RecipeOverlayGui();
	private Minecraft minecraft;
	private final List<IRecipeUpdateListener> listeners = Lists.newArrayList();
	private List<RecipeList> recipeLists;
	private ToggleWidget forwardButton;
	private ToggleWidget backButton;
	private int totalPages;
	private int currentPage;
	private RecipeBook recipeBook;
	private IRecipe<?> lastClickedRecipe;
	private RecipeList lastClickedRecipeList;

	public CastingRecipeBookPage() {
		for(int i = 0; i < 20; ++i) {
			this.buttons.add(new CastRecipeWidget());
		}

	}

	public void init(Minecraft minecraft, int posX, int posY) {
		this.minecraft = minecraft;
		this.recipeBook = minecraft.player.getRecipeBook();

		for(int i = 0; i < this.buttons.size(); ++i) {
			this.buttons.get(i).setPosition(posX + 11 + 25 * (i % 5), posY + 31 + 25 * (i / 5));
		}

		this.forwardButton = new ToggleWidget(posX + 93, posY + 137, 12, 17, false);
		this.forwardButton.initTextureValues(1, 208, 13, 18, RECIPE_BOOK);
		this.backButton = new ToggleWidget(posX + 38, posY + 137, 12, 17, true);
		this.backButton.initTextureValues(1, 208, 13, 18, RECIPE_BOOK);
	}

	public void addListener(CastFurnaceRecipeGui p_193732_1_) {
		this.listeners.remove(p_193732_1_);
		this.listeners.add(p_193732_1_);
	}

	public void updateLists(List<RecipeList> list, boolean goToFront) {
		this.recipeLists = list;
		this.totalPages = (int)Math.ceil((double)list.size() / 20.0D);
		if (this.totalPages <= this.currentPage || goToFront) {
			this.currentPage = 0;
		}

		this.updateButtonsForPage();
	}

	private void updateButtonsForPage() {
		int i = 20 * this.currentPage;
		for(int j = 0; j < this.buttons.size(); ++j) {
			CastRecipeWidget recipewidget = this.buttons.get(j);
			if (i + j < this.recipeLists.size()) {
				RecipeList recipelist = this.recipeLists.get(i + j);
				recipewidget.updateRecipeGroup(recipelist, this);
				recipewidget.visible = true;
			} else {
				recipewidget.visible = false;
			}
		}

		this.updateArrowButtons();
	}

	private void updateArrowButtons() {
		this.forwardButton.visible = this.totalPages > 1 && this.currentPage < this.totalPages - 1;
		this.backButton.visible = this.totalPages > 1 && this.currentPage > 0;
	}

	public void render(int x, int y, int mouseX, int mouseY, float partialTicks) {
		if (this.totalPages > 1) {
			String s = this.currentPage + 1 + "/" + this.totalPages;
			int i = this.minecraft.fontRenderer.getStringWidth(s);
			this.minecraft.fontRenderer.drawString(s, (float)(x - i / 2 + 73), (float)(y + 141), -1);
		}

		this.hoveredButton = null;

		for(CastRecipeWidget recipewidget : this.buttons) {
			recipewidget.render(mouseX, mouseY, partialTicks);
			if (recipewidget.visible && recipewidget.isHovered()) {
				this.hoveredButton = recipewidget;
			}
		}

		this.backButton.render(mouseX, mouseY, partialTicks);
		this.forwardButton.render(mouseX, mouseY, partialTicks);
		this.overlay.render(mouseX, mouseY, partialTicks);
	}

	public void renderTooltip(int p_193721_1_, int p_193721_2_) {
		if (this.minecraft.currentScreen != null && this.hoveredButton != null && !this.overlay.isVisible()) {
			this.minecraft.currentScreen.renderTooltip(this.hoveredButton.getToolTipText(this.minecraft.currentScreen), p_193721_1_, p_193721_2_);
		}

	}

	@Nullable
	public IRecipe<?> getLastClickedRecipe() {
		return this.lastClickedRecipe;
	}

	@Nullable
	public RecipeList getLastClickedRecipeList() {
		return this.lastClickedRecipeList;
	}

	public void setInvisible() {
		this.overlay.setVisible(false);
	}

	public boolean func_198955_a(double mouseX, double mouseY, int button, int p_198955_6_, int p_198955_7_, int p_198955_8_, int p_198955_9_) {
		this.lastClickedRecipe = null;
		this.lastClickedRecipeList = null;
		if (this.overlay.isVisible()) {
			
			if (this.overlay.mouseClicked(mouseX, mouseY, button)) {
				this.lastClickedRecipe = this.overlay.getLastRecipeClicked();
				this.lastClickedRecipeList = this.overlay.getRecipeList();
			} else {
				this.overlay.setVisible(false);
			}

			return true;
		} else if (this.forwardButton.mouseClicked(mouseX, mouseY, button)) {
			++this.currentPage;
			this.updateButtonsForPage();
			return true;
		} else if (this.backButton.mouseClicked(mouseX, mouseY, button)) {
			--this.currentPage;
			this.updateButtonsForPage();
			return true;
		} else {
			for(CastRecipeWidget recipewidget : this.buttons) {
				if (recipewidget.mouseClicked(mouseX, mouseY, button)) {
					return true;
				}
			}

			return false;
		}
	}

	public void recipesShown(List<IRecipe<?>> p_194195_1_) {
		for(IRecipeUpdateListener irecipeupdatelistener : this.listeners) {
			irecipeupdatelistener.recipesShown(p_194195_1_);
		}

	}

	public Minecraft func_203411_d() {
		return this.minecraft;
	}

	public RecipeBook func_203412_e() {
		return this.recipeBook;
	}
}