package azaka7.artimancy.common.magic;

import java.util.List;

import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class FirebreathSpell extends AbstractSpell{
	
	public FirebreathSpell(String id) {
		super(id);
	}

	@Override
	public boolean castSpell(LivingEntity caster, int power, int focus) {
		World world = caster.getEntityWorld();
		int range = 5 + focus;
		int radius = 3 + focus;
		Vector3d eyePos = caster.getEyePosition(1.0f);
		Vector3d lookDir = caster.getLook(1.0F);
		
		RayTraceResult blocktarget = caster.pick(range, 1.0f, true);
        Vector3d pos = null;
        
        if(blocktarget != null && blocktarget.getType() != RayTraceResult.Type.MISS) pos = blocktarget.getHitVec();
		
		if(pos != null) {
			for(int x = -1; x <= 1; x++) {
				for(int y = -1; y <= 1; y++) {
					for(int z = -1; z <= 1; z++) {
						BlockPos bpos = new BlockPos(pos.x + x, pos.y + y, pos.z + z);
						if(world.getBlockState(bpos).getBlock().equals(Blocks.AIR)) {
							world.setBlockState(bpos, FireBlock.getValidBlockForPosition(Blocks.FIRE.getDefaultState(), world, bpos));
							
						}
					}
				}
			}
		}
		
		range = blocktarget.getType() == Type.MISS ? range : Math.round((float) eyePos.distanceTo(blocktarget.getHitVec()));
        AxisAlignedBB aabb = caster.getBoundingBox().expand(lookDir.scale(range)).grow(radius, radius, radius);
       
        List<Entity> entities = world.getEntitiesInAABBexcluding(caster, aabb, (entity) -> {
            return !entity.isSpectator() && entity.canBeCollidedWith();
         });
        
        for(Entity entity : entities) {
        	AxisAlignedBB entityBox = entity.getBoundingBox();
        	Vector3d entityCenter = entityBox.getCenter();
        	double dist = eyePos.distanceTo(entityCenter);
        	
        	if(dist + entityBox.getAverageEdgeLength()/2.0 > range) continue;
        	
        	Vector3d closestOnLOS = eyePos.add(lookDir.scale(dist));//Closest position to the entity on the player's line of sight
        	double partialRadius = radius * dist / range;
        	if(closestOnLOS.distanceTo(entityCenter) <= partialRadius + (entityBox.getAverageEdgeLength()/2.0)) {
        		entity.setFire(5);
        		entity.attackEntityFrom(DamageSource.causeMobDamage(caster), 1.0f + power);
        	}
        }
        
        double progress = 0;
		while (progress <= range) {
			progress += 0.5*lookDir.length();
			if(world.rand.nextBoolean())
				world.addParticle(ParticleTypes.FLAME, true, eyePos.x + (progress*lookDir.x), eyePos.y + (progress*lookDir.y) - 0.2, eyePos.z + (progress*lookDir.z), 0, 0, 0);
		}
		Vector3d unit_a = lookDir.normalize();
		Vector3d unit_b = unit_a.crossProduct(new Vector3d(0,1,0));
		Vector3d unit_c = unit_a.crossProduct(unit_b);
		world.addParticle(ParticleTypes.FLASH, true, eyePos.x + lookDir.x, eyePos.y + lookDir.y, eyePos.z + lookDir.z, 0, 0, 0);
		for(int flames = 0; flames < 20 * (power+1); flames++) {
			Vector3d motion = unit_a.scale(1.0+ 0.5*world.rand.nextDouble());
			motion = motion.add(unit_b.scale(world.rand.nextDouble()*range/(radius*2))).add(unit_b.scale(-world.rand.nextDouble()*range/(radius*2)));
			motion = motion.add(unit_c.scale(world.rand.nextDouble()*range/(radius*2))).add(unit_c.scale(-world.rand.nextDouble()*range/(radius*2)));
			motion = motion.scale(0.3D + 0.025D*(focus+1));
			world.addParticle(ParticleTypes.FLAME, true, eyePos.x + lookDir.x/2.0, eyePos.y + lookDir.y/2.0 - 0.2, eyePos.z + lookDir.z/2.0, motion.x, motion.y, motion.z);
		}
		
		caster.playSound(SoundEvents.ENTITY_BLAZE_AMBIENT, 1.0F, (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.2F + 2.5F);
        
		return true;
	}

	@Override
	public boolean canCastSpell(LivingEntity caster) {
		return true;
	}

	@Override
	public int baseCost(LivingEntity caster) {
		return 11;
	}

	@Override
	public int getColor() {
		return 0xC08800;
	}

}
