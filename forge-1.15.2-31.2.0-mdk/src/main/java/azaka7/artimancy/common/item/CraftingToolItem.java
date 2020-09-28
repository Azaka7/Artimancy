package azaka7.artimancy.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import com.google.common.collect.ImmutableList.Builder;

import azaka7.artimancy.Artimancy;

public class CraftingToolItem extends Item{
	
	public CraftingToolItem(String name, ItemGroup tab, Builder<Item> itemList, int maxDamage){
		super((new Item.Properties()).group(tab).maxStackSize(1).maxDamage(maxDamage));
		this.setRegistryName(Artimancy.MODID+":"+name);
		itemList.add(this);
	}
	
	@Override
	public ItemStack getContainerItem(ItemStack itemStack)
    {
        if (!hasContainerItem(itemStack))
        {
            return ItemStack.EMPTY;
        }
        ItemStack ret = itemStack.copy();
        ret.setDamage(itemStack.getDamage() + 1);
        return ret;
    }
	
    @Override
	public boolean hasContainerItem(ItemStack stack)
    {
        return stack.getDamage() < stack.getMaxDamage();
    }
}
