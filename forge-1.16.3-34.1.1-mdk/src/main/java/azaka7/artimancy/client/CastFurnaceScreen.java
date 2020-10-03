package azaka7.artimancy.client;

import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import azaka7.artimancy.Artimancy;
import azaka7.artimancy.common.tileentity.CastFurnaceContainer;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class CastFurnaceScreen extends ContainerScreen<CastFurnaceContainer> implements IRecipeShownListener
{
	private static final ResourceLocation FURNACE_GUI_TEXTURES = new ResourceLocation(Artimancy.MODID+":textures/gui/container/casting_furnace_gui.png");
	private static final ResourceLocation RECIPE_BUTTON_TEXTURE = new ResourceLocation("textures/gui/recipe_button.png");
	/** The player inventory bound to this GUI. */
	//private final PlayerInventory playerInventory;
	// private final IInventory tileFurnace;
	private final CastFurnaceContainer furnaceContainer;
	public final CastFurnaceRecipeGui recipeGui;
	private boolean widthTooNarrowIn;

	public CastFurnaceScreen(CastFurnaceContainer container, CastFurnaceRecipeGui cfrgui, PlayerInventory playerInv, ITextComponent title)
	{
		super(container, playerInv, title);
		//this.playerInventory = playerInv;
		this.furnaceContainer = container;
		recipeGui = cfrgui;
		
	}
	
	@Override
	public void func_231160_c_() {
		super.func_231160_c_();
		this.widthTooNarrowIn = this.field_230708_k_ < 379;
		this.recipeGui.init(this.field_230708_k_, this.field_230709_l_, this.field_230706_i_, this.widthTooNarrowIn, this.container);
		this.guiLeft = this.recipeGui.updateScreenPosition(this.widthTooNarrowIn, this.field_230708_k_, this.xSize);
		this.func_230480_a_((Widget) new ImageButton(this.guiLeft + 20, this.field_230709_l_ / 2 - 49, 20, 18, 0, 0, 19, RECIPE_BUTTON_TEXTURE, (p_214087_1_) -> {
			this.recipeGui.renderUI(this.widthTooNarrowIn);
			this.recipeGui.toggleVisibility();
			this.guiLeft = this.recipeGui.updateScreenPosition(this.widthTooNarrowIn, this.field_230708_k_, this.xSize);
			((ImageButton)p_214087_1_).setPosition(this.guiLeft + 20, this.field_230709_l_ / 2 - 49);
		}));
		//Following line may not be needed
		//this.field_238742_p_ = (this.xSize - this.field_230712_o_.func_238414_a_(this.field_230704_d_)) / 2;
	}

	/**
	* Draws the screen and all the components in it.
	*/
	@Override
	public void func_230430_a_(MatrixStack matrix, int x, int y, float f) {
		this.func_230446_a_(matrix);
		if (this.recipeGui.isVisible() && this.widthTooNarrowIn) {
			this.func_230450_a_(matrix, f, x, y);
			this.recipeGui.func_230430_a_(matrix, x, y, f);
		} else {
			this.recipeGui.func_230430_a_(matrix, x, y, f);
			super.func_230430_a_(matrix, x, y, f);
			//this.recipeGui.func_230477_a_(matrix, this.guiLeft, this.guiTop, true, f); //used for ghost recipes. not doing that.
		}
		
		this.func_230459_a_(matrix, x, y);
		this.recipeGui.renderTooltip(matrix, this.guiLeft, this.guiTop, x, y);
	   }
	
	@Override
	public boolean func_231044_a_(double mouseX, double mouseY, int buttonID) {
		return recipeGui.func_231044_a_(mouseX, mouseY, buttonID) | super.func_231044_a_(mouseX, mouseY, buttonID);
	}

	/**
	* Draw the foreground layer for the GuiContainer (everything in front of the items)
	*
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String s = this.title.getFormattedText();
		this.font.drawString(s, this.xSize / 2 - this.font.getStringWidth(s) / 2, 6, 4210752);
		this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8, this.ySize - 96 + 2, 4210752);
	}*/

	/**
	* Draws the background layer of this container (behind the items).
	*/
	@SuppressWarnings("deprecation")
	@Override
	protected void func_230450_a_(MatrixStack matrix, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.field_230706_i_.getTextureManager().bindTexture(FURNACE_GUI_TEXTURES);
		int i = this.guiLeft;
		int j = this.guiTop;
		 this.func_238474_b_(matrix, i, j, 0, 0, this.xSize, this.ySize);
		
		if (furnaceContainer.isBurning()) 
		{
			int k = this.getBurnLeftScaled(13);
			this.func_238474_b_(matrix, i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
		}

		int scale = this.getCookProgressScaled(20);
		if(scale >= 19){
			this.func_238474_b_(matrix, i + 115, j + 59, 176, 18, 18, 4);
		} else {
			this.func_238474_b_(matrix, i + 115, j + 59, 176, 14, scale, 4);
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
	protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
		super.handleMouseClick(slotIn, slotId, mouseButton, type);
		this.recipeGui.slotClicked(slotIn);
	}

	@Override
	public boolean func_231046_a_(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
		return this.recipeGui.func_231046_a_(p_231046_1_, p_231046_2_, p_231046_3_) ? false : super.func_231046_a_(p_231046_1_, p_231046_2_, p_231046_3_);
	}

	@Override
	public boolean func_231042_a_(char p_231042_1_, int p_231042_2_) {
		return this.recipeGui.func_231042_a_(p_231042_1_, p_231042_2_) ? true : super.func_231042_a_(p_231042_1_, p_231042_2_);
	}
	
	@Override
	protected boolean hasClickedOutside(double x, double y, int guiLeft, int guiTop, int mouseButton) {
		boolean flag = x < (double)guiLeft || y < (double)guiTop || x >= (double)(guiLeft + this.xSize) || y >= (double)(guiTop + this.ySize);
		return this.recipeGui.clickedAway(x, y, this.guiLeft, this.guiTop, this.xSize, this.ySize, mouseButton) && flag;
	}
	

	@Override
	public void recipesUpdated() {
		recipeGui.recipesUpdated();
	}

	@Override	//This method is not meant to do anything here, so it voids out the method that is referenced through this method. It's only here for the interface that also provides recipesUpdated()
	public RecipeBookGui getRecipeGui() { 
		return new RecipeBookGui() { public void setupGhostRecipe(IRecipe<?> recipe, List<Slot> slots) {} }; 
	}

}