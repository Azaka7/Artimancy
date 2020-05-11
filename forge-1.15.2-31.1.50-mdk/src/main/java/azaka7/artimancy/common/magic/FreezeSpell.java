package azaka7.artimancy.common.magic;

import azaka7.artimancy.common.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Potions;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class FreezeSpell extends AbstractSpell{
	
	public FreezeSpell(String id) {
		super(id);
	}

	@Override
	public boolean castSpell(LivingEntity caster, int power, int focus) {
		World world = caster.getEntityWorld();
		
		double range = 10.0D * (focus + 1);
        RayTraceResult blocktarget = caster.pick(range, 1.0f, true);
        Vec3d pos = null;
        
        if(blocktarget != null && blocktarget.getType() != RayTraceResult.Type.MISS) pos = blocktarget.getHitVec();
		
		if(pos != null) {
			caster.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, 0.5F, (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.2F + 2.0F);
			
			if(!world.isRemote()) {
				float radius = 2 + power;
				for(int x = (int) -radius; x <= radius ; x++) {
					for(int z = (int) -radius; z <= radius ; z++) {
						if(Math.sqrt(x*x + z*z) <= radius) {
							for(int y = -2; y <= 1; y++) {
								BlockPos curPos = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
								BlockState state = world.getBlockState(curPos);
								if(state.getBlock() == Blocks.WATER && state.get(FlowingFluidBlock.LEVEL) == 0) {
									world.setBlockState(curPos, (Math.sqrt(x*x+z*z)+2 < radius && y > -2) ? Blocks.ICE.getDefaultState() : Blocks.FROSTED_ICE.getDefaultState());
								} else if(state.getBlock() == Blocks.LAVA && state.get(FlowingFluidBlock.LEVEL) == 0) {
									world.setBlockState(curPos, (Math.sqrt(x*x+z*z)+2 < radius && y > -2) ? Blocks.OBSIDIAN.getDefaultState() : Blocks.MAGMA_BLOCK.getDefaultState());
								} else if(state.getBlock() == Blocks.LAVA) {
									world.setBlockState(curPos, ModBlocks.instance().basalt.getDefaultState());
								} else if(state.getBlock() == Blocks.MAGMA_BLOCK) {
									world.setBlockState(curPos, Blocks.OBSIDIAN.getDefaultState());
								} else if(state.getBlock() == Blocks.AIR && world.getBlockState(curPos.down()).isTopSolid(world, curPos.down(), caster) && world.getDimension().getType() != DimensionType.THE_NETHER) {
									world.setBlockState(curPos, Blocks.SNOW.getDefaultState());
								} else if(state.getBlock() == Blocks.FIRE) {
									world.setBlockState(curPos, Blocks.AIR.getDefaultState());
								} 
								
							}
						}
					}
				}
			}
			
			AreaEffectCloudEntity aoe = new AreaEffectCloudEntity(world, pos.getX(), pos.getY(), pos.getZ());
			aoe.setOwner(caster);
		    aoe.setRadius(2.0F + power + (world.getDimension().getType() != DimensionType.THE_NETHER ? 0.0f : -1.0f));
		    aoe.setRadiusOnUse(-0.5F);
		    aoe.setWaitTime(10);
		    aoe.setRadiusPerTick(-aoe.getRadius() / 30);
		    aoe.setPotion(Potions.STRONG_SLOWNESS);
		    
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
		return 9;
	}

	@Override
	public int getColor() {
		return 0xa5e2f3;
	}

}
