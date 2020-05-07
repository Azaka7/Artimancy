package azaka7.artimancy.common.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Lists;

import azaka7.artimancy.Artimancy;
import azaka7.artimancy.common.MCMathHelper;
import azaka7.artimancy.common.magic.AbstractSpell;
import azaka7.artimancy.common.magic.Spells;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

public class StaffItem extends ToolItem{
	private static final Set<Block> EFFECTIVE_ON = getEffectiveOn();
	private final IItemTier material;
	
	private static final ArrayList<Enchantment> valid_enchants = Lists.newArrayList(Enchantments.FIRE_ASPECT,
			Enchantments.KNOCKBACK, Enchantments.LOOTING, Enchantments.SILK_TOUCH);

	public StaffItem(String name, IItemTier tierIn, Properties builder) {
		super(0, -2.0f, tierIn, EFFECTIVE_ON, builder.maxDamage(tierIn.getMaxUses()*7));
		this.setRegistryName(Artimancy.MODID+":"+name);
		material = tierIn;
	}

	public IItemTier getItemTier() {
		return material;
	}
	
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand handIn) {
		ItemStack itemstack = player.getHeldItem(handIn);
		
		AbstractSpell spell = Spells.getSpell(itemstack);
		
		if(spell != null) {
			int focus = EnchantmentHelper.getEnchantmentLevel(Artimancy.instance().commonProxy().FOCUS_ENCH, itemstack);
			int vigor = EnchantmentHelper.getEnchantmentLevel(Artimancy.instance().commonProxy().VIGOR_ENCH, itemstack);
			boolean autophagy = EnchantmentHelper.getEnchantmentLevel(Artimancy.instance().commonProxy().AUTOP_ENCH, itemstack) > 0;
			
			int spellCost = Spells.calcSpellCost(spell, vigor, itemstack.getItemEnchantability(), autophagy);
			int pxp = MCMathHelper.getXPFromLevelAndProgress(player.experienceLevel, player.experience);
			
			if(pxp >= spellCost || player.isCreative()) {
				
				player.setActiveHand(handIn);
				
				if(spell.castSpell(player, vigor, focus)) {
					player.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 2.0F, (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.1F + 1.2F);
					
					if(!player.isCreative()) {
						itemstack.damageItem(autophagy ? spellCost : 1, player, (entity) -> {
		                  entity.sendBreakAnimation(handIn);
		                  entity.giveExperiencePoints(7);
						});
						player.giveExperiencePoints(-spellCost);
					}
					
					player.getCooldownTracker().setCooldown(this, 20 - (focus > 4 ? 5 : focus));
					return ActionResult.resultConsume(itemstack);
				}
				
			}
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
			int vigor = EnchantmentHelper.getEnchantmentLevel(Artimancy.instance().commonProxy().VIGOR_ENCH, stack);
			boolean autophagy = EnchantmentHelper.getEnchantmentLevel(Artimancy.instance().commonProxy().AUTOP_ENCH, stack) > 0;
			tooltip.add(1, (new TranslationTextComponent("artimancy.spell_label")).appendSibling(new StringTextComponent(": ")).appendSibling(new TranslationTextComponent(spell.getID())));
			tooltip.add(2, (new TranslationTextComponent("artimancy.XPCostLabel")).appendSibling(new StringTextComponent(": "+Spells.calcSpellCost(spell, vigor, stack.getItemEnchantability(), autophagy))));
		} else {
			tooltip.add(1, new TranslationTextComponent("artimancy.no_spell"));
		}
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
	{
		if(enchantment == Enchantments.EFFICIENCY) return false;
		return enchantment !=null && (enchantment.type.equals(EnchantmentType.BREAKABLE) || enchantment.type.equals(Artimancy.instance().commonProxy().STAFF_TYPE) || valid_enchants.contains(enchantment)) ? true : enchantment.type.canEnchantItem(stack.getItem());
	}
	
	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		return state.getMaterial().equals(Material.GLASS) ? 3.0f*super.getDestroySpeed(stack, state) : super.getDestroySpeed(stack, state);
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
		return false;
	}
	
	@SuppressWarnings("deprecation")
	private static Set<Block> getEffectiveOn(){
		Builder<Block> builder = ImmutableSet.builder();
		for(Block block : ForgeRegistries.BLOCKS.getValues()) {
			if(block.getMaterial(block.getDefaultState()).equals(Material.GLASS)) {
				builder.add(block);
			}
		}
		return builder.build();
	}
	
}
