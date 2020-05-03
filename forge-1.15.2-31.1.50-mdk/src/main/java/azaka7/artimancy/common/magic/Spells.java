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
	
	static {
		registerSpell(TELEPORT);
		registerSpell(FIREBALL);
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
