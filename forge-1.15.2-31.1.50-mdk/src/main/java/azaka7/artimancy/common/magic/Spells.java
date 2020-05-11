package azaka7.artimancy.common.magic;

import java.util.HashMap;

import azaka7.artimancy.Artimancy;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class Spells {
	
	private static final HashMap<String, AbstractSpell> SPELLS = new HashMap<String,AbstractSpell>();
	private static final String SPELL_NBT = Artimancy.MODID+".spell";

	public static final AbstractSpell TELEPORT = new TeleportSpell("artimancy:teleport");
	public static final AbstractSpell FIREBALL = new FireballSpell("artimancy:fireball");
	public static final AbstractSpell LIGHTNING = new LightningSpell("artimancy:lightning");
	public static final AbstractSpell HEALING = new HealingSpell("artimancy:healing");
	public static final AbstractSpell LIGHT = new LightSpell("artimancy:light");
	public static final AbstractSpell MINING = new MiningSpell("artimancy:mining");
	public static final AbstractSpell LEVITATE = new LevitateSpell("artimancy:levitate");
	public static final AbstractSpell FREEZE = new FreezeSpell("artimancy:freeze");
	
	static {
		registerSpell(TELEPORT);
		registerSpell(FIREBALL);
		registerSpell(LIGHTNING);
		registerSpell(HEALING);
		registerSpell(LIGHT);
		registerSpell(MINING);
		registerSpell(LEVITATE);
		registerSpell(FREEZE);
	}
	
	/**
	 * Store the ID of an AbstractSpell in the NBT of an ItemStack
	 * @param spell The AbstractSpell to store. Must not be null.
	 * @param stack The ItemStack providing the NBT to store the AbstractSpell
	 */
	public static void applySpell(AbstractSpell spell, ItemStack stack) {
		if(spell == null || stack == null || stack.isEmpty()) {return;}
		if(!stack.hasTag()) stack.setTag(new CompoundNBT());
		stack.getTag().putString(SPELL_NBT, spell.getID());
	}
	
	/**
	 * Calculate the actual spell cost based on the spell's base cost and the Vigor enchantment level.
	 * @param spell The spell to be cast
	 * @param vigor The level of the Vigor enchantment being applied
	 * @param enchantability The enchantability of the item being used to cast the spell (Use 15 for no item)
	 * @return The spell cost (in experience points)
	 */
	public static int calcSpellCost(AbstractSpell spell, int vigor, int enchantability, boolean autophagous) {
		return (int) Math.round(spell.baseCost(null)*(1+(0.5*vigor))*15.0f/(enchantability * (autophagous ? 2 : 1)));
	}
	
	/**
	 * Get the AbstractSpell stored in an ItemStack's NBT.
	 * @param stack The ItemStack to get the spell from.
	 * @return The AbstractSpell contained by the item. If no spell is contained, return null.
	 */
	public static AbstractSpell getSpell(ItemStack stack) {
		if(stack == null || stack.isEmpty()) {return null;}
		if(!stack.hasTag() || !stack.getTag().contains(SPELL_NBT)) return null;
		return Spells.getSpellFromID(stack.getTag().get(SPELL_NBT).getString());
	}
	
	
	/**
	 * Try to register the given instance of AbstractSpell. This will only register if the ID is unique.
	 * @param spell An instance of AbstractSpell to be registered.
	 * @return True iff the spell was registered.
	 */
	public static final boolean registerSpell(AbstractSpell spell) {
		if(SPELLS.containsKey(spell.getID())) {
			return false;
		}
		SPELLS.put(spell.getID(), spell);
		return true;
	}
	
	/**
	 * Get a registered instance of AbstractSpell from a given String ID.
	 * @param id The ID of the spell being searched for.
	 * @return The registered instance of AbstractSpell, or null if there is no spell registered to the ID.
	 */
	public static final AbstractSpell getSpellFromID(String id) {
		return SPELLS.containsKey(id) ? SPELLS.get(id) : null;
	}
	
	public static final boolean isSpellRegistered(AbstractSpell spell) {
		return SPELLS.containsKey(spell.getID());
	}
	
}
