package azaka7.artimancy.client;

import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import azaka7.artimancy.Artimancy;
import azaka7.artimancy.common.tileentity.CastFurnaceContainer;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class CastFurnaceScreen extends ContainerScreen<CastFurnaceContainer> implements IRecipeShownListener
{
	private static final ResourceLocation FURNACE_GUI_TEXTURES = new ResourceLocation(Artimancy.MODID+":textures/gui/container/casting_furnace_gui.png");
	private static final ResourceLocation RECIPE_BUTTON_TEXTURE = new ResourceLocation("textures/gui/recipe_button.png");
	/** The player inventory bound to this GUI. */
	private final PlayerInventory playerInventory;
	// private final IInventory tileFurnace;
	private final CastFurnaceContainer furnaceContainer;
	public final CastFurnaceRecipeGui recipeBookGui;
	private boolean closeRecipeBook;

	public CastFurnaceScreen(CastFurnaceContainer container, CastFurnaceRecipeGui cfrgui, PlayerInventory playerInv, ITextComponent title)
	{
		super(container, playerInv, title);
		this.playerInventory = playerInv;
		this.furnaceContainer = container;
		recipeBookGui = cfrgui;
		
	}
	
	@Override
	public void init() {
		super.init();
		this.closeRecipeBook = this.width < 379;
		this.recipeBookGui.init(this.width, this.height, this.minecraft, this.closeRecipeBook, this.container);
		this.guiLeft = this.recipeBookGui.updateScreenPosition(this.closeRecipeBook, this.width, this.xSize);
		this.addButton((new ImageButton(this.guiLeft + 5, this.height / 2 - 49, 20, 18, 0, 0, 19, RECIPE_BUTTON_TEXTURE, (p_214087_1_) -> {
				this.recipeBookGui.renderUI(this.closeRecipeBook);
				this.recipeBookGui.toggleVisibility();
				this.guiLeft = this.recipeBookGui.updateScreenPosition(this.closeRecipeBook, this.width, this.xSize);
				((ImageButton)p_214087_1_).setPosition(this.guiLeft + 5, this.height / 2 - 49);
			})));
	}

	/**
	* Draws the screen and all the components in it.
	*/
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground();
		if (this.recipeBookGui.isVisible() && this.closeRecipeBook) {
			this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
			this.recipeBookGui.render(mouseX, mouseY, partialTicks);
		} else {
			this.recipeBookGui.render(mouseX, mouseY, partialTicks);
			super.render(mouseX, mouseY, partialTicks);
		}

		this.renderHoveredToolTip(mouseX, mouseY);
		this.recipeBookGui.renderTooltip(this.guiLeft, this.guiTop, mouseX, mouseY);
	}

	/**
	* Draw the foreground layer for the GuiContainer (everything in front of the items)
	*/
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String s = this.title.getFormattedText();
		this.font.drawString(s, this.xSize / 2 - this.font.getStringWidth(s) / 2, 6, 4210752);
		this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8, this.ySize - 96 + 2, 4210752);
	}

	/**
	* Draws the background layer of this container (behind the items).
	*/
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(FURNACE_GUI_TEXTURES);
		int i = this.guiLeft;
		int j = this.guiTop;
		this.blit(i, j, 0, 0, this.xSize, this.ySize);
		
		if (furnaceContainer.isBurning()) 
		{
				int k = this.getBurnLeftScaled(13);
				this.blit(i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
		}

		int scale = this.getCookProgressScaled(20);
		if(scale >= 19){
				this.blit(i + 115, j + 59, 176, 18, 18, 4);
		} else {
				this.blit(i + 115, j + 59, 176, 14, scale, 4);
		}
	}

	private int getCookProgressScaled(int pixels)
	{
		int i = furnaceContainer.getCookTime();
		int j = furnaceContainer.getItemCookTime();
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}

	private int getBurnLeftScaled(int pixels)
	{
		int i = furnaceContainer.getItemBurnTime();

		if (i == 0)
		{
				i = 200;
		}

		return furnaceContainer.getBurnTime() * pixels / i;
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		boolean ret = super.mouseClicked(mouseX, mouseY, button);
		return recipeBookGui.mouseClicked(mouseX, mouseY, button) || ret;
	}

	@Override
	public void recipesUpdated() {
		recipeBookGui.recipesUpdated();
	}

	@Override	//This method is not meant to do anything here, so it voids out the method that is referenced through this method. It's only here for the interface that also provides recipesUpdated()
	public RecipeBookGui func_194310_f() { return new RecipeBookGui() { public void setupGhostRecipe(IRecipe<?> recipe, List<Slot> slots) {} }; }
}