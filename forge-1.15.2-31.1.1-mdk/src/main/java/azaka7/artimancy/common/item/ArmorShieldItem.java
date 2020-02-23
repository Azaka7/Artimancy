package azaka7.artimancy.common.item;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableList.Builder;

import azaka7.artimancy.Artimancy;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.ThornsEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BannerItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ArmorShieldItem extends Item{
	
	private static final UUID SHIELD_MODIFIERS = UUID.fromString("61727469-6D61-6E63-793A-736869656C64"); //"artimancy:shield" to hex fits perfectly, so don't give me shit for making a UUID that someone else could copy, messing up the functionality of everything. This is the most appropriate UUID that could be used here and is literally as secure as text IDs.
	
	public static final IDispenseItemBehavior DISPENSER_BEHAVIOR = new DefaultDispenseItemBehavior() {
		/**
		 * Dispense the specified stack, play the dispense sound and spawn particles.
		 */
		protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
			return ArmorShieldItem.dispense(source, stack) ? stack : super.dispenseStack(source, stack);
		}
	};
	protected final EquipmentSlotType slot = EquipmentSlotType.OFFHAND;
	protected final int damageReduceAmount;
	protected final float toughness;
	protected final IArmorMaterial material;
	protected final float speedMod;

	public static boolean dispense(IBlockSource dispenser, ItemStack itemStack) {
		BlockPos blockpos = dispenser.getBlockPos().offset(dispenser.getBlockState().get(DispenserBlock.FACING));
		List<LivingEntity> list = dispenser.getWorld().getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(blockpos), EntityPredicates.NOT_SPECTATING.and(new EntityPredicates.ArmoredMob(itemStack)));
		if (list.isEmpty()) {
			return false;
		} else {
			LivingEntity livingentity = list.get(0);
			EquipmentSlotType equipmentslottype = MobEntity.getSlotForItemStack(itemStack);
			ItemStack itemstack = itemStack.split(1);
			livingentity.setItemStackToSlot(equipmentslottype, itemstack);
			if (livingentity instanceof MobEntity) {
				((MobEntity)livingentity).setDropChance(equipmentslottype, 2.0F);
				((MobEntity)livingentity).enablePersistence();
			}

			return true;
		}
	}
	
	public ArmorShieldItem(String name, IArmorMaterial materialIn, ItemGroup tab, Builder<Item> itemList, float speed) {
		super((new Item.Properties()).group(tab).defaultMaxDamage((int) (materialIn.getDurability(EquipmentSlotType.HEAD) * 2.5f))); //shield durability = 2.5x helmet durability
		this.addPropertyOverride(new ResourceLocation("blocking"), (thisitem, obj, player) -> {
	         return player != null && player.isHandActive() && player.getActiveItemStack() == thisitem ? 1.0F : 0.0F;
	      });
		
		this.material = materialIn;
		this.damageReduceAmount = materialIn.getDamageReductionAmount(EquipmentSlotType.FEET); //intentionally low passive protection due to blocking ability
		this.toughness = materialIn.getToughness();
		DispenserBlock.registerDispenseBehavior(this, DISPENSER_BEHAVIOR);
		this.speedMod = speed;

		this.setRegistryName(Artimancy.MODID+":"+name);
		itemList.add(this);
	}
	
	public boolean isShield(ItemStack stack, @Nullable LivingEntity entity)
    {
		return true;
    }
	
	public EquipmentSlotType getEquipmentSlot() {
		return this.slot;
	}
	
	public int getItemEnchantability() {
		return this.material.getEnchantability();
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
	{
		return enchantment.type.equals(EnchantmentType.BREAKABLE) || enchantment.type.equals(EnchantmentType.ARMOR_CHEST) ? true : enchantment.type.canEnchantItem(stack.getItem());
	}

	public IArmorMaterial getArmorMaterial() {
		return this.material;
	}
	
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return this.material.getRepairMaterial().test(repair) || super.getIsRepairable(toRepair, repair);
	}
	
	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		BannerItem.appendHoverTextFromTileEntityTag(stack, tooltip);
	}

	/**
	 * returns the action that specifies what animation to play when the items is being used
	 */
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BLOCK;
	}

	/**
	 * How long it takes to use or consume an item
	 */
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	/**
	 * Called to trigger the item's "innate" right click behavior. To handle when this item is used on a Block, see
	 * {@link #onItemUse}.
	 */
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		playerIn.setActiveHand(handIn);
		return ActionResult.func_226249_b_(itemstack);
	}

	/**
	 * Gets a map of item attribute modifiers, used by ItemSword to increase hit damage.
	 */
	public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
		@SuppressWarnings("deprecation")
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot);
		if (equipmentSlot == this.slot) {
			multimap.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(SHIELD_MODIFIERS, "Armor modifier", (double)this.damageReduceAmount, AttributeModifier.Operation.ADDITION));
			multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(SHIELD_MODIFIERS, "Armor toughness", (double)this.toughness, AttributeModifier.Operation.ADDITION));
			if(speedMod != 1.0f) {
				multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(SHIELD_MODIFIERS, "Movement Speed", (double)this.speedMod, AttributeModifier.Operation.MULTIPLY_BASE));
			}
		}

		return multimap;
	}

	public int getDamageReduceAmount() {
		return this.damageReduceAmount;
	}

	public float getToughness() {
		return this.toughness;
	}
	
	/**
	 * Makes Thorns Enchant work when blocking:
	 */
	
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
    	int thorns = EnchantmentHelper.getEnchantmentLevel(Enchantments.THORNS, stack);
    	
    	//If (1) the player was recently attacked and (2) the recent attacker is real, then (3) try thorns, and if thorns activates, then damage the shield more than usual.
		if((entity.ticksExisted - entity.getRevengeTimer()) <= 100 && entity.getRevengeTarget() != null &&  activateThorns(stack, entity, entity.getRevengeTarget(), thorns)) { amount += 3; }
		
		return super.damageItem(stack, amount, entity, onBroken);
	}
	
	public boolean activateThorns(ItemStack stack, LivingEntity user, Entity attacker, int level) {
	      
	      Random random = user.getRNG();
	      if (ThornsEnchantment.shouldHit(level, random)) {
	         if (attacker != null) {
	            attacker.attackEntityFrom(DamageSource.causeThornsDamage(user), (float)ThornsEnchantment.getDamage(level, random));
			    user.setRevengeTarget((LivingEntity)null); //Prevent accidental damaging of attacker more than once per hit by the target
	         } 
	         return true;
	      }
	      return false;
	}
}
