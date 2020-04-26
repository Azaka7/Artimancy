package azaka7.artimancy.common.tileentity;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class BurnFuelSlot extends Slot{
	private final AbstractBurnContainer container;

	   public BurnFuelSlot(AbstractBurnContainer abc, IInventory inventory, int i, int j, int k) {
	      super(inventory, i, j, k);
	      this.container = abc;
	   }

	   /**
	    * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
	    */
	   public boolean isItemValid(ItemStack stack) {
	      return this.container.isFuel(stack) || isBucket(stack);
	   }

	   public int getItemStackLimit(ItemStack stack) {
	      return isBucket(stack) ? 1 : super.getItemStackLimit(stack);
	   }

	   public static boolean isBucket(ItemStack stack) {
	      return stack.getItem() == Items.BUCKET;
	   }
}
