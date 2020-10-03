package azaka7.artimancy.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;

import azaka7.artimancy.Artimancy;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

public class ModSwordItem extends SwordItem {
		
		private final IItemTier hiltTier;
		protected final Multimap<Attribute, AttributeModifier> attributeMap;
	
		private final float attackDamage;
		private float attackSpeed;
		
		public ModSwordItem(String name, IItemTier tier, IItemTier hilt) {
			this(name, tier, hilt, 3, -2.4F, (new Item.Properties()).group(ItemGroup.COMBAT));
		}

		public ModSwordItem(String name, IItemTier tier, IItemTier hilt, int attackDamageIn, float attackSpeedIn, Item.Properties props) {
			super(tier, attackDamageIn, attackSpeedIn, props.defaultMaxDamage(((3*tier.getMaxUses())+hilt.getMaxUses())/4));
			this.setRegistryName(Artimancy.MODID+":"+name);
			this.attackSpeed = attackSpeedIn;
			this.attackDamage = (float)attackDamageIn + tier.getAttackDamage();
			this.hiltTier = hilt;
			
			Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.put(Attributes.field_233823_f_, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)this.attackDamage, AttributeModifier.Operation.ADDITION));
			builder.put(Attributes.field_233825_h_, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (double)attackSpeedIn, AttributeModifier.Operation.ADDITION));
			this.attributeMap = builder.build();
		}
		
		public IItemTier getHiltTier() {
			
			return this.hiltTier;
		}

		@Override
		public float getAttackDamage() {
			return this.attackDamage;
		}
		
		@Override
		public int getItemEnchantability()
	    {
	        return getHiltTier().getEnchantability();
	    }
		
		public float getAttackSpeed(ItemStack stack) {
			float speed = this.attackSpeed;
			speed *= Math.sqrt(this.getMaxDamage(stack)/this.getTier().getMaxUses());
			return speed;
		}
		
		@Override
	    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack)
	    {
			return slot == EquipmentSlotType.MAINHAND ? this.attributeMap : super.getAttributeModifiers(slot);
	    }

}
