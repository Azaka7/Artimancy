package azaka7.artimancy.common;

import java.util.ArrayList;
import java.util.Comparator;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import azaka7.artimancy.Artimancy;
import azaka7.artimancy.common.tileentity.TileEntityCastFurnace;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.item.Item;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;

public class CastingRecipeHandler {
	
	private final ArrayList<CastingRecipe> recipes = new ArrayList<CastingRecipe>();
	
	public final static Comparator<CastingRecipe> RECIPE_SORT = new Comparator<CastingRecipe>(){
		@Override
		public int compare(CastingRecipe o1, CastingRecipe o2) {
			return o1.specificity() - o2.specificity();
		}
	};
	
	private static final CastingRecipeHandler INSTANCE = new CastingRecipeHandler();
	public static CastingRecipeHandler instance() {
		return INSTANCE;
	}
	
	public void registerModRecipes(){
		final ModItems modItems = ModItems.instance();
		
		register(new GenericCastRecipe(new ItemStack(modItems.cast_iron_ingot), new ItemStack(modItems.cast_nugget), new ItemStack(modItems.cast_iron_nugget, 9)));
		register(new GenericCastRecipe(new ItemStack(modItems.cast_iron_nugget, 9), new ItemStack(modItems.cast_ingot), new ItemStack(modItems.cast_iron_ingot, 1)));
		register(new GenericCastRecipe(new ItemStack(Items.IRON_INGOT), new ItemStack(modItems.cast_nugget), new ItemStack(Items.IRON_NUGGET, 9)));
		register(new GenericCastRecipe(new ItemStack(Items.IRON_NUGGET, 9), new ItemStack(modItems.cast_ingot), new ItemStack(Items.IRON_INGOT, 1)));
		register(new GenericCastRecipe(new ItemStack(Items.COMPASS), new ItemStack(modItems.cast_ingot), new ItemStack(Items.IRON_INGOT, 4)));
		register(new GenericCastRecipe(new ItemStack(Blocks.ANVIL), new ItemStack(modItems.cast_ingot), new ItemStack(Items.IRON_INGOT, 31)));
		register(new GenericCastRecipe(new ItemStack(Blocks.CHIPPED_ANVIL), new ItemStack(modItems.cast_ingot), new ItemStack(Items.IRON_INGOT, 20)));
		register(new GenericCastRecipe(new ItemStack(Blocks.DAMAGED_ANVIL), new ItemStack(modItems.cast_ingot), new ItemStack(Items.IRON_INGOT, 10)));
		register(new GenericCastRecipe(new ItemStack(Items.GOLD_INGOT), new ItemStack(modItems.cast_nugget), new ItemStack(Items.GOLD_NUGGET, 9)));
		register(new GenericCastRecipe(new ItemStack(Items.GOLD_NUGGET, 9), new ItemStack(modItems.cast_ingot), new ItemStack(Items.GOLD_INGOT, 1)));
		register(new GenericCastRecipe(new ItemStack(Items.CLOCK), new ItemStack(modItems.cast_ingot), new ItemStack(Items.GOLD_INGOT, 4)));
		register(new GenericCastRecipe(new ItemStack(Blocks.CLAY, 1), new ItemStack(modItems.cast_plate), new ItemStack(modItems.ceramic_tile)));
		
		//PROCESS ORES
		register(new GenericCastRecipe(new ItemStack(modItems.ore_chunk_iron), new ItemStack(modItems.cast_ingot), new ItemStack(Items.IRON_INGOT, 1)));
		register(new GenericCastRecipe(new ItemStack(modItems.ore_chunk_iron), new ItemStack(modItems.cast_nugget), new ItemStack(Items.IRON_NUGGET, 10)));
		register(new GenericCastRecipe(new ItemStack(modItems.ore_chunk_gold), new ItemStack(modItems.cast_ingot), new ItemStack(Items.GOLD_INGOT, 1)));
		register(new GenericCastRecipe(new ItemStack(modItems.ore_chunk_gold), new ItemStack(modItems.cast_nugget), new ItemStack(Items.GOLD_NUGGET, 10)));
		
		//MAKE CASTS
		register(new GenericCastRecipe(new ItemStack(Blocks.CLAY), new ItemStack(Items.STICK), new ItemStack(modItems.cast_rod)));
		register(new MultiCastRecipe(new ItemStack(Blocks.CLAY), new ItemStack[]{
			new ItemStack(Items.BRICK), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.NETHER_BRICK), new ItemStack(Items.GOLD_INGOT)
		}, new ItemStack(modItems.cast_ingot)));
		register(new MultiCastRecipe(new ItemStack(Blocks.CLAY), new ItemStack[]{
			new ItemStack(Items.GOLD_NUGGET), new ItemStack(Items.IRON_NUGGET)
		}, new ItemStack(modItems.cast_nugget)));
		register(new MultiCastRecipe(new ItemStack(Blocks.CLAY), new ItemStack[]{
			new ItemStack(Blocks.ACACIA_PRESSURE_PLATE), new ItemStack(Blocks.OAK_PRESSURE_PLATE), new ItemStack(Blocks.BIRCH_PRESSURE_PLATE),
			new ItemStack(Blocks.JUNGLE_PRESSURE_PLATE), new ItemStack(Blocks.DARK_OAK_PRESSURE_PLATE), new ItemStack(Blocks.SPRUCE_PRESSURE_PLATE),
			new ItemStack(Blocks.STONE_PRESSURE_PLATE), new ItemStack(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE), new ItemStack(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE)
		}, new ItemStack(modItems.cast_plate)));
		register(new CastFormRecipe(new ItemStack(Blocks.CLAY), new ItemStack(modItems.cast_axe), AxeItem.class));
		
