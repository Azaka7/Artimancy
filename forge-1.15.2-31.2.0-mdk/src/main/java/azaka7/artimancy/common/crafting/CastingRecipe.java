package azaka7.artimancy.common.crafting;

import azaka7.artimancy.common.ModBlocks;
import azaka7.artimancy.common.tileentity.CastFurnaceTE;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class CastingRecipe implements IRecipe<IInventory> {
	
	//These keys are used to prevent the recipe from showing up in any crafting book except the casting furnace's book.
	public static final int width_key = -663941167; //"azaka7" base 36 to base 10, made negative to prevent vanilla crafting recipes
	public static final int height_key = -573869;   //"cast"   base 36 to base 10, made negative to prevent vanilla crafting recipes
	
	private final ResourceLocation id;
	private final String group;
	private final AmountedIngredient ingredient, ingredient2;
	private final Ingredient cast;
	private final ItemStack result, result2;
	private final float experience;
	private final int cookTime;
	
	public CastingRecipe(ResourceLocation id, String group, AmountedIngredient ingredient, AmountedIngredient ingredient2, Ingredient cast, ItemStack result, ItemStack result2, float experience, int cookTime) {
		this.id = id;
		this.group = group;
		this.ingredient = ingredient;
		this.ingredient2 = ingredient2;
		this.cast = cast;
		this.result = result;
		this.result2 = result2;
		this.experience = experience;
		this.cookTime = cookTime;
	}
	
	@Override
	public ItemStack getIcon() {
		return new ItemStack(ModBlocks.instance().cast_furnace);
	}
	
	@Override
	public boolean matches(IInventory inv, World worldIn) {
		if(!(inv instanceof CastFurnaceTE)) {return false;}
		CastFurnaceTE tecf = (CastFurnaceTE) inv;
		ItemStack input1 = tecf.getStackInSlot(0);
		ItemStack input2 = tecf.getStackInSlot(1);
		ItemStack inCast = tecf.getStackInSlot(2);
		
		//If the input ingredient and cast match, then only check ingredient2 if applicable.
		if(ingredient.test(input1) && input1.getCount() >= ingredient.getAmount() && cast.test(inCast)) {
			if(ingredient2 == null || ingredient2.hasNoMatchingItems() || ingredient2.equals(AmountedIngredient.EMPTY)) {
				return true;
			} else {
				return ingredient2.test(input2) && input2.getCount() >= ingredient2.getAmount();
			}
		}
		
		return false;
	}
	
	public NonNullList<AmountedIngredient> getAmountedIngredients() {
		NonNullList<AmountedIngredient> ret = NonNullList.create();
		ret.add(ingredient);
		if(ingredient2 != AmountedIngredient.EMPTY) {
			ret.add(ingredient2);
		}
		return ret;
	}
	
	public Ingredient getCast() {
		return this.cast;
	}
	
	public int getIngredient1Amount() {
		return ingredient.getAmount();
	}
	
	public int getIngredient2Amount() {
		return ingredient2.getAmount();
	}
	
	public boolean result1Stacks(ItemStack stack) {
		return stack == null || stack.isEmpty() || ItemStack.areItemsEqual(stack, result);
	}
	
	public boolean result2Stacks(ItemStack stack) {
		return stack == null || stack.isEmpty() || ItemStack.areItemsEqual(stack, result2);
	}

	@Override
	public ItemStack getCraftingResult(IInventory inv) {
		return result.copy();
	}
	
	public ItemStack getSecondaryResult(IInventory inv) {
		return result2.copy();
	}

	@Override
	public boolean canFit(int width, int height) {
		return width == width_key && height == height_key; 
	}

	@Override
	public ItemStack getRecipeOutput() {
		return result;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}
	
	@Override
	public String getGroup() {
	      return this.group;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return CastingRecipeSerializer.instance();
	}

	@Override
	public IRecipeType<?> getType() {
		return CastFurnaceTE.CAST_RECIPE_TYPE;
	}

	public int getCookTime() {
		return cookTime > 0 ? cookTime : 100;
	}

	public ItemStack getRecipeBonus() {
		return this.result2;
	}
	
	public float getExp() {
		return this.experience;
	}

}
