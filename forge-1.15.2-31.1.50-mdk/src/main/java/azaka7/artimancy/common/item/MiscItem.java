package azaka7.artimancy.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

import com.google.common.collect.ImmutableList.Builder;

import azaka7.artimancy.Artimancy;

public class MiscItem extends Item{
	
	public MiscItem(String name, ItemGroup tab, Builder<Item> itemList){
		super((new Item.Properties()).group(tab));
		this.setRegistryName(Artimancy.MODID+":"+name);
		itemList.add(this);
	}

}
