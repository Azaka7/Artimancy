package azaka7.artimancy.common.item;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList.Builder;

import azaka7.artimancy.common.magic.AbstractSpell;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
	
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if(spell != null)
			tooltip.add(1, (new TranslationTextComponent("artimancy.XPCostLabel")).appendSibling(new StringTextComponent(": "+spell.baseCost(null))));
	}
	
	

}
