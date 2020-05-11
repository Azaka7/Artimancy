package azaka7.artimancy.common.magic;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class LevitateSpell extends AbstractSpell{
	
	public LevitateSpell(String id) {
		super(id);
	}

	@Override
	public boolean castSpell(LivingEntity caster, int power, int focus) {
		World world = caster.getEntityWorld();
		
		Vec3d eyePos = caster.getEyePosition(1.0f);
		Vec3d lookDir = caster.getLook(1.0F);
		
		caster.playSound(SoundEvents.ENTITY_SHULKER_SHOOT, 1.0F, (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.2F + 1.0F);
			
		ShulkerBulletEntity bullet = new ShulkerBulletEntity(world, caster, null, Direction.getFacingFromVector(lookDir.getX(), lookDir.getY(), lookDir.getZ()).getAxis());
		float speed = (float) (1.0f*Math.sqrt(power + 1));
		float skewX = (float) ((world.getRandom().nextGaussian()-world.getRandom().nextGaussian())*0.1 / (focus + 2));
		float skewY = (float) ((world.getRandom().nextDouble() * 0.05) / (focus + 1));
		float skewZ = (float) ((world.getRandom().nextGaussian()-world.getRandom().nextGaussian())*0.1 / (focus+ 2));
		bullet.setRawPosition(eyePos.x + lookDir.x, eyePos.y + lookDir.y, eyePos.z + lookDir.z);
		bullet.setMotion(lookDir.x*speed+skewX, lookDir.y*speed + skewY, lookDir.z*speed + skewZ);
		bullet.setSilent(true);
		bullet.setNoGravity(false);
		bullet.setCustomName(new net.minecraft.util.text.StringTextComponent("artimancy.levitate_spell"));
		world.addEntity(bullet);
		
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
		return 0xA993b5;
	}

}
