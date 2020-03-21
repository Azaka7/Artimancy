package azaka7.artimancy.common.item;

import com.google.common.collect.ImmutableList.Builder;

import azaka7.artimancy.Artimancy;
import net.minecraft.item.AxeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ModAxeItem extends AxeItem{

	public ModAxeItem(String name, IItemTier tier, int attackDamage, float attackSpeed, ItemGroup tab, Builder<Item> itemList) {
		super(tier, attackDamage, attackSpeed, (new Item.Properties()).group(tab));
		this.setRegistryName(Artimancy.MODID+":"+name);
		itemList.add(this);
	}

}
