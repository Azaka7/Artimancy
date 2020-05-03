package azaka7.artimancy.common.item;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import azaka7.artimancy.Artimancy;
import azaka7.artimancy.common.magic.AbstractSpell;
import azaka7.artimancy.common.magic.Spells;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TieredItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class StaffItem extends TieredItem{
	
	private final IItemTier material;
	
	private static final ArrayList<Enchantment> valid_enchants = Lists.newArrayList(Enchantments.FIRE_ASPECT,
			Enchantments.KNOCKBACK, Enchantments.LOOTING, Enchantments.SILK_TOUCH);

	public StaffItem(String name, IItemTier tierIn, Properties builder) {
		super(tierIn, builder);
		this.setRegistryName(Artimancy.MODID+":"+name);
		material = tierIn;
	}

	public IItemTier getItemTier() {
		return material;
	}
	
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		
		AbstractSpell spell = Spells.getSpell(itemstack);
		
		if(spell != null) {
			int focus = EnchantmentHelper.getEnchantmentLevel(Artimancy.instance().commonProxy().FOCUS_ENCH, itemstack);
			int vigor = EnchantmentHelper.getEnchantmentLevel(Artimancy.instance().commonProxy().VIGOR_ENCH, itemstack);
			
			playerIn.setActiveHand(handIn);
			spell.castSpell(playerIn, vigor, focus);
			
			playerIn.getCooldownTracker().setCooldown(this, 20);
			
			return ActionResult.resultConsume(itemstack);
		}
		
		return ActionResult.resultFail(itemstack);
	}
	
	public ITextComponent getDisplayName(ItemStack stack) {
		AbstractSpell spell = Spells.getSpell(stack);
		if(spell != null && !stack.hasDisplayName()) {
			return super.getDisplayName(stack).appendSibling(new StringTextComponent(" (")).appendSibling(new TranslationTextComponent(spell.getID())).appendSibling(new StringTextComponent(")"));
		}
		
		return super.getDisplayName(stack);
	}
	
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if(!(stack.getItem() instanceof StaffItem)) {return;}
		AbstractSpell spell = Spells.getSpell(stack);
		if(spell != null) {
			tooltip.add(1, (new TranslationTextComponent("artimancy.spell_label")).appendSibling(new StringTextComponent(": ")).appendSibling(new TranslationTextComponent(spell.getID())));
		} else {
			tooltip.add(1, new TranslationTextComponent("artimancy.no_spell"));
		}
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
	{
		return enchantment !=null && (enchantment.type.equals(EnchantmentType.BREAKABLE) || enchantment.type.equals(Artimancy.instance().commonProxy().STAFF_TYPE) || valid_enchants.contains(enchantment)) ? true : enchantment.type.canEnchantItem(stack.getItem());
	}
	
	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		return state.getMaterial().equals(Material.GLASS) ? 3.0f*super.getDestroySpeed(stack, state) : super.getDestroySpeed(stack, state);
	}
	
	@SuppressWarnings("deprecation")
	@Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack)
    {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot);
		if (slot == EquipmentSlotType.MAINHAND) {
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 1.0D, AttributeModifier.Operation.ADDITION));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -0.5D, AttributeModifier.Operation.ADDITION));
		}

		return multimap;
    }
	
}