		//REFINE IRON TOOLS
		register(new ToolRefineRecipe(Items.IRON_HELMET, new ItemStack(modItems.cast_ingot), new ItemStack(Items.IRON_INGOT, 5), true));
		register(new ToolRefineRecipe(Items.IRON_HELMET, new ItemStack(modItems.cast_nugget), new ItemStack(Items.IRON_NUGGET, 45), true));
		register(new ToolRefineRecipe(Items.IRON_CHESTPLATE, new ItemStack(modItems.cast_ingot), new ItemStack(Items.IRON_INGOT, 8), true));
		register(new ToolRefineRecipe(Items.IRON_CHESTPLATE, new ItemStack(modItems.cast_nugget), new ItemStack(Items.IRON_NUGGET, 72), true));
		register(new ToolRefineRecipe(Items.IRON_LEGGINGS, new ItemStack(modItems.cast_ingot), new ItemStack(Items.IRON_INGOT, 7), true));
		register(new ToolRefineRecipe(Items.IRON_LEGGINGS, new ItemStack(modItems.cast_nugget), new ItemStack(Items.IRON_NUGGET, 63), true));
		register(new ToolRefineRecipe(Items.IRON_BOOTS, new ItemStack(modItems.cast_ingot), new ItemStack(Items.IRON_INGOT, 4), true));
		register(new ToolRefineRecipe(Items.IRON_BOOTS, new ItemStack(modItems.cast_nugget), new ItemStack(Items.IRON_NUGGET, 36), true));
		register(new ToolRefineRecipe(Items.IRON_PICKAXE, new ItemStack(modItems.cast_ingot), new ItemStack(Items.IRON_INGOT, 3), true));
		register(new ToolRefineRecipe(Items.IRON_PICKAXE, new ItemStack(modItems.cast_nugget), new ItemStack(Items.IRON_NUGGET, 27), true));
		register(new ToolRefineRecipe(Items.IRON_AXE, new ItemStack(modItems.cast_ingot), new ItemStack(Items.IRON_INGOT, 3), true));
		register(new ToolRefineRecipe(Items.IRON_AXE, new ItemStack(modItems.cast_nugget), new ItemStack(Items.IRON_NUGGET, 27), true));
		register(new ToolRefineRecipe(Items.IRON_SWORD, new ItemStack(modItems.cast_ingot), new ItemStack(Items.IRON_INGOT, 2), true));
		register(new ToolRefineRecipe(Items.IRON_SWORD, new ItemStack(modItems.cast_nugget), new ItemStack(Items.IRON_NUGGET, 18), true));
		register(new ToolRefineRecipe(Items.IRON_HOE, new ItemStack(modItems.cast_ingot), new ItemStack(Items.IRON_INGOT, 2), true));
		register(new ToolRefineRecipe(Items.IRON_HOE, new ItemStack(modItems.cast_nugget), new ItemStack(Items.IRON_NUGGET, 18), true));
		register(new ToolRefineRecipe(Items.IRON_SHOVEL, new ItemStack(modItems.cast_ingot), new ItemStack(Items.IRON_INGOT, 1), true));
		register(new ToolRefineRecipe(Items.IRON_SHOVEL, new ItemStack(modItems.cast_nugget), new ItemStack(Items.IRON_NUGGET, 9), true));
		register(new GenericCastRecipe(new ItemStack(Blocks.RAIL,8), new ItemStack(modItems.cast_ingot), new ItemStack(Items.IRON_INGOT, 3)));
		register(new GenericCastRecipe(new ItemStack(Blocks.RAIL,1), new ItemStack(modItems.cast_nugget), new ItemStack(Items.IRON_NUGGET, 3)));
		register(new GenericCastRecipe(new ItemStack(Blocks.IRON_BARS,8), new ItemStack(modItems.cast_ingot), new ItemStack(Items.IRON_INGOT, 3)));
		register(new GenericCastRecipe(new ItemStack(Blocks.IRON_BARS,1), new ItemStack(modItems.cast_nugget), new ItemStack(Items.IRON_NUGGET, 3)));
		register(new GenericCastRecipe(new ItemStack(Items.BUCKET), new ItemStack(modItems.cast_ingot), new ItemStack(Items.IRON_INGOT, 3)));
		register(new GenericCastRecipe(new ItemStack(Items.BUCKET), new ItemStack(modItems.cast_nugget), new ItemStack(Items.IRON_NUGGET, 27)));
		register(new GenericCastRecipe(new ItemStack(Blocks.CAULDRON), new ItemStack(modItems.cast_ingot), new ItemStack(Items.IRON_INGOT, 7)));
		register(new GenericCastRecipe(new ItemStack(Blocks.CAULDRON), new ItemStack(modItems.cast_nugget), new ItemStack(Items.IRON_NUGGET, 63)));
		register(new GenericCastRecipe(new ItemStack(Blocks.IRON_TRAPDOOR), new ItemStack(modItems.cast_ingot), new ItemStack(Items.IRON_INGOT, 4)));
		register(new GenericCastRecipe(new ItemStack(Blocks.IRON_TRAPDOOR), new ItemStack(modItems.cast_nugget), new ItemStack(Items.IRON_NUGGET, 4)));
		register(new GenericCastRecipe(new ItemStack(Items.MINECART), new ItemStack(modItems.cast_ingot), new ItemStack(Items.IRON_INGOT, 5)));
		register(new GenericCastRecipe(new ItemStack(Items.MINECART), new ItemStack(modItems.cast_nugget), new ItemStack(Items.IRON_NUGGET, 45)));
		//REFINE GOLD TOOLS
		register(new ToolRefineRecipe(Items.GOLDEN_HELMET, new ItemStack(modItems.cast_ingot), new ItemStack(Items.GOLD_INGOT, 5), true));
		register(new ToolRefineRecipe(Items.GOLDEN_HELMET, new ItemStack(modItems.cast_nugget), new ItemStack(Items.GOLD_NUGGET, 45), true));
		register(new ToolRefineRecipe(Items.GOLDEN_CHESTPLATE, new ItemStack(modItems.cast_ingot), new ItemStack(Items.GOLD_INGOT, 8), true));
		register(new ToolRefineRecipe(Items.GOLDEN_CHESTPLATE, new ItemStack(modItems.cast_nugget), new ItemStack(Items.GOLD_NUGGET, 72), true));
		register(new ToolRefineRecipe(Items.GOLDEN_LEGGINGS, new ItemStack(modItems.cast_ingot), new ItemStack(Items.GOLD_INGOT, 7), true));
		register(new ToolRefineRecipe(Items.GOLDEN_LEGGINGS, new ItemStack(modItems.cast_nugget), new ItemStack(Items.GOLD_NUGGET, 63), true));
		register(new ToolRefineRecipe(Items.GOLDEN_BOOTS, new ItemStack(modItems.cast_ingot), new ItemStack(Items.GOLD_INGOT, 4), true));
		register(new ToolRefineRecipe(Items.GOLDEN_BOOTS, new ItemStack(modItems.cast_nugget), new ItemStack(Items.GOLD_NUGGET, 36), true));
		register(new ToolRefineRecipe(Items.GOLDEN_PICKAXE, new ItemStack(modItems.cast_ingot), new ItemStack(Items.GOLD_INGOT, 3), true));
		register(new ToolRefineRecipe(Items.GOLDEN_PICKAXE, new ItemStack(modItems.cast_nugget), new ItemStack(Items.GOLD_NUGGET, 27), true));
		register(new ToolRefineRecipe(Items.GOLDEN_AXE, new ItemStack(modItems.cast_ingot), new ItemStack(Items.GOLD_INGOT, 3), true));
		register(new ToolRefineRecipe(Items.GOLDEN_AXE, new ItemStack(modItems.cast_nugget), new ItemStack(Items.GOLD_NUGGET, 27), true));
		register(new ToolRefineRecipe(Items.GOLDEN_SWORD, new ItemStack(modItems.cast_ingot), new ItemStack(Items.GOLD_INGOT, 2), true));
		register(new ToolRefineRecipe(Items.GOLDEN_SWORD, new ItemStack(modItems.cast_nugget), new ItemStack(Items.GOLD_NUGGET, 18), true));
		register(new ToolRefineRecipe(Items.GOLDEN_HOE, new ItemStack(modItems.cast_ingot), new ItemStack(Items.GOLD_INGOT, 2), true));
		register(new ToolRefineRecipe(Items.GOLDEN_HOE, new ItemStack(modItems.cast_nugget), new ItemStack(Items.GOLD_NUGGET, 18), true));
		register(new ToolRefineRecipe(Items.GOLDEN_SHOVEL, new ItemStack(modItems.cast_ingot), new ItemStack(Items.GOLD_INGOT, 1), true));
		register(new ToolRefineRecipe(Items.GOLDEN_SHOVEL, new ItemStack(modItems.cast_nugget), new ItemStack(Items.GOLD_NUGGET, 9), true));
		
