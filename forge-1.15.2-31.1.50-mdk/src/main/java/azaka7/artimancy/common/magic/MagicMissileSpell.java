package azaka7.artimancy.common.magic;

import azaka7.artimancy.common.entity.projectile.MagicMissileEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class MagicMissileSpell extends AbstractSpell {

	public MagicMissileSpell(String id) {
		super(id);
	}

	@Override
	public boolean canCastSpell(LivingEntity caster) {
		return true;
	}

	@Override
	public boolean castSpell(LivingEntity caster, int power, int focus) {
		
		World world = caster.getEntityWorld();
		caster.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.2F + 0.0F);
		
		MagicMissileEntity bolt = new MagicMissileEntity(caster, world);
		bolt.shoot(caster, caster.rotationPitch, caster.rotationYaw, 0.0F, (float)Math.pow(1.2F, 1+power), (float) Math.pow(20.0F, 1 - Math.sqrt(0.2F+ 0.5f*focus)));
		bolt.setDamage((power)+3);
		world.addEntity(bolt);
		
		return true;
	}

	@Override
	public int baseCost(LivingEntity caster) {
		return 9;
	}

	@Override
	public int getColor() {
		return 0xDF30DF;
	}

}
