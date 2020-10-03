package azaka7.artimancy.common.magic;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Potions;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class HealingSpell extends AbstractSpell{
	
	public HealingSpell(String id) {
		super(id);
	}

	@Override
	public boolean castSpell(LivingEntity caster, int power, int focus) {
		World world = caster.getEntityWorld();
		
		double range = 10.0D * (focus + 1);
        RayTraceResult blocktarget = caster.pick(range, 1.0f, true);
        Vector3d pos = null;
        
        if(blocktarget != null && blocktarget.getType() != RayTraceResult.Type.MISS) pos = blocktarget.getHitVec();
		
		if(pos != null) {
			caster.playSound(SoundEvents.BLOCK_BELL_RESONATE, 0.8F, (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.2F + 1.5F);
			
			AreaEffectCloudEntity aoe = new AreaEffectCloudEntity(world, pos.getX(), pos.getY(), pos.getZ());
			aoe.setOwner(caster);
		    aoe.setRadius(3.0F);
		    aoe.setRadiusOnUse(-0.5F/(power+1));
		    aoe.setWaitTime(10);
		    aoe.setRadiusPerTick(-aoe.getRadius() / (30*(power+1)));
		    aoe.setPotion(Potions.STRONG_REGENERATION);
		    
			world.addEntity(aoe);
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
		return 15;
	}

	@Override
	public int getColor() {
		return 0xf699cd;
	}

}
