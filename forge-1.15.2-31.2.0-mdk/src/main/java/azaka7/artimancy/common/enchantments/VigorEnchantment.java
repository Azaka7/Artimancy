package azaka7.artimancy.common.enchantments;


import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public class VigorEnchantment extends Enchantment {

	public VigorEnchantment(String name, EnchantmentType type) {
		super(Rarity.COMMON, type, new EquipmentSlotType[] {EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND});
		this.name = name;
		this.setRegistryName(name);
	}
	
	/**
	* Returns the minimal value of enchantability needed on the enchantment level passed.
	*/
	public int getMinEnchantability(int enchantmentLevel) {
		return 1 + (enchantmentLevel - 1) * 10;
	}

	public int getMaxEnchantability(int enchantmentLevel) {
		return this.getMinEnchantability(enchantmentLevel) + 15;
	}

	/**
	* Returns the maximum level that the enchantment can have.
	*/
	public int getMaxLevel() {
		return 5;
	}
}
