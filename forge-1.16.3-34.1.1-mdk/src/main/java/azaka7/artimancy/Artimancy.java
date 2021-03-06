package azaka7.artimancy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import azaka7.artimancy.client.ClientHandler;
import azaka7.artimancy.common.CommonHandler;
import azaka7.artimancy.common.item.SpellScrollItem;
import azaka7.artimancy.common.item.StaffItem;
import azaka7.artimancy.common.magic.AbstractSpell;
import azaka7.artimancy.common.magic.Spells;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.PaintingType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(Artimancy.MODID)
public class Artimancy
{
    public static final String MODID = "artimancy";
    public static final String NAME = "Artimancy";
    public static final String VERSION = "0.3.0a";

    private static final Logger LOGGER = LogManager.getLogger();
    
    private final CommonHandler common_proxy;
    private final ClientHandler client_proxy;
    private static Artimancy INSTANCE;
    
    public Artimancy() {
    	INSTANCE = this;
    	common_proxy = new CommonHandler(LOGGER);
    	
    	LOGGER.info("Instantiating Artimancy...");
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonInit);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class,common_proxy::registerItems);//this::registeryEvents);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class,common_proxy::registerBlocks);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(TileEntityType.class,common_proxy::registerContainers);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(ContainerType.class,common_proxy::registerContainerTypes);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(IRecipeSerializer.class,common_proxy::registerRecipeSerializers);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Enchantment.class,common_proxy::registerEnchants);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(EntityType.class,common_proxy::registerEntityTypes);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(PaintingType.class,common_proxy::registerPaintings);
        
        
        if(FMLEnvironment.dist == Dist.CLIENT) {
        	client_proxy = new ClientHandler();
        	FMLJavaModLoadingContext.get().getModEventBus().addListener(this::preInitClient);
        	FMLJavaModLoadingContext.get().getModEventBus().addListener(client_proxy::colorItemEvent);
        } else {
        	client_proxy = null;
        }

        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void pearlDamage(EnderTeleportEvent event) {
    	ItemStack held = event.getEntityLiving().getHeldItemMainhand();
    	if(held != null && !held.isEmpty() && held.getItem() instanceof StaffItem && Spells.getSpell(held) == Spells.TELEPORT) {
    		event.setAttackDamage(event.getAttackDamage()*0.1F);
    		held.damageItem(1, event.getEntityLiving(), (entity) -> {
                entity.sendBreakAnimation(event.getEntityLiving().getActiveHand());
			});
    		
    	}
    }
    
    @SubscribeEvent
    public void anvilCrafting(AnvilUpdateEvent event) {
    	if(event.getLeft() == null || event.getLeft().isEmpty() || event.getRight() == null || event.getRight().isEmpty()) {return;}
    	if(event.getLeft().getItem() instanceof StaffItem && event.getRight().getItem() instanceof SpellScrollItem) {
    		AbstractSpell spell = ((SpellScrollItem)event.getRight().getItem()).getSpell();
    		if(spell != null) {
    			ItemStack output = event.getLeft().copy();
    			Spells.applySpell(spell, output);
    			event.setOutput(output);
    			event.setCost(1);
    		}
    	}
    }
    
    /*@SuppressWarnings("unchecked")
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
    	} else if(event.getGenericType() == PaintingType.class) {
    		common_proxy.registerPaintings((Register<PaintingType>) event);
    	} else if(event.getGenericType() == Enchantment.class) {
    		common_proxy.registerEnchants((Register<Enchantment>) event);
    	} else if(event.getGenericType() == EntityType.class) {
    		common_proxy.registerEntityTypes((Register<EntityType<?>>) event);
    	}
    }*/
    
    public void commonInit(final FMLCommonSetupEvent event)
    {
    	LOGGER.debug("Artimancy Common Init");
    }

	@OnlyIn(Dist.CLIENT)
    public void preInitClient(final FMLClientSetupEvent event)
    {
    	LOGGER.debug("Artimancy Client Init");
    	client_proxy.registerClientUIs();
    	client_proxy.setRenderLayers();
    	client_proxy.registerEntityRenderers();
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
