package azaka7.artimancy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import azaka7.artimancy.client.ClientHandler;
import azaka7.artimancy.common.CommonHandler;
import azaka7.artimancy.common.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Artimancy.MODID)
public class Artimancy
{
    public static final String MODID = "artimancy";
    public static final String NAME = "Artimancy";
    public static final String VERSION = "0.1.2a";

    private static final Logger LOGGER = LogManager.getLogger();
    
    private final CommonHandler common_proxy;
    private final ClientHandler client_proxy;
    private static Artimancy INSTANCE;
    
    public Artimancy() {
    	INSTANCE = this;
    	common_proxy = new CommonHandler(LOGGER);
    	client_proxy = new ClientHandler();
    	
    	LOGGER.info("Instantiating Artimancy...");
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonInit);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::preInitClient);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registeryEvents);

        MinecraftForge.EVENT_BUS.register(this);
    	
    }
    
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        LOGGER.info("Artimancy is unregistering it's own data pack so that Forge will reload it and override every other datapack.");
    	MinecraftServer server = event.getServer();
        server.getCommandManager().handleCommand(server.getCommandSource().withPermissionLevel(2), "datapack disable \"mod:artimancy\"");
    }
    
    @SuppressWarnings("unchecked")
	public final void registeryEvents(RegistryEvent.Register<?> event){
    	
    	if(event.getGenericType() == Item.class) {
    		common_proxy.registerItems((Register<Item>) event);
    	} else if(event.getGenericType() == Block.class) {
    		common_proxy.registerBlocks((Register<Block>) event);
    	} else if(event.getGenericType() == TileEntityType.class) {
    		common_proxy.registerContainers((Register<TileEntityType<?>>) event);
    	}else if(event.getGenericType() == ContainerType.class) {
    		common_proxy.registerContainerTypes((Register<ContainerType<?>>) event);
    	} else if(event.getGenericType() == IRecipeSerializer.class) {
    		common_proxy.registerRecipeSerializers((Register<IRecipeSerializer<?>>) event);
    	}
    }
    
    public void commonInit(final FMLCommonSetupEvent event)
    {
    	LOGGER.debug("Artimancy Common Init");
		
    }

	@OnlyIn(Dist.CLIENT)
    public void preInitClient(final FMLClientSetupEvent event)
    {
    	LOGGER.debug("Artimancy Client Init");
    	client_proxy.registerClientUIs();
		RenderTypeLookup.setRenderLayer(ModBlocks.instance().white_mushroom, RenderType.func_228643_e_());
    	
    }

	public CommonHandler commonProxy() {
		return INSTANCE.common_proxy;
	}
	
	public ClientHandler clientProxy() {
		return INSTANCE.client_proxy;
	}

	public static final Artimancy instance() {
		return INSTANCE;
	}

	public TileEntityType<?> getCastFurnaceType() {
		return common_proxy.getCastFurnaceType();
	}

	public Logger getLogger() {
		return LOGGER;
	}
}
