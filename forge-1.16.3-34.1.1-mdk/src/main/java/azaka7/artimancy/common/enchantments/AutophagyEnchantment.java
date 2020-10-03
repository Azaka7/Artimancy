package azaka7.artimancy.common.enchantments;


import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;

public class AutophagyEnchantment extends Enchantment {

	public AutophagyEnchantment(String name, EnchantmentType type) {
		super(Rarity.RARE, type, new EquipmentSlotType[] {EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND});
		this.name = name;
		this.setRegistryName(name);
	}
	
	protected boolean canApplyTogether(Enchantment ench) {
		return ench != Enchantments.UNBREAKING;
	}
	
	/**
	* Returns the minimal value of enchantability needed on the enchantment level passed.
	*/
	public int getMinEnchantability(int enchantmentLevel) {
		return 25;
	}

	public int getMaxEnchantability(int enchantmentLevel) {
		return 80;
	}

	/**
	* Returns the maximum level that the enchantment can have.
	*/
	public int getMaxLevel() {
		return 1;
	}
}
