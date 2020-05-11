package azaka7.artimancy.common.tileentity;

import java.util.Optional;

import javax.annotation.Nullable;

import azaka7.artimancy.Artimancy;
import azaka7.artimancy.common.block.CastFurnaceBlock;
import azaka7.artimancy.common.crafting.CastingRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.LazyOptional;

public class CastFurnaceTileEntity extends LockableTileEntity implements ITickableTileEntity, ISidedInventory, IRecipeHelperPopulator
{

	public static final IRecipeType<CastingRecipe> CAST_RECIPE_TYPE = IRecipeType.register(Artimancy.MODID+"_casting");
	
	public CastFurnaceTileEntity() {
		super(Artimancy.instance().getCastFurnaceType());
		timing = new FurnaceTiming(0, 0, 0, 0);
	}

	private static final int[] SLOTS_TOP = new int[] {0,1};
    private static final int[] SLOTS_BOTTOM = new int[] {4,5};
    private static final int[] SLOTS_SIDES = new int[] {2,3};
    
    private FurnaceTiming timing;
    private String customName;
    
    /**
     * main_input, secondary_input, cast, fuel, main_result, secondary_result
     */
	private NonNullList<ItemStack> items = NonNullList.<ItemStack>withSize(6, ItemStack.EMPTY);
	
	private CastingRecipe lastValidRecipe = null;
	
	public CastingRecipe getCurrentRecipe() {
		if(lastValidRecipe != null && lastValidRecipe.matches(this, this.world)) {
			return lastValidRecipe;
		}
		Optional<CastingRecipe> opt = this.world.getRecipeManager().getRecipe(CAST_RECIPE_TYPE, this, this.world);
		return opt.isPresent() ? opt.get() : null;
	}

	public FurnaceTiming getTiming() {
		return timing;
	}
	
