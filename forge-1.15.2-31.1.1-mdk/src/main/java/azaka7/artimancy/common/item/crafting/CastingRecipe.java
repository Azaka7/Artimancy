package azaka7.artimancy.common.item.crafting;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class CastingRecipe implements IRecipe<IInventory> {

	@Override
	public boolean matches(IInventory inv, World worldIn) {
		return true;
	}

	@Override
	public ItemStack getCraftingResult(IInventory inv) {
		return null;
	}

	@Override
	public boolean canFit(int width, int height) {
		return false;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return null;
	}

	@Override
	public ResourceLocation getId() {
		return null;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return null;
	}

	@Override
	public IRecipeType<?> getType() {
		return null;
	}

}
