package azaka7.artimancy.common.tileentity;

import java.util.List;

import javax.annotation.Nullable;

import azaka7.artimancy.Artimancy;
import azaka7.artimancy.common.CastingRecipeHandler;
import azaka7.artimancy.common.CastingRecipeHandler.CastingRecipe;
import azaka7.artimancy.common.block.BlockCastFurnace;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.LazyOptional;

public class TileEntityCastFurnace extends LockableTileEntity implements ITickableTileEntity, ISidedInventory
{

	public static final IRecipeType<? extends CastingRecipeHandler.CastingRecipe> RECIPE_TYPE = IRecipeType.register(Artimancy.MODID+"_casting");
	
	public TileEntityCastFurnace() {
		super(Artimancy.instance().getCastFurnaceType());
		timing = new FurnaceTiming(0, 0, 0, 0);
		//TODO make sure new IRecipeType for casting works
	}

	private static final int[] SLOTS_TOP = new int[] {0};
    private static final int[] SLOTS_BOTTOM = new int[] {2, 1};
    private static final int[] SLOTS_SIDES = new int[] {1};
	
    
    private FurnaceTiming timing;
    private String furnaceCustomName;
    
	private NonNullList<ItemStack> furnaceItemStacks = NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);

	public FurnaceTiming getTiming() {
		return timing;
	}
	
	@Override
	public int getSizeInventory() {
		return this.furnaceItemStacks.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.furnaceItemStacks)
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
		return this.furnaceItemStacks.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(this.furnaceItemStacks, index, count);
    }
	
	@Override
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.furnaceItemStacks, index);
    }
    
	@Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        ItemStack itemstack = this.furnaceItemStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.furnaceItemStacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag)
        {
            timing.setItemCookTime(this.getCookTime(stack));
            timing.setCookTime(0);
            this.markDirty();
        }
    }/**
     * Get the name of this object. For players this returns their username
     */
    public ITextComponent getName()
    {
        return this.hasCustomName() ? new StringTextComponent(this.furnaceCustomName) : new StringTextComponent(Artimancy.MODID+".container.cast_furnace");
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName()
    {
        return this.furnaceCustomName != null && !this.furnaceCustomName.isEmpty();
    }

    public void setCustomInventoryName(String p_145951_1_)
    {
        this.furnaceCustomName = p_145951_1_;
    }

    @Override
    public void read(CompoundNBT compound)
    {
        super.read(compound);
        this.furnaceItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.furnaceItemStacks);
        timing.setBurnTime(compound.getInt("BurnTime"));
        timing.setItemBurnTime(compound.getInt("BurnTimeTotal"));
        timing.setCookTime(compound.getInt("CookTime"));
        timing.setItemCookTime(compound.getInt("CookTimeTotal"));
        timing.setItemBurnTime(getItemBurnTime(this.furnaceItemStacks.get(1)));

        if (compound.contains("CustomName"))
        {
            this.furnaceCustomName = compound.getString("CustomName");
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
        ItemStackHelper.saveAllItems(compound, this.furnaceItemStacks);

        if (this.hasCustomName())
        {
            compound.putString("CustomName", this.furnaceCustomName);
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
        boolean flag = this.isBurning();
        boolean flag1 = false;

        if (this.isBurning())
        {
            timing.setBurnTime(timing.getBurnTime() - 1);
        }

        if (!this.world.isRemote)
        {
            ItemStack itemstack = this.furnaceItemStacks.get(1);

            if (this.isBurning() || !itemstack.isEmpty() && !((ItemStack)this.furnaceItemStacks.get(0)).isEmpty())
            {
                if (!this.isBurning() && this.canSmelt())
                {
                    timing.setBurnTime(getItemBurnTime(itemstack));
                    timing.setItemBurnTime(timing.getBurnTime());
                    
                    if (this.isBurning())
                    {
                        flag1 = true;
                        if (!itemstack.isEmpty())
                        {
                            Item item = itemstack.getItem();
                            itemstack.shrink(1);

                            if (itemstack.isEmpty())
                            {
                                ItemStack item1 = item.getContainerItem(itemstack);
                                this.furnaceItemStacks.set(1, item1);
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
                        timing.setItemCookTime(this.getCookTime(this.furnaceItemStacks.get(0)));
                        this.smeltItem();
                        flag1 = true;
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
            
            world.setBlockState(pos, getBlockState().with(BlockCastFurnace.LIT, this.isBurning()));

            if (flag != this.isBurning())
            {
                flag1 = true;
                //BlockCastFurnace.replaceBlock(null, null, this.world, this.pos, 0);//(this.isBurning(), this.world, this.pos);
            }
        }

        if (flag1)
        {
            this.markDirty();
        }
    }

    public int getCookTime(ItemStack stack)
    {
        return 100;
    }

    /**
     * Returns true if the furnace can smelt an item, i.e. has a source item, destination stack isn't full, etc.
     */
    
	private boolean canSmelt()
    {
    	ItemStack input = this.furnaceItemStacks.get(0);
    	ItemStack cast = this.furnaceItemStacks.get(3);
    	if(input.isEmpty() || cast.isEmpty()){
    		//System.out.println("no cast");
    		return false;
    	}
    	
    	@SuppressWarnings("unchecked")
		List<CastingRecipeHandler.CastingRecipe> recipes = this.world.getRecipeManager().getRecipes((IRecipeType<CastingRecipeHandler.CastingRecipe>)RECIPE_TYPE, this, this.world);
        recipes.sort(CastingRecipeHandler.RECIPE_SORT);
        //CastingRecipe recipe = null;
        for(CastingRecipeHandler.CastingRecipe rec : recipes) {
        	if(rec.matches(input, cast)) {
        		//TODO save this recipe to instance (temporary/no nbt) so lookup doesn't always have to happen again when doing smelting actions.
        		return true; 
        	}
        }
        return false;
        /*if (recipe == null)
        {
    		//System.out.println("no recipe");
            return false;
        }
        else
        {
            ItemStack newOutput = recipe.getOutput(input);
            
            if (newOutput.isEmpty() || input.getCount() < recipe.getInputCost())
            {
        		//System.out.println("no output / not enough input");
                return false;
            }
            else
            {
                ItemStack inOutput = this.furnaceItemStacks.get(2);

                if (inOutput.isEmpty() && newOutput.getCount() <= 64)
                {
            		//System.out.println("canSmelt");
                    return true;
                }
                else if (!inOutput.isItemEqual(newOutput))
                {
            		//System.out.println("ouput mismatch");
                    return false;
                }
                else if (inOutput.getCount() + newOutput.getCount() <= this.getInventoryStackLimit() && inOutput.getCount() + newOutput.getCount() <= inOutput.getMaxStackSize())  // Forge fix: make furnace respect stack sizes in furnace recipes
                {
            		//System.out.println("canSmelt");
                    return true;
                }
                else
                {
            		//System.out.println("canSmelt, with less output");
                    return inOutput.getCount() + newOutput.getCount() <= newOutput.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        }*/
    }

    /**
     * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
     */
    public void smeltItem()
    {
        if (this.canSmelt())
        {

        	ItemStack input = this.furnaceItemStacks.get(0);
        	ItemStack cast = this.furnaceItemStacks.get(3);
            CastingRecipe recipe = CastingRecipeHandler.instance().getRecipeFor(input, cast);
            ItemStack newOutput = recipe.getOutput(input);
            ItemStack inOutput = this.furnaceItemStacks.get(2);

            if (inOutput.isEmpty())
            {
                this.furnaceItemStacks.set(2, newOutput.copy());
            }
            else if (inOutput.getItem() == newOutput.getItem())
            {
                inOutput.grow(newOutput.getCount());
            }

            input.shrink(recipe.getInputCost());
        }
    }

    /**
     * Returns the number of ticks that the supplied fuel item will keep the furnace burning, or 0 if the item isn't
     * fuel
     */
    public static int getItemBurnTime(ItemStack stack)
    {
    	if (stack.isEmpty()) {
            return 0;
         } else {
            Item item = stack.getItem();
            int ret = stack.getBurnTime();
            return net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(stack, ret == -1 ? AbstractFurnaceTileEntity.getBurnTimes().getOrDefault(item, 0) : ret);
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
            ItemStack itemstack = this.furnaceItemStacks.get(1);
            return isItemFuel(stack) || BurnFuelSlot.isBucket(stack) && itemstack.getItem() != Items.BUCKET;
        }
    }
    
    @Override
    public int[] getSlotsForFace(Direction side)
    {
        if (side == Direction.DOWN)
        {
            return SLOTS_BOTTOM;
        }
        else
        {
            return side == Direction.UP ? SLOTS_TOP : SLOTS_SIDES;
        }
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side.
     */
    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction)
    {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side.
     */
    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction)
    {
        if (direction == Direction.DOWN && index == 1)
        {
            Item item = stack.getItem();

            if (item != Items.WATER_BUCKET && item != Items.BUCKET)
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
        this.furnaceItemStacks.clear();
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
		return "TileEntityCastFurnace("+Integer.toHexString(this.hashCode())+")";
	}
	
}