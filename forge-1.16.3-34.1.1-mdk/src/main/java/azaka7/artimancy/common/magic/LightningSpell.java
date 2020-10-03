package azaka7.artimancy.common.magic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class LightningSpell extends AbstractSpell{
	
	public LightningSpell(String id) {
		super(id);
	}

	@SuppressWarnings("unused")
	@Override
	public boolean castSpell(LivingEntity caster, int power, int focus) {
		World world = caster.getEntityWorld();
		
		double range = 20.0D * (focus + 1);
		Vector3d eyePos = caster.getEyePosition(1.0f);
		Vector3d lookDir = caster.getLook(1.0F);
        Vector3d farPos = eyePos.add(lookDir.x * range, lookDir.y * range, lookDir.z * range);
        //float f = 1.0F;
        RayTraceResult blocktarget = caster.pick(range, 1.0f, true);
        double newRange = blocktarget != null && blocktarget.getType() != RayTraceResult.Type.MISS ? eyePos.distanceTo(blocktarget.getHitVec()) : range;
        AxisAlignedBB axisalignedbb = caster.getBoundingBox().expand(lookDir.scale(newRange)).grow(1.0D, 1.0D, 1.0D);
        EntityRayTraceResult entitytarget = ProjectileHelper.rayTraceEntities(caster, eyePos, farPos, axisalignedbb, (entity) -> {
           return !entity.isSpectator() && entity.canBeCollidedWith();
        }, newRange);
		
        //caster.pick(range, 1.0f, false);
		Vector3d pos = null;
		if(entitytarget == null || entitytarget.getType() == RayTraceResult.Type.MISS) {
			if(blocktarget == null || blocktarget.getType() == RayTraceResult.Type.MISS) {
				return false;
			}
			pos = blocktarget.getHitVec();
		} else if(blocktarget == null || blocktarget.getType() == RayTraceResult.Type.MISS) {
			pos = entitytarget.getHitVec();
		} else {
			pos = eyePos.distanceTo(blocktarget.getHitVec()) < eyePos.distanceTo(entitytarget.getHitVec()) ? blocktarget.getHitVec() : entitytarget.getHitVec();
		}
		if(pos != null) {
			caster.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.2F + 1.5F);
			
			final double x = pos.x, y = pos.y, z = pos.z;
			java.util.Timer timer = new java.util.Timer();
			timer.schedule(new java.util.TimerTask() {
				@Override
				public void run() {
					LightningBoltEntity lightning = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, world);
					lightning.setPosition(x, y, z);
					lightning.func_233623_a_(true);
					if(caster instanceof ServerPlayerEntity) lightning.setCaster((ServerPlayerEntity) caster);
					world.addEntity(lightning);
					
					for(Entity entity : world.getEntitiesInAABBexcluding(caster, new AxisAlignedBB(new BlockPos(x-1,y-1,z-1), new BlockPos(x+1,y+2,z+1)), e -> !e.isSpectator())) {
						entity.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 4.0f * power);
					}
					
				}}, 333);
			
			return true;
		}
		
		return false;
	}

	@Override
	public boolean canCastSpell(LivingEntity caster) {
		return true;
	}

	@Override
	public int baseCost(LivingEntity caster) {
		return 30;
	}

	@Override
	public int getColor() {
		return 0x8df9ff;
	}

}
