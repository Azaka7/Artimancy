package azaka7.artimancy.common.item;

import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.PickaxeItem;

import com.google.common.collect.ImmutableList.Builder;

import azaka7.artimancy.Artimancy;

public class ModPickaxeItem extends PickaxeItem{
	
	public ModPickaxeItem(String name, IItemTier tier, int attackDamage, float attackSpeed, ItemGroup tab, Builder<Item> itemList){
		super(tier, attackDamage, attackSpeed, (new Item.Properties()).group(tab));
		this.setRegistryName(Artimancy.MODID+":"+name);
		itemList.add(this);
	}

}
