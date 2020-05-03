package azaka7.artimancy.common.item;

import com.google.common.collect.ImmutableList.Builder;

import azaka7.artimancy.common.magic.AbstractSpell;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class SpellScrollItem extends MiscItem {
	
	private final AbstractSpell spell;

	public SpellScrollItem(String name, AbstractSpell spellToHold, ItemGroup tab, Builder<Item> itemList) {
		super(name, tab, itemList, 1);
		spell = spellToHold;
	}
	
	public boolean hasEffect(ItemStack stack) {
		return spell != null;
	}

	public AbstractSpell getSpell() {
		return spell;
	}
	
	

}
