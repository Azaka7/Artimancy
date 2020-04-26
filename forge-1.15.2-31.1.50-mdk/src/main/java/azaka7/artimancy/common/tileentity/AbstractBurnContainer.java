package azaka7.artimancy.common.tileentity;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.item.ItemStack;

public abstract class AbstractBurnContainer extends RecipeBookContainer<IInventory> {

	protected AbstractBurnContainer(ContainerType<?> type, int id) {
		super(type, id);
	}
	
	public abstract boolean isFuel(ItemStack stack);

}
