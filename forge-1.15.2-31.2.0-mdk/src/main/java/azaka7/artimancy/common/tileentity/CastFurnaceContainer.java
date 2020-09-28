package azaka7.artimancy.common.tileentity;

import java.util.List;

import com.google.common.collect.Lists;

import azaka7.artimancy.Artimancy;
import azaka7.artimancy.common.crafting.CastingRecipe;
import azaka7.artimancy.common.crafting.CastingRecipeSerializer;
import net.minecraft.client.util.RecipeBookCategories;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.IContainerFactory;

public class CastFurnaceContainer extends AbstractBurnContainer implements IContainerFactory<CastFurnaceContainer>{
	
    private final IInventory tileFurnace;
    private FurnaceTiming timing;
    private World world;

    public CastFurnaceContainer(int screenID, PlayerInventory playerInventory)
    {
    	this(screenID, playerInventory, new Inventory(6), new FurnaceTiming());
    }
    
    public CastFurnaceContainer(int screenID, PlayerInventory playerInventory, IInventory furnaceInventory, FurnaceTiming times)
    {
    	super(Artimancy.instance().commonProxy().getCastFurnaceContainerType(), screenID);
    	
        this.tileFurnace = furnaceInventory;
        this.timing = times == null ? new FurnaceTiming() : times;
        this.world = playerInventory.player.getEntityWorld();

        this.addSlot(new Slot(furnaceInventory, 0, 56, 17)); //input1
        this.addSlot(new Slot(furnaceInventory, 1, 33, 17)); //input2
        this.addSlot(new Slot(furnaceInventory, 2, 84, 35)); //cast
        this.addSlot(new BurnFuelSlot(this,furnaceInventory, 3, 56, 53)); //fuel
        this.addSlot(new SlotCastingOutput(playerInventory.player, furnaceInventory, 4, 116, 35)); //output1
        this.addSlot(new SlotCastingOutput(playerInventory.player, furnaceInventory, 5, 143, 35)); //output2

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

        this.trackInt(timing.getBurnTimeIRF());
        this.trackInt(timing.getItemBurnTimeIRF());
        this.trackInt(timing.getCookTimeIRF());
        this.trackInt(timing.getItemCookTimeIRF());
    }
    
    @Override
    public ContainerType<?> getType() {
    	return Artimancy.instance().commonProxy().getCastFurnaceContainerType();
    }

    public int getBurnTime() { return timing.getBurnTime(); }
    public int getItemBurnTime() { return timing.getItemBurnTime(); }
    public int getCookTime() { return timing.getCookTime(); }
    public int getItemCookTime() { return timing.getItemCookTime(); }
    
    public boolean isBurning()
    {
        return timing.getBurnTime() > 0;
    }
    
    public void updateProgressBar(int id, int data)
    {
    	switch (id)
        {
            case 0:
                timing.setBurnTime(data);
                break;
            case 1:
                timing.setItemBurnTime(data);
                break;
            case 2:
                timing.setCookTime(data);
                break;
            case 3:
                timing.setItemCookTime(data);
                break;
        }
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean canInteractWith(PlayerEntity playerIn)
    {
        return this.tileFurnace.isUsableByPlayer(playerIn);
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

            if (index >= 0 && index <=5)
            {
                if (!this.mergeItemStack(itemstack1, 6, 42, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else {
                if (CastingRecipeSerializer.instance().isValidInput(itemstack1))//!FurnaceRecipes.instance().getSmeltingResult(itemstack1).isEmpty())
                {
                    if (!this.mergeItemStack(itemstack1, 0, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (CastFurnaceTE.isItemFuel(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 3, 4, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (!itemstack1.isEmpty() && CastingRecipeSerializer.instance().isValidCast(itemstack1))//itemstack1.getItem() == ModItems.instance().cast)
                {
                    if (!this.mergeItemStack(itemstack1, 2, 3, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 6 && index < 33)
                {
                    if (!this.mergeItemStack(itemstack1, 33, 42, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 33 && index < 42 && !this.mergeItemStack(itemstack1, 4, 31, false))
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

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

	@Override
	public boolean isFuel(ItemStack stack) {
		return CastFurnaceTE.isItemFuel(stack);
	}

	@Override
	public CastFurnaceContainer create(int id, PlayerInventory inv) {
		return new CastFurnaceContainer(id,inv, this.tileFurnace, this.timing);
	}

	@Override
	public CastFurnaceContainer create(int windowId, PlayerInventory inv, PacketBuffer data) {
		BlockPos pos = data.readBlockPos();
		TileEntity te = inv.player.world.getTileEntity(pos);
		IInventory inventory = null;
		if(te instanceof CastFurnaceTE) {
			inventory = (IInventory) te;
		} else {return create(windowId, inv);}
		FurnaceTiming timing = new FurnaceTiming(data.readInt(),data.readInt(),data.readInt(),data.readInt());
		return new CastFurnaceContainer(windowId, inv, inventory, timing);
	}
	
	public static CastFurnaceContainer createNew(int windowId, PlayerInventory inv, PacketBuffer data) {
		BlockPos pos = data.readBlockPos();
		TileEntity te = inv.player.world.getTileEntity(pos);
		IInventory inventory = null;
		if(te instanceof CastFurnaceTE) {
			inventory = (IInventory) te;
		} else {return new CastFurnaceContainer(windowId, inv);}
		FurnaceTiming timing = new FurnaceTiming(data.readInt(),data.readInt(),data.readInt(),data.readInt());
		return new CastFurnaceContainer(windowId, inv, inventory, timing);
	}
	
	@Override
	public String toString() {
		return "CastFurnaceContainer("+Integer.toHexString(this.hashCode())+")[IInventory=\""+tileFurnace+"\",\" FurnaceTiming="+timing+"\"]";
	}

	@Override
	public void fillStackedContents(RecipeItemHelper p_201771_1_) {
		if (this.tileFurnace instanceof IRecipeHelperPopulator) {
	         ((IRecipeHelperPopulator)this.tileFurnace).fillStackedContents(p_201771_1_);
	      }
	}

	@Override
	public void clear() {
		this.tileFurnace.clear();
	}

	@Override
	public boolean matches(IRecipe<? super IInventory> recipeIn) {
		if(recipeIn.getType() != CastFurnaceTE.CAST_RECIPE_TYPE){ return false; }
		return recipeIn.matches(this.tileFurnace, this.world);
	}

	@Override
	public int getOutputSlot() {
		return 5;
	}

	@Override
	public int getWidth() {
		return CastingRecipe.width_key;
	}

	@Override
	public int getHeight() {
		return CastingRecipe.height_key;
	}

	@Override
	public int getSize() {
		return 6;
	}
	
	@Override
	public List<RecipeBookCategories> getRecipeBookCategories() {
		return Lists.newArrayList(RecipeBookCategories.MISC);
	}
}
