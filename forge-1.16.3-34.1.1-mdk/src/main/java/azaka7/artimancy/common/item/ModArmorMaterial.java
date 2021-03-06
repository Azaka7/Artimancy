package azaka7.artimancy.common.item;

import azaka7.artimancy.Artimancy;
import azaka7.artimancy.common.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundEvents;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;

public class ModArmorMaterial implements IArmorMaterial{

	public static final IArmorMaterial STEEL = new ModArmorMaterial(Artimancy.MODID+":steel", 24, new int[] {2,5,6,2}, 5, "steel_plate", 2.0F, 0.0F, SoundEvents.ITEM_ARMOR_EQUIP_IRON);
	public static final IArmorMaterial CASTIRON = new ModArmorMaterial(Artimancy.MODID+":cast_iron", 11, new int[] {3,6,8,3},13,"cast_iron_plate", 1.0F, 0.0F, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND);
	public static final IArmorMaterial IRON_LEATHER = new ModArmorMaterial(Artimancy.MODID+":iron_plated", 12, new int[] {2,4,5,2},12,"iron_plate", 0.0F, 0.0F, SoundEvents.ITEM_ARMOR_EQUIP_IRON);
	public static final IArmorMaterial STEEL_LEATHER = new ModArmorMaterial(Artimancy.MODID+":steel_plated", 15, new int[] {2,4,5,2},10,"steel_plate", 1.0F, 0.0F, SoundEvents.ITEM_ARMOR_EQUIP_IRON);
	public static final IArmorMaterial CASTIRON_LEATHER = new ModArmorMaterial(Artimancy.MODID+":cast_iron_plated", 8, new int[] {3,5,7,3},13,"cast_iron_plate", 0.5F, 0.0F, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND);
	public static final IArmorMaterial GOLD_LEATHER = new ModArmorMaterial(Artimancy.MODID+":gold_plated", 6, new int[] {1,3,4,1},20,"gold_plate", 0.0F, 0.0F, SoundEvents.ITEM_ARMOR_EQUIP_GOLD);

	public static final IArmorMaterial SILVER = new ModArmorMaterial(Artimancy.MODID+":silver", 8, new int[] {2,3,5,2},18,"silver_plate", 0.0F, 0.0F, SoundEvents.ITEM_ARMOR_EQUIP_GOLD);
	public static final IArmorMaterial SILVER_LEATHER = new ModArmorMaterial(Artimancy.MODID+":silver_plated", 6, new int[] {1,3,4,2},18,"silver_plate", 0.0F, 0.0F, SoundEvents.ITEM_ARMOR_EQUIP_GOLD);
	
	
	protected static final int[] durabilityMultipliers = new int[] {13, 15, 16, 11};
	
	private final String name;
	private final int durabilityBase;
	private final int[] damageReduction;
	private final int enchantability;
	private Ingredient repairItem = null;
	private final String repairItemID;
	private final float toughness;
	private final SoundEvent equipSound;
	private final float knockbackResist;
	
	private ModArmorMaterial(String name, int durability, int[] damageRedux, int ench, String repairID, float tough, float kbresist, SoundEvent sound) {
		this.name = name;
		this.durabilityBase = durability;
		this.damageReduction = damageRedux;
		this.enchantability = ench;
		this.repairItemID = repairID;
		this.toughness = tough;
		this.knockbackResist = kbresist;
		this.equipSound = sound;
	}

	public String getTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
		String[] parts = this.getName().split(":");
		String ret = parts[0]+":textures/models/armor/" + parts[1] + "_layer_";
		
		if(slot == EquipmentSlotType.LEGS) {
			ret += "2";
		} else if(slot == EquipmentSlotType.CHEST || slot == EquipmentSlotType.HEAD || slot == EquipmentSlotType.FEET) {
			ret += "1";
		}
		if("overlay".equals(type)) {
			ret += "_overlay"; // Never called for normal armor. make new armor model to call this (make the model awesome, too)
		}
		
		return (ret + ".png");
	}

	@Override
	public int getDurability(EquipmentSlotType slotIn) {
		return durabilityBase * durabilityMultipliers[slotIn.getIndex()];
	}

	@Override
	public int getDamageReductionAmount(EquipmentSlotType slotIn) {
		return damageReduction[slotIn.getIndex()];
	}

	@Override
	public int getEnchantability() {
		return enchantability;
	}

	@Override
	public SoundEvent getSoundEvent() {
		return equipSound;
	}

	@Override
	public Ingredient getRepairMaterial() {
		if(repairItem == null){
			Item rep = ModItems.instance().getItem(repairItemID);
			if(rep != null) {
				repairItem =  Ingredient.fromItems(rep);
			}
		}
		return repairItem;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public float getToughness() {
		return toughness;
	}

	//Knockback resistance
	@Override
	public float func_230304_f_() {
		return knockbackResist;
	}

}
