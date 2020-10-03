package azaka7.artimancy.common.tileentity;

import net.minecraft.util.IntReferenceHolder;

public class FurnaceTiming {
	
	/** The number of ticks that the furnace will keep burning */
	//private int burnTime;
	private IntReferenceHolder burnTime = IntReferenceHolder.single();
    /** The number of ticks that a fresh copy of the currently-burning item would keep the furnace burning for */
	private IntReferenceHolder itemBurnTime = IntReferenceHolder.single();
	private IntReferenceHolder cookTime = IntReferenceHolder.single();
	private IntReferenceHolder itemCookTime = IntReferenceHolder.single();
	
	
	public FurnaceTiming() {
		this(0,0,0,0);
	}
	
	public FurnaceTiming(int bT, int iBT, int cT, int iCT) {
		setBurnTime(bT);
		setItemBurnTime(iBT);
		setCookTime(cT);
		setItemCookTime(iCT);
	}

	public int getBurnTime() {
		return burnTime.get();
	}
	
	public IntReferenceHolder getBurnTimeIRF() {
		return burnTime;
	}
	
	public void setBurnTime(int burnTime) {
		this.burnTime.set(burnTime);;
	}

	public int getItemBurnTime() {
		return itemBurnTime.get();
	}

	public IntReferenceHolder getItemBurnTimeIRF() {
		return itemBurnTime;
	}

	public void setItemBurnTime(int itemBurnTime) {
		this.itemBurnTime.set(itemBurnTime);
	}

	public int getCookTime() {
		return cookTime.get();
	}

	public IntReferenceHolder getCookTimeIRF() {
		return cookTime;
	}

	public void setCookTime(int cookTime) {
		this.cookTime.set(cookTime);
	}

	public int getItemCookTime() {
		return itemCookTime.get();
	}

	public IntReferenceHolder getItemCookTimeIRF() {
		return itemCookTime;
	}

	public void setItemCookTime(int itemCookTime) {
		this.itemCookTime.set(itemCookTime);
	}
	
	@Override
	public String toString() {
		return "FurnaceTiming("+Integer.toHexString(this.hashCode())+")["+burnTime.get()+","+itemBurnTime.get()+","+cookTime.get()+","+itemCookTime.get()+"]";
	}

}
