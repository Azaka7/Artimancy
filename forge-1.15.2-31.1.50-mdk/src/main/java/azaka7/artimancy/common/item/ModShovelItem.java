package azaka7.artimancy.common.item;

import com.google.common.collect.ImmutableList.Builder;

import azaka7.artimancy.Artimancy;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ShovelItem;

public class ModShovelItem extends ShovelItem{

	public ModShovelItem(String name, IItemTier tier, float attackDamage, float attackSpeed, ItemGroup tab, Builder<Item> itemList) {
		super(tier, attackDamage, attackSpeed, (new Item.Properties()).group(tab));
		this.setRegistryName(Artimancy.MODID+":"+name);
		itemList.add(this);
	}

}
