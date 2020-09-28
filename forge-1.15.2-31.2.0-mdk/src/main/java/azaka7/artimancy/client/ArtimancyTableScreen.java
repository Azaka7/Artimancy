package azaka7.artimancy.client;


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
	private final PlayerInventory playerInventory;

	public ArtimancyTableScreen(ArtimancyTableContainer container, PlayerInventory playerInv, ITextComponent title)
	{
		super(container, playerInv, title);
		this.playerInventory = playerInv;
	}
	
	@Override
	public void init() {
		super.init();
	}

	/**
	* Draws the screen and all the components in it.
	*/
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
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
	}

}