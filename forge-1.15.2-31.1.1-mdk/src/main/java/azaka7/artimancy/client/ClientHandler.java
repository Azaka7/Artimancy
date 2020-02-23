package azaka7.artimancy.client;

import azaka7.artimancy.Artimancy;
import azaka7.artimancy.common.tileentity.CastFurnaceContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.ScreenManager.IScreenFactory;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ClientHandler{
	
	public ClientHandler(){
		
	}
	
	public final void registerClientUIs() {
		ScreenManager.registerFactory(Artimancy.instance().commonProxy().getCastFurnaceContainerType(), new IScreenFactory<CastFurnaceContainer, CastFurnaceScreen>() {
			
			@Override
			public CastFurnaceScreen create(CastFurnaceContainer container, PlayerInventory inventory, ITextComponent title) {
				if(container == null || inventory == null) { return null; }
				return new CastFurnaceScreen(container, inventory, title);
			}
			
		});
	}
	
	protected ModelResourceLocation asModResource(String in){
		return new ModelResourceLocation(Artimancy.MODID+":"+in);
	}
	
}
