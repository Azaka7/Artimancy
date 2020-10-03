package azaka7.artimancy.common.magic;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class MiningSpell extends AbstractSpell{
	
	public MiningSpell(String id) {
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
			int flag = (int) range*2;
			while(!isValidPos(world, bpos, caster) && flag > 0) {
				pos = pos.add(caster.getLookVec().mul(0.25, 0.25, 0.25));
				bpos = new BlockPos(pos.x,pos.y,pos.z);
				flag--;
			}
			if(!isValidPos(world, bpos, caster)) return false;
			
			Vector3d looking = caster.getLookVec();
			boolean worked = false;
			for(BlockPos targ : getTargetPositions(power, bpos, Direction.getFacingFromVector(looking.getX(), 0, looking.getZ()))) {
				BlockState state = world.getBlockState(targ);
				if(state.getBlockHardness(world, targ) < 0) {continue;}
				ItemStack stack = caster.getHeldItem(caster.getActiveHand());
				int lvl = stack.getItem().getHarvestLevel(stack, null, caster instanceof PlayerEntity ? (PlayerEntity)caster : null, state);
				if(lvl < state.getHarvestLevel()) {continue;}
				
				worked = true;
				if(world.isRemote) {break;}//Prevents client-side rendering wrong blocks broken
				TileEntity tile = world.getTileEntity(targ);
				int xp = state.getExpDrop(world, bpos, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack), EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack));
				world.setBlockState(targ, Blocks.AIR.getDefaultState());
				world.destroyBlock(targ, false);
				Block.spawnDrops(state, world, targ, tile, caster, stack);
				state.getBlock().onPlayerDestroy(world, targ, state);
				if(caster instanceof PlayerEntity) {
					((PlayerEntity) caster).giveExperiencePoints(xp);
				}
			}
			
			if(worked) {
				caster.playSound(SoundEvents.ENTITY_GUARDIAN_ATTACK, 1.0F, (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.2F + 2.0F);
				Vector3d eyePos = caster.getEyePosition(1.0f);
				double progress = 0;
				while (progress <= eyePos.distanceTo(pos)) {
					progress += 0.5*looking.length();
					world.addParticle(ParticleTypes.ENCHANT, true, eyePos.x + (progress*looking.x), eyePos.y + (progress*looking.y), eyePos.z + (progress*looking.z), looking.x, looking.y, looking.z);
				}
			}
			
			return worked;
		}
		
		return false;
	}
	
	private boolean isValidPos(World world, BlockPos pos, LivingEntity caster) {
		return world.getBlockState(pos).getMaterial() != Material.AIR && world.getBlockState(pos).getMaterial() != Material.WATER;
	}
	
	private ArrayList<BlockPos> getTargetPositions(int power, BlockPos center, Direction dir){
		ArrayList<BlockPos> targets = new java.util.ArrayList<BlockPos>();
		if(dir == Direction.UP || dir == Direction.DOWN) {return targets;}
		switch(power) {
		case 5:{
			targets.add(center.add(1, 2, 1));
			targets.add(center.add(1, 2, -1));
			targets.add(center.add(-1, 2, 1));
			targets.add(center.add(-1, 2, -1));
			targets.add(center.add(1, 2, 0));
			targets.add(center.add(0, 2, 1));
			targets.add(center.add(-1, 2, 0));
			targets.add(center.add(0, 2, -1));
			targets.add(center.add(0, 2, 0));
		}
		case 4:{
			targets.add(center.add(0, 0, 0));
			targets.add(center.add(0, 0, 1));
			targets.add(center.add(0, 1, 0));
			targets.add(center.add(0, 1, 1));
			targets.add(center.add(0, 1, -1));
			targets.add(center.add(0, -1, 1));
			targets.add(center.add(0, -1, -1));
			targets.add(center.add(0, 0, -1));
			targets.add(center.add(0, -1, 0));

			targets.add(center.add(1, 0, 0));
			targets.add(center.add(1, 0, 1));
			targets.add(center.add(1, 1, 0));
			targets.add(center.add(1, 1, 1));
			targets.add(center.add(1, 1, -1));
			targets.add(center.add(1, -1, 1));
			targets.add(center.add(1, -1, -1));
			targets.add(center.add(1, 0, -1));
			targets.add(center.add(1, -1, 0));

			targets.add(center.add(-1, 0, 0));
			targets.add(center.add(-1, 0, 1));
			targets.add(center.add(-1, 1, 0));
			targets.add(center.add(-1, 1, 1));
			targets.add(center.add(-1, 1, -1));
			targets.add(center.add(-1, -1, 1));
			targets.add(center.add(-1, -1, -1));
			targets.add(center.add(-1, 0, -1));
			targets.add(center.add(-1, -1, 0));
			break;
		}
		case 3:{
			targets.add((isEastOrWest(dir) ? center.north() : center.east()).up());//.rotate(isEastOrWest(dir) ? Rotation.CLOCKWISE_90 : Rotation.NONE));
			targets.add((isEastOrWest(dir) ? center.south() : center.west()).up());//.rotate(isEastOrWest(dir) ? Rotation.CLOCKWISE_90 : Rotation.NONE));
			targets.add((isEastOrWest(dir) ? center.north() : center.east()).down());//.rotate(isEastOrWest(dir) ? Rotation.CLOCKWISE_90 : Rotation.NONE));
			targets.add((isEastOrWest(dir) ? center.south() : center.west()).down());//.rotate(isEastOrWest(dir) ? Rotation.CLOCKWISE_90 : Rotation.NONE));
		}
		case 2:{
			targets.add(isEastOrWest(dir) ? center.north() : center.east());//.rotate(isEastOrWest(dir) ? Rotation.CLOCKWISE_90 : Rotation.NONE));
			targets.add(isEastOrWest(dir) ? center.south() : center.west());//.rotate(isEastOrWest(dir) ? Rotation.CLOCKWISE_90 : Rotation.NONE));
		}
		case 1:{
			targets.add(center.down());
			targets.add(center.up());
		}
		default: {targets.add(center); break;}
		}
		return targets;
	}
	
	private boolean isEastOrWest(Direction dir) {
		return dir == Direction.EAST || dir == Direction.WEST;
	}

	@Override
	public boolean canCastSpell(LivingEntity caster) {
		return true;
	}

	@Override
	public int baseCost(LivingEntity caster) {
		return 8;
	}

	@Override
	public int getColor() {
		return 0x14dc2c;
	}

}
