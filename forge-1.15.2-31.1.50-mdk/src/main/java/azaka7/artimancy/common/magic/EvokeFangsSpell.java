package azaka7.artimancy.common.magic;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

public class EvokeFangsSpell extends AbstractSpell{
	
	public EvokeFangsSpell(String id) {
		super(id);
	}

	@Override
	public boolean castSpell(LivingEntity caster, int power, int focus) {
		World world = caster.getEntityWorld();
		
		boolean flag = false;
		for(int l = 0; l < 8 + (2*power); ++l) {
            double d2 = 1.25D * (double)(l + 1);
            int delay = (int) (4 * l / (focus+1 == 0 ? 0.0001 : focus+1));
            float angle = (float) ((caster.getRotationYawHead()+90.0f) * Math.PI / 180.0f);
            boolean flag2 = this.spawnFangs(world, caster, caster.getPosX() + (double)MathHelper.cos(angle) * d2, caster.getPosZ() + (double)MathHelper.sin(angle) * d2, (double)caster.getPosY(), caster.getPosY()+1.0D, angle, delay);
            flag = flag || flag2;
		}
		if(flag) caster.playSound(SoundEvents.ENTITY_EVOKER_CAST_SPELL, 1.0F, (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.2F + 0.0F);
		
		return flag;
	}
	
	private boolean spawnFangs(World world, LivingEntity caster, double x, double z, double yMin, double y, float rotation, int delay) {
        BlockPos blockpos = new BlockPos(x, y, z);
        boolean flag = false;
        double d0 = 0.0D;

        while(true) {
           BlockPos blockpos1 = blockpos.down();
           BlockState blockstate = world.getBlockState(blockpos1);
           if (blockstate.isSolidSide(world, blockpos1, Direction.UP)) {
              if (!world.isAirBlock(blockpos)) {
                 BlockState blockstate1 = world.getBlockState(blockpos);
                 VoxelShape voxelshape = blockstate1.getCollisionShape(world, blockpos);
                 if (!voxelshape.isEmpty()) {
                    d0 = voxelshape.getEnd(Direction.Axis.Y);
                 }
              }

              flag = true;
              break;
           }

           blockpos = blockpos.down();
           if (blockpos.getY() < MathHelper.floor(yMin) - 1) {
              break;
           }
        }

        if (flag) {
        	EvokerFangsEntity fangs = new EvokerFangsEntity(world, x, (double)blockpos.getY() + d0, z, rotation, delay, caster);
        	world.addEntity(fangs);
        }
        
        return flag;
     }

	@Override
	public boolean canCastSpell(LivingEntity caster) {
		return true;
	}

	@Override
	public int baseCost(LivingEntity caster) {
		return 16;
	}

	@Override
	public int getColor() {
		return 0xaaaaaa;
	}

}
