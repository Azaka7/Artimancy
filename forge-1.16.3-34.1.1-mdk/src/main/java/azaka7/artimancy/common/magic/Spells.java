package azaka7.artimancy.common.magic;

import java.util.HashMap;

import javax.annotation.Nullable;

import azaka7.artimancy.Artimancy;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class Spells {
	
	private static final HashMap<String, AbstractSpell> SPELLS = new HashMap<String,AbstractSpell>();
	private static final String SPELL_NBT = Artimancy.MODID+".spell";
	private static final String NULL_ID = "$NULL";

	public static final AbstractSpell TELEPORT = new TeleportSpell("artimancy:teleport");
	public static final AbstractSpell FIREBALL = new FireballSpell("artimancy:fireball");
	public static final AbstractSpell LIGHTNING = new LightningSpell("artimancy:lightning");
	public static final AbstractSpell HEALING = new HealingSpell("artimancy:healing");
	public static final AbstractSpell LIGHT = new LightSpell("artimancy:light");
	public static final AbstractSpell MINING = new MiningSpell("artimancy:mining");
	public static final AbstractSpell LEVITATE = new LevitateSpell("artimancy:levitate");
	public static final AbstractSpell FREEZE = new FreezeSpell("artimancy:freeze");
	public static final AbstractSpell EVOKE_FANGS = new EvokeFangsSpell("artimancy:evoke_fangs");
	public static final AbstractSpell MAGIC_MISSILE = new MagicMissileSpell("artimancy:magic_missile");
	public static final AbstractSpell FIREBREATH = new FirebreathSpell("artimancy:firebreath");
	public static final AbstractSpell SHOCKWAVE = new ShockwaveSpell("artimancy:shockwave");
	
	static {
		registerSpell(TELEPORT);
		registerSpell(FIREBALL);
		registerSpell(LIGHTNING);
		registerSpell(HEALING);
		registerSpell(LIGHT);
		registerSpell(MINING);
		registerSpell(LEVITATE);
		registerSpell(FREEZE);
		registerSpell(EVOKE_FANGS);
		registerSpell(MAGIC_MISSILE);
		registerSpell(FIREBREATH);
		registerSpell(SHOCKWAVE);
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
		if(spell == null || NULL_ID.equals(spell.getID()) || SPELLS.containsKey(spell.getID())) {
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
		if(NULL_ID.equals(id)) {return null;}
		return SPELLS.containsKey(id) ? SPELLS.get(id) : null;
	}
	
	/**
	 * Safely check if the AbstractSpell has been registered.
	 * @param spell The AbstractSpell to check for.
	 * @return True if the spell has been registered, or if the spell's ID represents a null spell (to help prevent spells from trying to be registered under that ID)
	 */
	public static final boolean isSpellRegistered(AbstractSpell spell) {
		return spell == null || SPELLS.containsKey(spell.getID());
	}
	
	/**
	 * Safely obtain the ID of a spell. The AbstractSpell may be null.
	 * @param spell The AbstractSpell to get the ID of.
	 * @return Spells.NULL_ID if null, otherwise spell.getID()
	 */
	public static String getSpellID(@Nullable AbstractSpell spell) {
		return spell == null ? NULL_ID : spell.getID();
	}
	
}
