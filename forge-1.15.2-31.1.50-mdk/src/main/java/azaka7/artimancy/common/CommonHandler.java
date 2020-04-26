package azaka7.artimancy.common;

import java.lang.reflect.Field;
import java.util.function.Supplier;

import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;

import azaka7.artimancy.Artimancy;
import azaka7.artimancy.common.block.ICustomItemBlock;
import azaka7.artimancy.common.crafting.CastingRecipeSerializer;
import azaka7.artimancy.common.tileentity.CastFurnaceContainer;
import azaka7.artimancy.common.tileentity.CastFurnaceTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.event.world.BlockEvent;

public class CommonHandler {
	
	private final Logger LOGGER;
	
	private static final ToggleShapedSerializer TOGGLE_SHAPED_SERIALIZER = new ToggleShapedSerializer();
	
	private TileEntityType<CastFurnaceTileEntity> castFurnaceTEType;
    public final TileEntityType<CastFurnaceTileEntity> getCastFurnaceType() {return castFurnaceTEType;}
    private ContainerType<CastFurnaceContainer> castFurnaceContainerType;
    public final ContainerType<CastFurnaceContainer> getCastFurnaceContainerType() {return castFurnaceContainerType;}
    //TODO Control this with config
	private static boolean removeVanillaRecipes = true;
	
	public CommonHandler(Logger logger){
		LOGGER = logger;
		castFurnaceContainerType = new ContainerType<CastFurnaceContainer>(CastFurnaceContainer::new) {
			 public CastFurnaceContainer create(int windowId, PlayerInventory playerInv, net.minecraft.network.PacketBuffer extraData) {
				 return CastFurnaceContainer.createNew(windowId, playerInv, extraData);
			 }
		};
		castFurnaceContainerType.setRegistryName("castfurnace");
		castFurnaceTEType = TileEntityType.Builder.create(CastFurnaceTileEntity::new, ModBlocks.instance().cast_furnace).build(null);
		castFurnaceTEType.setRegistryName("castfurnace");
		
	}
	
	public final void registerBlocks(RegistryEvent.Register<Block> event){
		LOGGER.debug("Registering Mod Blocks...");
		for(Block block : ModBlocks.instance().getModBlockList().keySet()){
			event.getRegistry().register(block);
		}
	}
	
	public final void registerItems(RegistryEvent.Register<Item> event){
		LOGGER.debug("Registering Mod Items...");
		for(Block block : ModBlocks.instance().getModBlockList().keySet()){
			if(block instanceof ICustomItemBlock){
				
			} else {
				ItemGroup group = ModBlocks.instance().getModBlockList().get(block);
				event.getRegistry().register((new BlockItem(block, (new Item.Properties()).group(group))).setRegistryName(block.getRegistryName()));
			}
		}
		for(Item item : ModItems.instance().getModItemList()){
			event.getRegistry().register(item);
		}

		//Now that the mod's items registered, replace vanilla armor repair materials with modded items
		//TODO hook this up to config file with the toggleable recipes
		for(Field field : ArmorMaterial.class.getDeclaredFields()) {
			if(field.getType().equals(LazyValue.class)) {
				try {
					LOGGER.debug("Overriding vanilla repair materials");
					field.setAccessible(true);
					Supplier<Ingredient> supplier = (() -> {return Ingredient.fromItems(ModItems.instance().adamant_plate);});
					field.set(ArmorMaterial.DIAMOND, new LazyValue<Ingredient>(supplier));

					supplier = (() -> {return Ingredient.fromItems(ModItems.instance().iron_plate);});
					field.set(ArmorMaterial.IRON, new LazyValue<Ingredient>(supplier));

					supplier = (() -> {return Ingredient.fromItems(ModItems.instance().gold_plate);});
					field.set(ArmorMaterial.GOLD, new LazyValue<Ingredient>(supplier));
					
					field.setAccessible(false);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}
	
	public final void registerContainers(Register<TileEntityType<?>> event) {
		LOGGER.debug("Registering Mod TileEntityTypes...");
        event.getRegistry().register(castFurnaceTEType);
	}
	
	public final void registerContainerTypes(RegistryEvent.Register<ContainerType<?>> event){
		event.getRegistry().register(getCastFurnaceContainerType());
		
	}

	public void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
		IRecipeSerializer.register(Artimancy.MODID+":toggle_shaped_recipe", TOGGLE_SHAPED_SERIALIZER);
		IRecipeSerializer.register(Artimancy.MODID+":casting_recipe", CastingRecipeSerializer.INSTANCE);
	}
	
	public void blockDrops(BlockEvent.BreakEvent event){
		Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
		boolean silking = 0 != EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, event.getPlayer().getActiveItemStack());
		
		
		if(block.equals(Blocks.IRON_ORE) && !silking) {
			if(!event.isCanceled()) {
				double x = event.getPos().getX() + 0.5d;
				double y = event.getPos().getY() + 0.5d;
				double z = event.getPos().getZ() + 0.5d;
				event.getWorld().addEntity(new ItemEntity((World) event.getWorld(), x, y, z, new ItemStack(ModItems.instance().sulfur, 1)));
			}
		}
	}
	
	private static final class ToggleShapedSerializer extends ShapedRecipe.Serializer{
		
		public ShapedRecipe read(ResourceLocation recipeId, JsonObject json) {
			Artimancy.instance().getLogger().debug("Loading toggleable recipe from json: "+recipeId+" ("+(removeVanillaRecipes ? "disabled" : "enabled")+")");
	        ShapedRecipe main = super.read(recipeId, json) ;
	        if(removeVanillaRecipes) {
	        	ShapedRecipe nulled = new ShapedRecipe(main.getId(),main.getGroup(),main.getHeight(),main.getWidth(), main.getIngredients(), ItemStack.EMPTY){
	        		@Override
	        		public boolean isDynamic() {
	        		      return true;
	        		}
	        	};
	        	return nulled;
	        }
			return main;
	      }
		
		public ShapedRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			Artimancy.instance().getLogger().debug("Loading toggleable recipe from packet: "+recipeId+" ("+(removeVanillaRecipes ? "disabled" : "enabled")+")");
			ShapedRecipe main = super.read(recipeId, buffer) ;
			if(removeVanillaRecipes) {
				ShapedRecipe nulled = new ShapedRecipe(main.getId(),main.getGroup(),main.getHeight(),main.getWidth(), main.getIngredients(), ItemStack.EMPTY){
					@Override
					public boolean isDynamic() {
						return true;
					}
					
					public IRecipeSerializer<?> getSerializer() {
						return CommonHandler.TOGGLE_SHAPED_SERIALIZER;
					}
				};
				return nulled;
			}
			return main;
	      }
	}

}
