package azaka7.artimancy.common.magic;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class FireballSpell extends AbstractSpell{
	
	public FireballSpell(String id) {
		super(id);
	}

	@Override
	public boolean castSpell(LivingEntity caster, int power, int focus) {
		World world = caster.getEntityWorld();
		Vector3d vec3d = caster.getLook(1.0F);
		caster.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 1.0F, (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.2F + 0.0F);
		
		FireballEntity fireball = new FireballEntity(world, caster, vec3d.getX(), vec3d.getY(), vec3d.getZ());
		fireball.explosionPower = power;
		fireball.setPosition(fireball.getPosX(), caster.getPosYHeight(0.5D) + 0.5D, fireball.getPosZ());
		double aX = vec3d.getX() + caster.world.rand.nextGaussian() * Math.pow(0.2D, 0.5*(focus+3));
		double aY = vec3d.getY() + caster.world.rand.nextGaussian() * Math.pow(0.2D, 0.5*(focus+3));
		double aZ = vec3d.getZ() + caster.world.rand.nextGaussian() * Math.pow(0.2D, 0.5*(focus+3));
		double scale = (double)MathHelper.sqrt(aX*aX + aY*aY + aZ*aZ);
		fireball.accelerationX = aX / scale * 0.1D;
		fireball.accelerationY = aY / scale * 0.1D;
		fireball.accelerationZ = aZ / scale * 0.1D;
		
		world.addEntity(fireball);
		return true;
	}

	@Override
	public boolean canCastSpell(LivingEntity caster) {
		return true;
	}

	@Override
	public int baseCost(LivingEntity caster) {
		return 10;
	}

	@Override
	public int getColor() {
		return 0xcf3213;
	}

}
