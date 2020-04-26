package azaka7.artimancy.common.magic;

import java.util.HashMap;

import net.minecraft.entity.LivingEntity;

public abstract class AbstractSpell {
	
	private static final HashMap<String, AbstractSpell> SPELLS = new HashMap<String,AbstractSpell>();
	protected final String spellID;
	
	/**
	 * Create an instance of this class that is a (grand) child of AbstractSpell. Call {@link AbstractSpell#registerSpell} to register this instance.
	 * @param id A String ID to represent this spell when registered.
	 */
	protected AbstractSpell(String id) {
		this.spellID = id;
	}
	
	/**
	 * Get the String ID of this spell. The returned value does NOT imply this instance has been registered.
	 * @return The String ID of this spell.
	 */
	public final String getID() { return this.spellID; }
	
	/**
	 * Determines if this instance of AbstractSpell has been registered.
	 * @return True iff this instance is registered.
	 */
	public final boolean isRegistered() {
		return SPELLS.containsKey(this.spellID);
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
	
	/**
	 * Make the spell do what it's supposed to do. Mana/material costs are handled after this method is called.
	 * @param caster The LivingEntity attempting to cast this spell
	 * @param power A linear scaling of how powerful the spell is. What this does is ambiguous and determined by each spell
	 * @param luck A value representing the player's current luck. What this does is ambiguous and determined by each spell
	 * @return true if costs should be handled, i.e. when the spell executes without errors.
	 */
	public abstract boolean castSpell(LivingEntity caster, int power, int luck);

}
