package azaka7.artimancy.common.magic;

import net.minecraft.entity.LivingEntity;

public abstract class AbstractSpell {
	
	
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
	 * Determine if the entity is able to cast the spell.
	 * @param caster The LivingEntity attempting to cast this spell
	 * @return true if the spell should be cast
	 */
	public abstract boolean canCastSpell(LivingEntity caster);
	
	/**
	 * Make the spell do what it's supposed to do. Mana/material costs are handled after this method is called.
	 * @param caster The LivingEntity attempting to cast this spell
	 * @param power A linear scaling of how powerful the spell is. What this does is ambiguous and determined by each spell
	 * @param focus A value representing the player's current luck. What this does is ambiguous and determined by each spell
	 * @return true if costs should be handled, i.e. when the spell executes without errors.
	 */
	public abstract boolean castSpell(LivingEntity caster, int power, int focus);
	
	/**
	 * The basic amount of mana it costs to cast this spell. Enchantments are applied after this method.
	 * @param caster The LivingEntity attempting to cast this spell
	 * @return the basic mana cost for casting this spell
	 */
	public abstract int baseCost(LivingEntity caster);

	/**
	 * A staff with a spell will have its focus colored depending on the spell stored.
	 * @return The color of the focus when this spell is store in a staff
	 */
	public abstract int getColor();

}
