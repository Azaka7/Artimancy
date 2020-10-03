package azaka7.artimancy.client;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import azaka7.artimancy.Artimancy;
import azaka7.artimancy.common.tileentity.ArtimancyTableContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ArtimancyTableScreen extends ContainerScreen<ArtimancyTableContainer>
{
	private static final ResourceLocation FURNACE_GUI_TEXTURES = new ResourceLocation(Artimancy.MODID+":textures/gui/container/artimancy_table_gui.png");
	
	/** The player inventory bound to this GUI. */
	//private final PlayerInventory playerInventory;

	public ArtimancyTableScreen(ArtimancyTableContainer container, PlayerInventory playerInv, ITextComponent title)
	{
		super(container, playerInv, title);
		//this.playerInventory = playerInv;
	}

	/**
	* Draws the screen and all the components in it.
	*/
	@Override
	public void func_230430_a_(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		this.func_230446_a_(p_230430_1_);
	    super.func_230430_a_(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
	    this.func_230459_a_(p_230430_1_, p_230430_2_, p_230430_3_);
	}

	/**
	* Draw the foreground layer for the GuiContainer (everything in front of the items)
	*/
	/*@Override
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
	protected void func_230450_a_(MatrixStack matrix, float f, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.field_230706_i_.getTextureManager().bindTexture(FURNACE_GUI_TEXTURES);
		int i = (this.field_230708_k_ - this.xSize) / 2;
	    int j = (this.field_230709_l_ - this.ySize) / 2;
	    this.func_238474_b_(matrix, i, j, 0, 0, this.xSize, this.ySize);
	}

}