		//CAST PLATES
		register(new GenericCastRecipe(new ItemStack(Items.GOLD_INGOT, 2), new ItemStack(modItems.cast_plate), new ItemStack(modItems.plate_gold)));
		register(new GenericCastRecipe(new ItemStack(modItems.plate_gold), new ItemStack(modItems.cast_ingot), new ItemStack(Items.GOLD_INGOT, 2)));
		register(new GenericCastRecipe(new ItemStack(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE), new ItemStack(modItems.cast_plate), new ItemStack(modItems.plate_gold)));
		register(new GenericCastRecipe(new ItemStack(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE), new ItemStack(modItems.cast_ingot), new ItemStack(Items.GOLD_INGOT, 2)));
		register(new GenericCastRecipe(new ItemStack(Items.IRON_INGOT, 2), new ItemStack(modItems.cast_plate), new ItemStack(modItems.plate_iron)));
		register(new GenericCastRecipe(new ItemStack(modItems.plate_iron), new ItemStack(modItems.cast_ingot), new ItemStack(Items.IRON_INGOT, 2)));
		register(new GenericCastRecipe(new ItemStack(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE), new ItemStack(modItems.cast_plate), new ItemStack(modItems.plate_iron)));
		register(new GenericCastRecipe(new ItemStack(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE), new ItemStack(modItems.cast_ingot), new ItemStack(Items.IRON_INGOT, 2)));
		register(new GenericCastRecipe(new ItemStack(modItems.steel_ingot, 2), new ItemStack(modItems.cast_plate), new ItemStack(modItems.plate_steel)));
		register(new GenericCastRecipe(new ItemStack(modItems.plate_steel), new ItemStack(modItems.cast_ingot), new ItemStack(modItems.steel_ingot, 2)));
	}
	
	/**
	 * Tries to register the casting recipe. Does not override matching recipes.
	 * @return true if the recipe was successfully added.
	 */
	public boolean register(CastingRecipe recipe){
		for(CastingRecipe r : recipes){
			if(r.specificity() > recipe.specificity()){
				break;
			} else if(r.matches(recipe)){
				return false;
			}
		}
		recipes.add(recipe);
		sortRecipes();
		return true;
	}
	
	/**
	 * Tries to register a casting recipe.
	 * @param recipe The recipe to register
	 * @param forced If true, ignores matching recipes and forces the recipe to be added.
	 * @return true if the recipe was successfully added.
	 */
	public boolean register(CastingRecipe recipe, boolean forced){
		if(forced){
			recipes.add(recipe);
			sortRecipes();
			return true;
		} else {
			return register(recipe);
		}
	}
	
	public boolean isValidInput(ItemStack input) {
		for(CastingRecipe recipe : recipes){
			if(recipe.matchesInput(input)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isValidCast(ItemStack input) {
		for(CastingRecipe recipe : recipes){
			if(recipe.matchesCast(input)){
				return true;
			}
		}
		return false;
	}
	
	public CastingRecipe getRecipeFor(ItemStack input, ItemStack cast)
	{
		for(CastingRecipe recipe : recipes){
			if(recipe.matchesInput(input) && recipe.matchesCast(cast)){
				return recipe;
			}
		}
		return null;
	}
	
	private void sortRecipes(){
		recipes.sort(RECIPE_SORT);
	}
	
	
	public static interface CastingRecipe extends IRecipe<TileEntityCastFurnace>{
		
		public boolean matchesInput(ItemStack stack);
		
		@Deprecated
		public int getInputCost();
		
		/**
		 * Checks if the object is a valid input for the recipe's cast.
		 * @param object Generally a  Class, ItemStack, Item, or null
		 * @return true if the object is a valid cast
		 */
		public boolean matchesCast(Object object);

		public ItemStack getOutput(ItemStack in);
		
		/**
		 * 00. the mold is an ItemStack with NBT && the input is an ItemStack with NBT data
		 * <p>05. the mold is an ItemStack with NBT || the input is an ItemStack with NBT data
		 * <p>10. the mold is an ItemStack with amount && the input is an ItemStack with amount
		 * <p>15. the mold is an ItemStack with amount || the input is an ItemStack with amount
		 * <p>20. the mold is an Item && the input is an Item
		 * <p>25. the mold is an Item || the input is an Item
		 * <p>30. the mold is a Class
		 * @return a value representing the preference to this recipe. Higher numbers are checked for last.
		 */
		public int specificity();
		
		public boolean matches(CastingRecipe recipe);
		
		public default boolean matches(TileEntityCastFurnace inv, World worldIn) {
			ItemStack input = inv.getStackInSlot(0);//.furnaceItemStacks.get(0);
	    	ItemStack cast = inv.getStackInSlot(3);//.furnaceItemStacks.get(3);
			return this.equals(INSTANCE.getRecipeFor(input, cast));
		}

		public default ItemStack getCraftingResult(TileEntityCastFurnace inv) {
			ItemStack input = inv.getStackInSlot(0);//.furnaceItemStacks.get(0);
			return this.getOutput(input);
		}

		   /**
		    * Used to determine if this recipe can fit in a grid of the given width/height
		    */
		public default boolean canFit(int width, int height) { return true;}

		   /**
		    * Get the result of this recipe, usually for display purposes (e.g. recipe book). If your recipe has more than one
		    * possible result (e.g. it's dynamic and depends on its inputs), then return an empty stack.
		    */
		ItemStack getRecipeOutput();

		default NonNullList<ItemStack> getRemainingItems(TileEntityCastFurnace inv) {
		   NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

		   for(int i = 0; i < nonnulllist.size(); ++i) {
		      ItemStack item = inv.getStackInSlot(i);
		      if (item.hasContainerItem()) {
		         nonnulllist.set(i, item.getContainerItem());
		      }
		   }

		   return nonnulllist;
		}

		NonNullList<Ingredient> getIngredients();

		/**
		 * If true, this recipe does not appear in the recipe book and does not respect recipe unlocking (and the
		 * doLimitedCrafting gamerule)
		 */
		default boolean isDynamic() {
		   return false;
		}

		/**
		 * Recipes with equal group are combined into one button in the recipe book
		 */
		public String getGroup();

		default ItemStack getIcon() {
		   return new ItemStack(ModBlocks.instance().cast_furnace);
		}

		public ResourceLocation getId();

		IRecipeSerializer<?> getSerializer();

		public default IRecipeType<?> getType(){
			return TileEntityCastFurnace.RECIPE_TYPE;
		}

		public float getExperience();

		public boolean scaledOutput();

		public boolean useCastClass();

		public default boolean matches(ItemStack input, ItemStack cast) {
			return this.matchesInput(input) && this.matchesCast(cast);
		}
		
	}
	
	public static class GenericCastRecipe implements CastingRecipe{
		
		private final ResourceLocation recipeID;
		
		private final String group;
		
		private final ItemStack output;
		
		private final Ingredient input, cast;
		
		private final float experience;
		
		private final int specificity;
		
		private final boolean scaledOutput, useCastClass;
		
		@Deprecated
		public GenericCastRecipe(ItemStack input, ItemStack cast, ItemStack output){
			this.group = "";
			this.input = Ingredient.fromStacks(input.copy());
			this.cast = Ingredient.fromStacks(cast.copy());
			this.output = output.copy();
			this.experience = 0.2f;
			this.specificity = 10;
			this.scaledOutput = false;
			this.useCastClass = false;
			this.recipeID = new ResourceLocation(Artimancy.MODID+":null");
		}

		private GenericCastRecipe(ResourceLocation id, String group, Ingredient input, Ingredient cast, ItemStack output, float experience,
				int specificity, boolean scaledOutput, boolean useCastClass) {
			this.recipeID = id;
			this.group = group == null ? "" : group;
			this.input = input;
			this.cast = cast;
			this.output = output;
			this.experience = experience;
			this.specificity = specificity;
			this.scaledOutput = scaledOutput;
			this.useCastClass = useCastClass;
		}

		@Override
		public boolean matchesInput(ItemStack stack) {
			return input.test(stack);
		}
		
		@Override
		@Deprecated
		public int getInputCost(){
			return 1;
		}

		@Override
		public boolean matchesCast(Object object) {
			if(object instanceof ItemStack){
				return cast.test((ItemStack) object);
			} else if(useCastClass && object instanceof Class) {
				boolean flag = false;
				for(ItemStack stack : cast.getMatchingStacks()) {
					flag = ((Class<?>) object).getClass().isInstance(stack.getItem());
					if(flag) break;
				}
				return flag;
			}
			return false;
		}

		@Override
		public ItemStack getOutput(ItemStack in) {
			if(!matchesInput(in)){return ItemStack.EMPTY;}
			return output.copy();
		}

		@Override
		public int specificity() {
			return specificity;
		}

		@Override
		public boolean matches(CastingRecipe recipe) {
			return recipe == this;
		}

		@Override
		public ItemStack getRecipeOutput() {
			return this.output;
		}

		@Override
		public NonNullList<Ingredient> getIngredients() {
			return NonNullList.from(input, cast);
		}

		@Override
		public IRecipeSerializer<?> getSerializer() {
			return null;
		}
		
		public class Factory implements CastingSerializer.IFactory{

			@SuppressWarnings("unchecked")
			@Override
			public <T extends CastingRecipe> T build(ResourceLocation id, String group, Ingredient input, Ingredient cast, ItemStack output,
					float experience, int specificity, boolean scaledOutput, boolean useResultClass) {
				
				return (T) new GenericCastRecipe(id, group, input, cast, output, experience, specificity, scaledOutput, useResultClass);
			}
			
		}
		
		public String group() { return group; }

		@Override
		public float getExperience() {
			return experience;
		}

		@Override
		public boolean scaledOutput() {
			return scaledOutput;
		}

		@Override
		public boolean useCastClass() {
			return useCastClass;
		}

		@Override
		public String getGroup() {
			return group;
		}

		@Override
		public ResourceLocation getId() {
			return recipeID;
		}
		
	}
	
	@Deprecated
	public static class MultiCastRecipe implements CastingRecipe{
		
		private final ItemStack input, output;
		private final ItemStack[] casts;
		
		public MultiCastRecipe(ItemStack input, ItemStack[] casts, ItemStack output){
			this.input = input.copy();
			this.casts = casts;
			this.output = output.copy();
		}

		@Override
		public boolean matchesInput(ItemStack stack) {
			return ItemStack.areItemsEqual(stack, input);
		}
		
		@Override
		public int getInputCost(){
			return input.getCount();
		}

		@Override
		public boolean matchesCast(Object object) {
			if(object instanceof ItemStack){
				ItemStack stack = (ItemStack) object;
				for(ItemStack cast : casts){
					if(ItemStack.areItemsEqual(stack, cast)){
						return true;
					}
				}
			}
			return false;
		}

		@Override
		public ItemStack getOutput(ItemStack in) {
			if(!matchesInput(in)){return ItemStack.EMPTY;}
			return output.copy();
		}

		@Override
		public int specificity() {
			return 12;
		}

		@Override
		public boolean matches(CastingRecipe recipe) {
			return recipe.matchesCast(casts) && recipe.matchesInput(input.copy());
		}

		@Override
		public ItemStack getRecipeOutput() {
			return null;
		}

		@Override
		public NonNullList<Ingredient> getIngredients() {
			return null;
		}

		@Override
		public IRecipeSerializer<?> getSerializer() {
			return null;
		}

		@Override
		public float getExperience() {
			return 0;
		}

		@Override
		public boolean scaledOutput() {
			return false;
		}

		@Override
		public boolean useCastClass() {
			return false;
		}

		@Override
		public String getGroup() {
			return "";
		}

		@Override
		public ResourceLocation getId() {
			return null;
		}
		
	}
	
	@Deprecated
	public static class CastFormRecipe implements CastingRecipe{
		
		private final ItemStack inputStack;
		private final ItemStack outputStack;
		
		private final Class<? extends Item> moldClass;
		
		public CastFormRecipe(ItemStack input, ItemStack output, Class<? extends Item> mold)
		{
			inputStack = input;
			outputStack = output;
			moldClass = mold;
		}

		@Override
		public boolean matchesInput(ItemStack stack) {
			return ItemStack.areItemsEqual(inputStack, stack);
		}
		
		@Override
		public int getInputCost(){
			return inputStack.getCount();
		}

		@SuppressWarnings("rawtypes")
		@Override
		public boolean matchesCast(Object object) {
			if(object instanceof Class){
				return moldClass.isAssignableFrom((Class) object);
			} else if(object instanceof ItemStack){
				ItemStack stack = (ItemStack) object;
				if(!stack.isEmpty()){
					return moldClass.isInstance(stack.getItem());
				}
				return false;
			} else {
				return moldClass.isInstance(object);
			}
		}

		@Override
		public int specificity() {
			return 30;
		}

		@Override
		public ItemStack getOutput(ItemStack in) {
			if(!matchesInput(in)){return ItemStack.EMPTY;}
			return outputStack.copy();
		}

		@Override
		public boolean matches(CastingRecipe recipe) {
			if(recipe != null){
				return recipe.matchesInput(inputStack) && recipe.matchesCast(moldClass);
			}
			return false;
		}

		@Override
		public ItemStack getRecipeOutput() {
			return null;
		}

		@Override
		public NonNullList<Ingredient> getIngredients() {
			return null;
		}

		@Override
		public IRecipeSerializer<?> getSerializer() {
			return null;
		}

		@Override
		public float getExperience() {
			return 0;
		}

		@Override
		public boolean scaledOutput() {
			return false;
		}

		@Override
		public boolean useCastClass() {
			return false;
		}

		@Override
		public String getGroup() {
			return "";
		}

		@Override
		public ResourceLocation getId() {
			return null;
		}
		
	}
	
	@Deprecated
	public static class ToolRefineRecipe implements CastingRecipe{
		
		private final Item input;
		private final ItemStack castItem, output;
		private final boolean scaleCount;
		
		public ToolRefineRecipe(Item in, ItemStack cast, ItemStack out, boolean scale){
			input = in;
			castItem = cast.copy();
			output = out.copy();
			scaleCount = scale;
		}

		@Override
		public boolean matchesInput(ItemStack stack) {
			return !stack.isEmpty() && input.equals(stack.getItem());
		}

		@Override
		public int getInputCost(){
			return 1;
		}

		@Override
		public boolean matchesCast(Object object) {
			if(object instanceof ItemStack){
				return ItemStack.areItemsEqual(castItem, (ItemStack) object);
			}
			return false;
		}

		@Override
		public ItemStack getOutput(ItemStack in) {
			if(!matchesInput(in)){return ItemStack.EMPTY;}
			if(!scaleCount || !in.getItem().isDamageable()){
				return output.copy();
			}
			
			ItemStack ret = output.copy();
			int i = ret.getCount();
			float j = in.getDamage();
			float k = in.getMaxDamage();
			ret.setCount((int) (i * ((k - j) / k)));
			return ret;
		}

		@Override
		public int specificity() {
			return 15;
		}

		@Override
		public boolean matches(CastingRecipe recipe) { 
			return recipe.matchesInput(new ItemStack(input)) && recipe.matchesCast(castItem);
		}

		@Override
		public ItemStack getRecipeOutput() {
			return null;
		}

		@Override
		public NonNullList<Ingredient> getIngredients() {
			return null;
		}

		@Override
		public IRecipeSerializer<?> getSerializer() {
			return null;
		}

		@Override
		public float getExperience() {
			return 0;
		}

		@Override
		public boolean scaledOutput() {
			return false;
		}

		@Override
		public boolean useCastClass() {
			return false;
		}

		@Override
		public String getGroup() {
			return "";
		}

		@Override
		public ResourceLocation getId() {
			return null;
		}
		
	}
	
	public static class CastingSerializer<T extends CastingSerializer.IFactory> extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CastingRecipe>{
		
		private final IFactory factory;
		
		public CastingSerializer(T factory) {
			this.factory = factory;
		}
		
		@SuppressWarnings("deprecation")
		@Override
		public CastingRecipe read(ResourceLocation recipeId, JsonObject json) {
			String group = JSONUtils.getString(json, "group");
			boolean scaledOutput = JSONUtils.getBoolean(json, "scale-output", false);
			boolean useCastClass = JSONUtils.getBoolean(json, "use-cast-class", false);
			JsonElement inputelement = (JsonElement)(JSONUtils.isJsonArray(json, "input") ? JSONUtils.getJsonArray(json, "input") : JSONUtils.getJsonObject(json, "input"));
		    Ingredient input = Ingredient.deserialize(inputelement);
		    JsonElement castelement = (JsonElement)(JSONUtils.isJsonArray(json, "cast") ? JSONUtils.getJsonArray(json, "cast") : JSONUtils.getJsonObject(json, "cast"));
		    Ingredient cast = Ingredient.deserialize(castelement);
		    if (!json.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
		    ItemStack output;
		    if (json.get("result").isJsonObject()) {
		    	output = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
		    }
		    else {
		    	String result = JSONUtils.getString(json, "result");
		    	ResourceLocation resourcelocation = new ResourceLocation(result);
		    	output = new ItemStack(Registry.ITEM.getValue(resourcelocation).orElseThrow(() -> {
		    	return new IllegalStateException("Item: " + result + " does not exist");
		    }));
		    }
		    float experience = JSONUtils.getFloat(json, "experience", 0.0F);
		    
		    int specificity = 0;
		    specificity += input.getMatchingStacks().length * 10;
		    for(ItemStack stack : input.getMatchingStacks()) {
		    	if(stack.hasTag()) { specificity -= 1; }
		    	if(stack.hasDisplayName()) { specificity -= 2; }
		    	if(stack.getDamage() != 0) { specificity -= 2; }
		    	if(stack.getCount() > 1) { specificity -= 2; }
		    	if(stack.isEnchanted()) { specificity -= 2; }
		    }
		    
		    if(!useCastClass) {
		    	specificity += cast.getMatchingStacks().length * 10;
			    for(ItemStack stack : cast.getMatchingStacks()) {
			    	if(stack.hasTag()) { specificity -= 1; }
			    	if(stack.hasDisplayName()) { specificity -= 2; }
			    	if(stack.getDamage() != 0) { specificity -= 2; }
			    	if(stack.getCount() > 1) { specificity -= 2; }
			    	if(stack.isEnchanted()) { specificity -= 2; }
			    }
		    }
		    return factory.build(recipeId, group, input, cast, output, experience, specificity, scaledOutput, useCastClass);
		}

		@Override
		public CastingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			String group = buffer.readString(32767);
			Ingredient input = Ingredient.read(buffer);
			Ingredient cast = Ingredient.read(buffer);
			ItemStack output = buffer.readItemStack();
			float experience = buffer.readFloat();
			int specificity = buffer.readInt();
			byte bools = buffer.readByte();
			boolean scaledOutput =   (bools & 0b01) == 0b01 ? true : false;
			boolean useResultClass = (bools & 0b10) == 0b10 ? true : false;
			return factory.build(recipeId, group, input, cast, output, experience, specificity, scaledOutput, useResultClass);
		}

		@Override
		public void write(PacketBuffer buffer, CastingRecipe recipe) {
			buffer.writeString(recipe.getGroup());
			recipe.getIngredients().get(0).write(buffer);
			recipe.getIngredients().get(1).write(buffer);
			buffer.writeItemStack(recipe.getRecipeOutput());
			buffer.writeFloat(recipe.getExperience());
			buffer.writeInt(recipe.specificity());
			byte bools = (byte) 0;
			bools += (recipe.scaledOutput()   ? (byte) 0b0001 : (byte) 0);
			bools += (recipe.useCastClass() ? (byte) 0b0010 : (byte) 0);
			buffer.writeByte(bools);
		}
		
		public interface IFactory{
			<T extends CastingRecipe> T build(ResourceLocation id, String group, Ingredient input, Ingredient cast, ItemStack output, float experience, int specificity, boolean scaledOutput, boolean useResultClass);
		}
		
	}
}
