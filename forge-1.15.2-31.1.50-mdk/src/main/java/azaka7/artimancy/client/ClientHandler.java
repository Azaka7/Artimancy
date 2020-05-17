package azaka7.artimancy.client;

import azaka7.artimancy.Artimancy;
import azaka7.artimancy.client.entity.projectile.MagicMissileRenderer;
import azaka7.artimancy.common.ModBlocks;
import azaka7.artimancy.common.ModItems;
import azaka7.artimancy.common.entity.ModEntityTypes;
import azaka7.artimancy.common.entity.projectile.MagicMissileEntity;
import azaka7.artimancy.common.magic.AbstractSpell;
import azaka7.artimancy.common.magic.Spells;
import azaka7.artimancy.common.tileentity.CastFurnaceContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.ScreenManager.IScreenFactory;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientHandler{
	
	public ClientHandler(){}
	
	@OnlyIn(Dist.CLIENT)
	public void colorItemEvent(ColorHandlerEvent.Item event) {
		event.getItemColors().register((itemstack, tintIndex) -> {
			AbstractSpell spell = Spells.getSpell(itemstack);
			return spell == null ? 0xFFFFFF : spell.getColor();
				}, ModItems.instance().staffs.toArray(new IItemProvider[ModItems.instance().staffs.size()]));
	}
	
	public final void registerClientUIs() {
		ScreenManager.registerFactory(Artimancy.instance().commonProxy().getCastFurnaceContainerType(), new IScreenFactory<CastFurnaceContainer, CastFurnaceScreen>() {
			
			@Override
			public CastFurnaceScreen create(CastFurnaceContainer container, PlayerInventory inventory, ITextComponent title) {
				if(container == null || inventory == null) { return null; }
				return new CastFurnaceScreen(container, new CastFurnaceRecipeGui(), inventory, title);
			}
			
		});
	}
	
	protected ModelResourceLocation asModResource(String in){
		return new ModelResourceLocation(Artimancy.MODID+":"+in);
	}

	public void setRenderLayers() {
		RenderTypeLookup.setRenderLayer(ModBlocks.instance().white_mushroom, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.instance().lumin_block, RenderType.getTranslucent());
	}

	public void registerEntityRenderers() {
		EntityRendererManager erm = Minecraft.getInstance().getRenderManager();
		ArrowRenderer<MagicMissileEntity> renderer = new MagicMissileRenderer(erm);
		erm.register(ModEntityTypes.MAGIC_MISSILE, renderer);
		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.MAGIC_MISSILE, MagicMissileRenderer::new);
	}
	
}
