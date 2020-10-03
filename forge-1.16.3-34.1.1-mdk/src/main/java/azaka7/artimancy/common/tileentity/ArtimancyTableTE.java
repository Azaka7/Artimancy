package azaka7.artimancy.common.tileentity;

import java.util.Optional;

import javax.annotation.Nullable;

import azaka7.artimancy.Artimancy;
import azaka7.artimancy.common.crafting.ArtificeRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.INameable;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ArtimancyTableTE extends LockableTileEntity implements INameable, ITickableTileEntity, INamedContainerProvider{

	public static final int INPUT_SLOT = 0;
	public static final int SOPHIC_SLOT = 1;
	public static final int ELEMENTAL_SLOT = 2;
	public static final int RESULT_SLOT = 3;
	private final NonNullList<ItemStack> tempStacks = NonNullList.withSize(4, ItemStack.EMPTY);
	
	public static final IRecipeType<ArtificeRecipe> ARTIFICE_RECIPE_TYPE = IRecipeType.register(Artimancy.MODID+"_artifice");
	private final ITextComponent defaultName = new TranslationTextComponent(Artimancy.MODID+".container.artimancy_table");
	private ITextComponent customname;
	
	private ArtificeRecipe lastValidRecipe = null;

	public ArtimancyTableTE() {
		super(Artimancy.instance().commonProxy().getArtimancyTableType());
	}

	@Override
	public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player) {
		return new ArtimancyTableContainer(id, playerInv, IWorldPosCallable.of(getWorld(), getPos()));
	}
	
	public ArtificeRecipe getCurrentRecipe() {
		if(lastValidRecipe != null && lastValidRecipe.matches(this, this.world)) {
			return lastValidRecipe;
		}
		Optional<ArtificeRecipe> opt = this.world.getRecipeManager().getRecipe(ARTIFICE_RECIPE_TYPE, this, this.world);
		if(opt.isPresent()) {
			lastValidRecipe = opt.get();
			return lastValidRecipe;
		}
		return null;
	}

	@Override
	public void tick() {}
	
	public void updateResultSlot() {
		ArtificeRecipe recipe = getCurrentRecipe();
		if(recipe == null) {
			if(!this.getStackInSlot(RESULT_SLOT).isEmpty()) {
				tempStacks.set(RESULT_SLOT, ItemStack.EMPTY);
			}
		} else {
			ItemStack result = recipe.getCraftingResult(this);
			if(this.getStackInSlot(RESULT_SLOT).isEmpty()) {
				tempStacks.set(RESULT_SLOT, result);
			}
		}
	}

	@Override
	public ITextComponent getName() {
		return this.hasCustomName() ? this.getCustomName() : defaultName;
	}

	@Override
	public ITextComponent getDisplayName() {
		return getName();
	}
	
	public void setCustomName(@Nullable ITextComponent name) {
		this.customname = name;
	}
	
	@Override
	@Nullable
	public ITextComponent getCustomName() {
		return this.customname;
	}

	@Override
	public int getSizeInventory() {
		return tempStacks.size();
	}

	@Override
	public boolean isEmpty() {
		for(ItemStack stack : tempStacks) {
			if(!(stack == null || stack.isEmpty())) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return tempStacks.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack ret =  ItemStackHelper.getAndSplit(tempStacks, index, count);
		updateResultSlot();
		return ret;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack ret = ItemStackHelper.getAndRemove(tempStacks, index);
		updateResultSlot();
		return ret;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = tempStacks.get(index);
	    boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
	    tempStacks.set(index, stack);
	    if (stack.getCount() > this.getInventoryStackLimit()) {
	       stack.setCount(this.getInventoryStackLimit());
	    }
	    if(!flag) {
	    	this.markDirty();
	    }
	    updateResultSlot();
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		return this.pos.distanceSq(player.getPosX(), player.getPosY(), player.getPosZ(), true) <= 10;
	}

	@Override
	public void clear() {}

	@Override
	protected ITextComponent getDefaultName() {
		return defaultName;
	}

	@Override
	protected Container createMenu(int id, PlayerInventory playerInv) {
		return new ArtimancyTableContainer(id, playerInv, IWorldPosCallable.of(getWorld(), getPos()));
	}

	//TODO Fix this being called twice when not shift-clicked out.
	public void onCraft() {
		for(int i = 0; i < 3; i++) {
			this.decrStackSize(i, 1);
		}
	}

}
