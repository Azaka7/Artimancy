package azaka7.artimancy.common.magic;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;

public class ShockwaveSpell extends AbstractSpell{
	
	public ShockwaveSpell(String id) {
		super(id);
	}

	@Override
	public boolean castSpell(LivingEntity caster, int power, int focus) {
		World world = caster.getEntityWorld();
		
		double range = 20.0D * (focus + 1);
		RayTraceResult blocktarget = caster.isShiftKeyDown() ? caster.pick(range, 1.0f, true) : caster.pick(range, 1.0f, false);
        Vec3d pos = null;

        if(blocktarget != null && blocktarget.getType() != RayTraceResult.Type.MISS) pos = blocktarget.getHitVec();
		if(pos != null) {

			BlockPos bpos = new BlockPos(pos.x,pos.y,pos.z);
			if(!isValidPos(world, bpos.down())) return false;
			
			boolean worked = false;
			for(int x = -2; x <= 2; x++) {
				for(int z = -2; z <= 2; z++) {
					if(x*x + z*z <= 4.1) {
						BlockPos here = bpos.add(x, -1, z);
						BlockState state = world.getBlockState(here);
						if(isValidPos(world,here)) {
							ItemStack held = caster.getHeldItem(caster.getActiveHand());
							if(world instanceof ServerWorld && caster instanceof PlayerEntity && state.getHarvestLevel() <= held.getHarvestLevel(null, (PlayerEntity) caster, state)) {
								
								LootContext.Builder looter = (new LootContext.Builder((ServerWorld)world)).withRandom(world.rand).withParameter(LootParameters.POSITION, here).withParameter(LootParameters.TOOL, ItemStack.EMPTY).withNullableParameter(LootParameters.BLOCK_ENTITY, world.getTileEntity(here)).withNullableParameter(LootParameters.THIS_ENTITY, caster);
								BlockState stateToLaunch = state;
								
								List<ItemStack> drops = state.getDrops(looter);
								for(ItemStack stack : drops) {
									Block block = Block.getBlockFromItem(stack.getItem());
									if(block != null && block != Blocks.AIR && block != state.getBlock()) {
										stateToLaunch = block.getDefaultState();
										world.setBlockState(here, stateToLaunch);
									}
								}
								
								FallingBlockEntity falling = new FallingBlockEntity(world, here.getX() + 0.5D, here.getY(), here.getZ() + 0.5D, stateToLaunch);
								falling.setMotion(0, (0.6+(0.2 * (power)))/Math.sqrt((x*x + z*z)+1) + 0.2*world.getRandom().nextDouble(), 0);
								world.addEntity(falling);
								worked = true;
							} else if(state.getHarvestLevel() <= held.getHarvestLevel(null, (PlayerEntity) caster, state)){
								worked = true;
							}
						}
					}
				}
			}
			
			if(worked) {
				for(Entity e : world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.subtract(3, 1, 3), pos.add(3, 2, 3)))) {
					double dist = (new Vec3d(e.getPosX(), pos.getY(), e.getPosZ())).distanceTo(pos);
					if(dist <= 2.5D && !(e instanceof FallingBlockEntity) ) {
						Vec3d mot = e.getMotion();
						e.setMotion(mot.x, mot.y+((0.6+(0.25 * (power)))/Math.sqrt(dist+1)), mot.z);
						e.fallDistance = 0;
					}
				}
				
				caster.playSound(SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.0F, (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.2F + 1.5F);
			}
			
			return worked;
		}
		
		return false;
	}
	
	private boolean isValidPos(World world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		return state.isSolid() && state.getBlockHardness(world, pos) >= 0 && state.getMaterial().getPushReaction() != PushReaction.BLOCK && world.getBlockState(pos.up()).getMaterial().isReplaceable() && world.getTileEntity(pos) == null;
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
		return 0x624c2c;
	}

}
