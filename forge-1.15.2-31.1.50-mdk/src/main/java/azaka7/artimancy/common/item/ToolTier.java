package azaka7.artimancy.common.item;

import azaka7.artimancy.common.ModItems;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;

public enum ToolTier implements IItemTier{

	STEEL(2, 1019, 7.0f, 2.5f, 12, "artimancy:steel_ingot"),
	CAST_IRON(3, 121, 12.0f, 3.0f, 10, "artimancy:cast_iron_ingot"),
	SILVER(1, 86, 10.0f, 1.0f, 18, "artimancy:silver_ingot");
	
	private final int maxUses, harvestLevel, enchantability;
	
	private final float efficiency, attackDamage;
	
	private Ingredient repairItem;
	private final String repairID;
	
	private ToolTier(int level, int uses, float efficiency, float attack, int enchant, String repair) {
		this.maxUses = uses;
		this.efficiency = efficiency;
		this.attackDamage = attack;
		this.harvestLevel = level;
		this.enchantability = enchant;
		this.repairID = repair;
	}
	
	@Override
	public int getMaxUses() {
		return maxUses;
	}

	@Override
	public float getEfficiency() {
		return efficiency;
	}

	@Override
	public float getAttackDamage() {
		return attackDamage;
	}

	@Override
	public int getHarvestLevel() {
		return harvestLevel;
	}

	@Override
	public int getEnchantability() {
		return enchantability;
	}

	@Override
	public Ingredient getRepairMaterial() {
		if(repairItem == null){
			Item rep = ModItems.instance().getItem(repairID);
			if(rep != null) {
				repairItem =  Ingredient.fromItems(rep);
			}
		}
		return repairItem;
	}
	
	

}
