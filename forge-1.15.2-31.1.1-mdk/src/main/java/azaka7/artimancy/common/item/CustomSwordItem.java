package azaka7.artimancy.common.item;

import com.google.common.collect.Multimap;

import azaka7.artimancy.Artimancy;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

public class CustomSwordItem extends SwordItem {
		
		private final IItemTier hiltTier;
	
		private final float attackDamage;
		private float attackSpeed;
		
		public CustomSwordItem(String name, IItemTier tier, IItemTier hilt) {
			this(name, tier, hilt, 3, -2.4F, (new Item.Properties()).group(ItemGroup.COMBAT));
		}

		public CustomSwordItem(String name, IItemTier tier, IItemTier hilt, int attackDamageIn, float attackSpeedIn, Item.Properties builder) {
			super(tier, attackDamageIn, attackSpeedIn, builder.defaultMaxDamage(((3*tier.getMaxUses())+hilt.getMaxUses())/4));
			this.setRegistryName(Artimancy.MODID+":"+name);
			this.attackSpeed = attackSpeedIn;
			this.attackDamage = (float)attackDamageIn + tier.getAttackDamage();
			this.hiltTier = hilt;
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
	    public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack)
	    {
			Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot);
			if (slot == EquipmentSlotType.MAINHAND) {
				multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)this.attackDamage, AttributeModifier.Operation.ADDITION));
				multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (double)this.getAttackSpeed(stack), AttributeModifier.Operation.ADDITION));
			}

			return multimap;
	    }

}
