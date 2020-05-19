package azaka7.artimancy.common.item;

import com.google.common.collect.ImmutableList.Builder;

import azaka7.artimancy.Artimancy;
import net.minecraft.item.HoeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ModHoeItem extends HoeItem{

	public ModHoeItem(String name, IItemTier tier, float attackSpeed, ItemGroup tab, Builder<Item> itemList) {
		super(tier, attackSpeed, (new Item.Properties()).group(tab));
		this.setRegistryName(Artimancy.MODID+":"+name);
		itemList.add(this);
	}

}