	@Override
	public int getSizeInventory() {
		return this.items.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.items)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.items.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(this.items, index, count);
    }
	
	@Override
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.items, index);
    }
    
	@Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.items.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index >= 0 && index <=2)
        {
            timing.setItemCookTime(this.getItemCookTime());
            if(timing.getItemCookTime() > 0) {
            	timing.setCookTime(0);
            	this.markDirty();
            }
        }
    }
	
	/**
    * Get the name of this object. For players this returns their username
    */
    public ITextComponent getName()
    {
        return this.hasCustomName() ? new StringTextComponent(this.customName) : new TranslationTextComponent(Artimancy.MODID+".container.cast_furnace");
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName()
    {
        return this.customName != null && !this.customName.isEmpty();
    }

    public void setCustomInventoryName(String p_145951_1_)
    {
        this.customName = p_145951_1_;
    }

    @Override
    public void read(CompoundNBT compound)
    {
        super.read(compound);
        this.items = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.items);
        timing.setBurnTime(compound.getInt("BurnTime"));
        timing.setItemBurnTime(compound.getInt("BurnTimeTotal"));
        timing.setCookTime(compound.getInt("CookTime"));
        timing.setItemCookTime(compound.getInt("CookTimeTotal"));
        timing.setItemBurnTime(getItemBurnTime(this.items.get(1)));

        if (compound.contains("CustomName"))
        {
            this.customName = compound.getString("CustomName");
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        compound.putInt("BurnTime", (short)timing.getBurnTime());
        compound.putInt("BurnTimeTotal", (short)timing.getItemBurnTime());
        compound.putInt("CookTime", (short)timing.getCookTime());
        compound.putInt("CookTimeTotal", (short)timing.getItemCookTime());
        ItemStackHelper.saveAllItems(compound, this.items);

        if (this.hasCustomName())
        {
            compound.putString("CustomName", this.customName);
        }

        return compound;
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Furnace isBurning
     */
    public boolean isBurning()
    {
        return timing.getBurnTime() > 0;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    @Override
    public void tick() {
        boolean toMarkDirty = false;

        if (this.isBurning())
        {
            timing.setBurnTime(timing.getBurnTime() - 1);
        }

        if (!this.world.isRemote)
        {
        	
            ItemStack fuel = this.items.get(3);
            
            if (this.isBurning() || !fuel.isEmpty() && !((ItemStack)this.items.get(0)).isEmpty())
            {
                if (!this.isBurning() && this.canSmelt())
                {
                    timing.setBurnTime(getItemBurnTime(fuel));
                    timing.setItemBurnTime(timing.getBurnTime());
                    timing.setCookTime(timing.getCookTime() + 1);
                    
                    if (this.isBurning())
                    {
                        toMarkDirty = true;
                        if (!fuel.isEmpty())
                        {
                            fuel.shrink(1);

                            if (fuel.isEmpty())
                            {
                                ItemStack item1 = fuel.getItem().getContainerItem(fuel);
                                this.items.set(3, item1);
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canSmelt())
                {
                    timing.setCookTime(timing.getCookTime() + 1);

                    if (timing.getCookTime() == timing.getItemCookTime())
                    {
                        timing.setCookTime(0);
                        timing.setItemCookTime(this.getItemCookTime());
                        this.smeltItem();
                        toMarkDirty = true;
                    }
                }
                else
                {
                    timing.setCookTime(0);
                }
            }
            else if (!this.isBurning() && timing.getCookTime() > 0)
            {
                timing.setCookTime(MathHelper.clamp(timing.getCookTime() - 2, 0, timing.getItemCookTime()));
            }
            
            world.setBlockState(pos, getBlockState().with(CastFurnaceBlock.LIT, this.isBurning()));
        }

        if (toMarkDirty)
        {
            this.markDirty();
        }
    }

    public int getItemCookTime()
    {
    	CastingRecipe rec = this.getCurrentRecipe();
        return rec == null ? 200 : rec.getCookTime();
    }

    /**
     * Returns true if the furnace can smelt an item, i.e. has a source item, destination stack isn't full, etc.
     */
    
	private boolean canSmelt()
    {
    	ItemStack input1 = this.items.get(0);
    	ItemStack cast = this.items.get(2);
    	
    	if(input1.isEmpty() || cast.isEmpty()){
    		return false;
    	}
    	
    	CastingRecipe recipe = this.getCurrentRecipe();
        if(recipe != null && recipe.matches(this, getWorld())) {
        	//Only allow smelting if the inputs match and the output slots can hold the new recipe output.
        	return recipe.result1Stacks(this.items.get(4)) && recipe.result2Stacks(this.items.get(5));
        }
        return false;
    }

    /**
     * Turn items from the furnace source stacks into the appropriate smelted items in the furnace result stack
     */
    public void smeltItem()
    {
        if (this.canSmelt())
        {

        	ItemStack input1 = this.items.get(0);
        	ItemStack input2 = this.items.get(1);
        	
        	CastingRecipe recipe = this.getCurrentRecipe();
            ItemStack newOutput = recipe.getCraftingResult(this);
            ItemStack newOutput2 = recipe.getSecondaryResult(this);
            
            ItemStack inOutput = this.items.get(4);
            ItemStack inOutput2 = this.items.get(5);

            if (inOutput.isEmpty())
            {
                this.items.set(4, newOutput.copy());
            }
            else if (inOutput.getItem() == newOutput.getItem())
            {
                inOutput.grow(newOutput.getCount());
            }
            
            if (inOutput2.isEmpty())
            {
                this.items.set(5, newOutput2.copy());
            }
            else if (inOutput2.getItem() == newOutput2.getItem())
            {
                inOutput2.grow(newOutput2.getCount());
            }

            input1.shrink(recipe.getIngredient1Amount());
            input2.shrink(recipe.getIngredient2Amount());
        }
    }

    /**
     * Returns the number of ticks that the supplied fuel item will keep the furnace burning, or 0 if the item isn't
     * fuel
     */
    @SuppressWarnings("deprecation")
	public static int getItemBurnTime(ItemStack stack)
    {
    	if (stack.isEmpty()) {
            return 0;
         } else {
            return net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(stack, stack.getBurnTime() == -1 ? AbstractFurnaceTileEntity.getBurnTimes().getOrDefault(stack.getItem(), 0) : stack.getBurnTime());
         }
    }

    public static boolean isItemFuel(ItemStack stack)
    {
        return getItemBurnTime(stack) > 0;
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean isUsableByPlayer(PlayerEntity player)
    {
        if (this.world.getTileEntity(this.pos) != this)
        {
            return false;
        }
        else
        {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     */
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
    	if (index == 3){
    		return true;
    	}
    	else if (index == 2)
        {
            return false;
        }
        else if (index != 1)
        {
            return true;
        }
        else
        {
            ItemStack itemstack = this.items.get(1);
            return isItemFuel(stack) || BurnFuelSlot.isBucket(stack) && itemstack.getItem() != Items.BUCKET;
        }
    }
    
    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return side == Direction.UP ? SLOTS_TOP : (side == Direction.DOWN ? SLOTS_BOTTOM : SLOTS_SIDES);
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side.
     */
    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction)
    {
    	if(direction == Direction.DOWN) { return false; }
    	for(int i : getSlotsForFace(direction)) {
    		if(i == index) {
    			return this.isItemValidForSlot(index, itemStackIn);
    		}
    	}
    	return false;
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side.
     */
    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction)
    {
        if (direction == Direction.DOWN && index == 3)
        {
            if (stack.getItem() != Items.WATER_BUCKET && stack.getItem() != Items.BUCKET)
            {
                return false;
            }
        }

        return true;
    }
    
    @Override
	protected Container createMenu(int id, PlayerInventory playerInventory) {
    	return new CastFurnaceContainer(id, playerInventory, this, this.timing);
	}

    public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return timing.getBurnTime();
            case 1:
                return timing.getItemBurnTime();
            case 2:
                return timing.getCookTime();
            case 3:
                return timing.getItemCookTime();
            default:
                return 0;
        }
    }

    public void setField(int id, int value)
    {
        switch (id)
        {
            case 0:
                timing.setBurnTime(value);
                break;
            case 1:
                timing.setItemBurnTime(value);
                break;
            case 2:
                timing.setCookTime(value);
                break;
            case 3:
                timing.setItemCookTime(value);
                break;
        }
    }

    public int getFieldCount()
    {
        return 4;
    }

    public void clear()
    {
        this.items.clear();
    }

	@Override
	protected ITextComponent getDefaultName() {
		return getName();
	}

	private final LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers =
	           net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

	@Override
	public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
	   if (!this.removed && facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
	      if (facing == Direction.UP)         return handlers[0].cast();
	      else if (facing == Direction.DOWN)  return handlers[1].cast();
	      else                                 return handlers[2].cast();
	   }
	      return super.getCapability(capability, facing);
	}
	
	public String toString() {
		return "CastFurnaceTileEntity("+Integer.toHexString(this.hashCode())+")";
	}

	@Override
	public void fillStackedContents(RecipeItemHelper helper) {
		for(ItemStack itemstack : this.items) {
	         helper.accountStack(itemstack);
	      }
	}
	
}