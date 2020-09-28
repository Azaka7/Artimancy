package azaka7.artimancy.common.tileentity;

import azaka7.artimancy.Artimancy;
import azaka7.artimancy.common.ModBlocks;
import azaka7.artimancy.common.crafting.ArtificeRecipeSerializer;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.network.IContainerFactory;

public class ArtimancyTableContainer extends Container implements IContainerFactory<ArtimancyTableContainer>{
	
	private final IInventory dummyInv = new Inventory(4);
	private final IInventory tableInventory;

	
	private final IWorldPosCallable worldPos;
	
	public ArtimancyTableContainer(int id, PlayerInventory inventory) {
		super(Artimancy.instance().commonProxy().getArtimancyTableContainerType(), id);
		worldPos = IWorldPosCallable.of(inventory.player.world, inventory.player.getPosition());
		tableInventory = dummyInv;
	}

	public ArtimancyTableContainer(int id, PlayerInventory inventory, IWorldPosCallable pos) {
		super(Artimancy.instance().commonProxy().getArtimancyTableContainerType(), id);
		worldPos = pos;
		final ArtimancyTableTE[] teContainer = new ArtimancyTableTE[] {null};
		pos.consume((world, blockpos) -> {
			TileEntity te = world.getTileEntity(blockpos);
			if(te instanceof ArtimancyTableTE) {
				teContainer[0] = (ArtimancyTableTE) te;
			} else {
				teContainer[0] = null;
			}
		});
		tableInventory = teContainer[0];
		//Circle slot
		this.addSlot(new Slot(tableInventory,ArtimancyTableTE.INPUT_SLOT,49,59){
			
			public boolean isItemValid(ItemStack stack) {
				return true; 
			}
			public int getSlotStackLimit() { return 1; }
		 });
		//Triangle slot
		this.addSlot(new Slot(tableInventory,ArtimancyTableTE.SOPHIC_SLOT,25,17){
			
			public boolean isItemValid(ItemStack stack) {
				return ArtificeRecipeSerializer.instance().isValidSophic(stack);
			}
			public int getSlotStackLimit() { return 64; }
		 });
		//Square slot
		this.addSlot(new Slot(tableInventory,ArtimancyTableTE.ELEMENTAL_SLOT,73,17){
			
			public boolean isItemValid(ItemStack stack) {
				return ArtificeRecipeSerializer.instance().isValidElemental(stack); 
			}
			public int getSlotStackLimit() { return 64; }
		 });
		//Result slot
		this.addSlot(new Slot(tableInventory, ArtimancyTableTE.RESULT_SLOT, 131, 34){
			
			public boolean isItemValid(ItemStack stack) {
				return false; 
			}
			public int getSlotStackLimit() { return 1; }
			public ItemStack onTake(PlayerEntity playerIn, ItemStack stackIn) {
				super.onTake(playerIn, stackIn);
				if(tableInventory instanceof ArtimancyTableTE) ((ArtimancyTableTE) tableInventory).onCraft();
				return stackIn;
			}
		 });
		
		for (int i = 0; i < 3; ++i)
		  {
				for (int j = 0; j < 9; ++j)
				{
					 this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
				}
		  }

		  for (int k = 0; k < 9; ++k)
		  {
				this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
		  }
	}
	
	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return isWithinUsableDistance(this.worldPos, playerIn, ModBlocks.instance().artimancy_table);
	}
	
	@Override
	 public ContainerType<?> getType() {
	 	return Artimancy.instance().commonProxy().getArtimancyTableContainerType();
	 }

	public static ArtimancyTableContainer createNew(int windowId, PlayerInventory playerInv, PacketBuffer extraData) {
		IWorldPosCallable pos = IWorldPosCallable.of(playerInv.player.world, extraData.readBlockPos());
		return new ArtimancyTableContainer(windowId,playerInv,pos);
	}

	@Override
	public ArtimancyTableContainer create(int windowId, PlayerInventory inv, PacketBuffer data) {
		return createNew(windowId, inv, data);
	}
	
	/**
	 * Called when the container is closed.
	 */
	public void onContainerClosed(PlayerEntity playerIn) {
		super.onContainerClosed(playerIn);
		this.worldPos.consume((world, pos) -> {
			for(int i = 0; i < tableInventory.getSizeInventory()-1; i++) {
				ItemStack stack = tableInventory.getStackInSlot(i);
				if(stack == null || stack.isEmpty()) { continue; }
				ItemEntity drop = new ItemEntity(world, pos.getX()+0.5f, pos.getY()+0.6f, pos.getZ()+0.5f, tableInventory.removeStackFromSlot(i));
				world.addEntity(drop);
			}
		}
		);
	}
	
	/**
	 * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
	 * inventory and the other inventory(s).
	 */
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

				if (index == 3) {
					this.worldPos.consume((world, pos) -> {
						itemstack1.getItem().onCreated(itemstack1, world, playerIn);
					});
					if (!this.mergeItemStack(itemstack1, 4, 40, true)) {
						return slot.onTake(playerIn, ItemStack.EMPTY);
					}
					slot.onSlotChange(itemstack1, itemstack);
				} else if (index >= 0 && index < 3) {
					 if (!this.mergeItemStack(itemstack1, 4, 40, true))
					 {
						  return ItemStack.EMPTY;
					 }

					 slot.onSlotChange(itemstack1, itemstack);
				} else {
					 if (this.getSlot(1).isItemValid(itemstack1))
					 {
						  if (!this.mergeItemStack(itemstack1, 1, 2, false))
						  {
								return ItemStack.EMPTY;
						  }
					 }
					 else if (this.getSlot(2).isItemValid(itemstack1))
					 {
						  if (!this.mergeItemStack(itemstack1, 2, 3, false))
						  {
								return ItemStack.EMPTY;
						  }
					 }
					 else if (this.getSlot(0).isItemValid(itemstack1))
					 {
						  if (!this.mergeItemStack(itemstack1, 0, 1, false))
						  {
								return ItemStack.EMPTY;
						  }
					 }
					 else if (index >= 4 && index < 31)
					 {
						  if (!this.mergeItemStack(itemstack1, 33, 42, false))
						  {
								return ItemStack.EMPTY;
						  }
					 }
					 else if (index >= 31 && index < 40 && !this.mergeItemStack(itemstack1, 4, 31, false))
					 {
						  return ItemStack.EMPTY;
					 }
				}

				if (itemstack1.isEmpty())
				{
					 slot.putStack(ItemStack.EMPTY);
				}
				else
				{
					 slot.onSlotChanged();
				}

				if (itemstack1.getCount() == itemstack.getCount())
				{
					 return ItemStack.EMPTY;
				}

				ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);
				if (index == 0) {
					playerIn.dropItem(itemstack2, false);
				}
		  }

		  return itemstack;
	 }
	
	private static class ResultSlot extends Slot{
		
		private final IInventory inventory;
		private final PlayerEntity player;
		private int amountCrafted;

		public ResultSlot(IInventory inventoryIn, PlayerEntity playerIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
			inventory = inventoryIn;
			player = playerIn;
		}
		
		public boolean isItemValid(ItemStack stack) {
			return false;
		}
		
		 /**
		 * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new stack.
		 */
		public ItemStack decrStackSize(int amount) {
			if (this.getHasStack()) {
				this.amountCrafted += Math.min(amount, this.getStack().getCount());
			}

			return super.decrStackSize(amount);
		}

		/**
		 * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an
		 * internal count then calls onCrafting(item).
		 */
		protected void onCrafting(ItemStack stack, int amount) {
			this.amountCrafted += amount;
			this.onCrafting(stack);
		}

		protected void onSwapCraft(int p_190900_1_) {
			this.amountCrafted += p_190900_1_;
		}

		/**
		 * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
		 */
		protected void onCrafting(ItemStack stack) {
			if (this.amountCrafted > 0) {
				stack.onCrafting(this.player.world, this.player, this.amountCrafted);
				net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerCraftingEvent(this.player, stack, this.inventory);
			}

			if (this.inventory instanceof IRecipeHolder) {
				((IRecipeHolder)this.inventory).onCrafting(this.player);
			}

			this.amountCrafted = 0;
		}

		public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
			this.onCrafting(stack);
			net.minecraftforge.common.ForgeHooks.setCraftingPlayer(thePlayer);
			NonNullList<ItemStack> nonnulllist = thePlayer.world.getRecipeManager().getRecipeNonNull(ArtimancyTableTE.ARTIFICE_RECIPE_TYPE, this.inventory, thePlayer.world);
			net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);
			for(int i = 0; i < nonnulllist.size(); ++i) {
				ItemStack itemstack = this.inventory.getStackInSlot(i);
				ItemStack itemstack1 = nonnulllist.get(i);
				if (!itemstack.isEmpty()) {
					this.inventory.decrStackSize(i, 1);
					itemstack = this.inventory.getStackInSlot(i);
				}

				if (!itemstack1.isEmpty()) {
					if (itemstack.isEmpty()) {
						this.inventory.setInventorySlotContents(i, itemstack1);
					} else if (ItemStack.areItemsEqual(itemstack, itemstack1) && ItemStack.areItemStackTagsEqual(itemstack, itemstack1)) {
						itemstack1.grow(itemstack.getCount());
						this.inventory.setInventorySlotContents(i, itemstack1);
					} else if (!this.player.inventory.addItemStackToInventory(itemstack1)) {
						this.player.dropItem(itemstack1, false);
					}
				}
			}

			return stack;
		}
	}
}
