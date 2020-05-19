package azaka7.artimancy.common.item;

import azaka7.artimancy.common.ModItems;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;

public enum ToolTier implements IItemTier{

	STEEL(2, 1019, 7.0f, 2.5f, 12, "artimancy:steel_ingot"),
	CAST_IRON(3, 121, 12.0f, 3.0f, 10, "artimancy:cast_iron_ingot"),
	SILVER(1, 86, 10.0f, 1.0f, 18, "artimancy:silver_ingot"),

	OAK(0, 59, 2.0F, 0.0F, 15, "minecraft:oak_planks"),
	DARK_OAK(0, 65, 1.9F, 0.25F, 18, "minecraft:dark_oak_planks"),
	BIRCH(0, 55, 2.1F, 0.25F, 14, "minecraft:birch_planks"),
	SPRUCE(0, 62, 2.0F, 0.0F, 18, "minecraft:spruce_planks"),
	ACACIA(0, 49, 2.2F, 0.5F, 15, "minecraft:acacia_planks"),
	JUNGLE(0, 69, 2.0F, 0.0F, 16, "minecraft:jungle_planks");
	
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
