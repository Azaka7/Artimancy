package azaka7.artimancy.common.item;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList.Builder;

import azaka7.artimancy.Artimancy;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

//INFO: ItemArmorDyable automatically dyes the base of the armor, then renders the overlay over it uncolored
public class SimpleArmorItem extends ArmorItem{

	public SimpleArmorItem(String name, ItemGroup tab, Builder<Item> itemList, IArmorMaterial materialIn, EquipmentSlotType slot) {
		super(materialIn, slot, (new Item.Properties()).group(tab));
		this.setRegistryName(Artimancy.MODID+":"+name);
		itemList.add(this);
	}
	
	/**
     * Returning null from this function will use the default value.
     *
     * @param stack  ItemStack for the equipped armor
     * @param entity The entity wearing the armor
     * @param slot   The slot the armor is in
     * @param type   The subtype, can be null or "overlay"
     * @return Path of texture to bind, or null to use default
     */
    @Nullable
    @Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type)
    {
    	IArmorMaterial material = ((ArmorItem)stack.getItem()).getArmorMaterial();
    	if(material instanceof ModArmorMaterial) {
    		return ((ModArmorMaterial) material).getTexture(stack, entity, slot, type);
    	}
    	return super.getArmorTexture(stack, entity, slot, type);
    }

}
