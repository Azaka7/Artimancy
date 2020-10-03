package azaka7.artimancy.common.magic;

import azaka7.artimancy.common.ModBlocks;
import azaka7.artimancy.common.block.LuminBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class LightSpell extends AbstractSpell{
	
	public LightSpell(String id) {
		super(id);
	}

	@Override
	public boolean castSpell(LivingEntity caster, int power, int focus) {
		World world = caster.getEntityWorld();
		
		double range = 20.0D * (focus + 1);
        RayTraceResult blocktarget = caster.isSneaking() ? caster.pick(range, 1.0f, true) : caster.pick(range, 1.0f, false);
        Vector3d pos = null;
        
        if(blocktarget != null && blocktarget.getType() != RayTraceResult.Type.MISS) pos = blocktarget.getHitVec();
		
		if(pos != null) {
			
			BlockPos bpos = new BlockPos(pos.x,pos.y,pos.z);
			int flag = (int) caster.getPositionVec().distanceTo(pos);
			while(!isValidPos(world, bpos, caster) && flag > 0) {
				pos = pos.subtract(caster.getLookVec());
				bpos = new BlockPos(pos.x,pos.y,pos.z);
				flag--;
			}
			if(!isValidPos(world, bpos, caster)) return false;
			
			caster.playSound(SoundEvents.BLOCK_BEACON_ACTIVATE, 0.25F, (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.2F + 2.0F);
			
			world.setBlockState(bpos, ((LuminBlock)ModBlocks.instance().lumin_block).getStateForLevel(power, world, bpos));
			return true;
		}
		
		return false;
	}
	
	private boolean isValidPos(World world, BlockPos pos, LivingEntity caster) {
		if(caster.isSneaking()) {
			return world.getBlockState(pos).getMaterial() == Material.AIR;
		} else {
			return world.getBlockState(pos).getMaterial() == Material.AIR || world.getBlockState(pos).getMaterial() == Material.WATER;
		}
	}

	@Override
	public boolean canCastSpell(LivingEntity caster) {
		return true;
	}

	@Override
	public int baseCost(LivingEntity caster) {
		return 1;
	}

	@Override
	public int getColor() {
		return 0xfff59e;
	}

}
