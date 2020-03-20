package azaka7.artimancy.common.magic;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FireballSpell extends AbstractSpell{
	
	public FireballSpell(String id) {
		super(id);
	}

	@Override
	public boolean castSpell(LivingEntity caster, int power, int luck) {
		World world = caster.getEntityWorld();
		Vec3d vec3d = caster.getLook(1.0F);
		caster.playSound(SoundEvents.ENTITY_BLAZE_AMBIENT, 1.0F, (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.2F + 1.0F);
		
		FireballEntity fireball = new FireballEntity(world, caster, vec3d.getX(), vec3d.getY(), vec3d.getZ());
		fireball.explosionPower = power + Math.round(world.getRandom().nextFloat() * (luck + 1));
		fireball.setPosition(fireball.func_226277_ct_(), caster.func_226283_e_(0.5D) + 0.5D, fireball.func_226281_cx_());
		world.addEntity(fireball);
		return true;
	}

}
