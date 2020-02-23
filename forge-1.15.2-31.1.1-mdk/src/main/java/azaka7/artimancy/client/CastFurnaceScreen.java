package azaka7.artimancy.client;

import net.minecraft.client.gui.recipebook.AbstractRecipeBookGui;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import com.mojang.blaze3d.systems.RenderSystem;

import azaka7.artimancy.Artimancy;
import azaka7.artimancy.common.tileentity.CastFurnaceContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class CastFurnaceScreen extends ContainerScreen<CastFurnaceContainer> implements IRecipeShownListener
{
    private static final ResourceLocation FURNACE_GUI_TEXTURES = new ResourceLocation(Artimancy.MODID+":textures/gui/container/casting_furnace_gui.png");
    /** The player inventory bound to this GUI. */
    private final PlayerInventory playerInventory;
   // private final IInventory tileFurnace;
    private final CastFurnaceContainer furnaceContainer;
    public final AbstractRecipeBookGui recipeBook;

    public CastFurnaceScreen(CastFurnaceContainer container, PlayerInventory playerInv, ITextComponent title)
    {
        super(container, playerInv, title);
        this.playerInventory = playerInv;
        this.furnaceContainer = container;
        recipeBook = new CastFurnaceRecipeGUI();
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
    	this.renderBackground();
        //if (this.recipeBook.isVisible() && this.field_214090_m) {
        //   this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        //   this.recipeBook.render(mouseX, mouseY, partialTicks);
        //} else {
        //   this.recipeBook.render(mouseX, mouseY, partialTicks);
           super.render(mouseX, mouseY, partialTicks);
        //   this.recipeBook.renderGhostRecipe(this.guiLeft, this.guiTop, true, partialTicks);
        //}

        this.renderHoveredToolTip(mouseX, mouseY);
        //this.recipeBook.renderTooltip(this.guiLeft, this.guiTop, mouseX, mouseY);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
    	String s = this.title.getFormattedText();
        this.font.drawString(s, this.xSize / 2 - this.font.getStringWidth(s) / 2, 6, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getUnformattedComponentText(), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(FURNACE_GUI_TEXTURES);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
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
	public void recipesUpdated() {
		recipeBook.recipesUpdated();
	}

	@Override
	public RecipeBookGui func_194310_f() {
		return recipeBook;
	}
}