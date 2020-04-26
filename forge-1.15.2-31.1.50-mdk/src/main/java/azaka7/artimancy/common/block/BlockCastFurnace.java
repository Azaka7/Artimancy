package azaka7.artimancy.common.block;

import java.util.Random;
import java.util.function.Consumer;

import azaka7.artimancy.Artimancy;
import azaka7.artimancy.common.tileentity.FurnaceTiming;
import azaka7.artimancy.common.tileentity.CastFurnaceTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockCastFurnace extends ContainerBlock{
	
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty LIT = BooleanProperty.create("lit");
	
	public BlockCastFurnace(String name, Properties prop){
		super(prop);
		this.setRegistryName(Artimancy.MODID+":"+name);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(LIT, false));
	}
	
	@Override
	public ActionResultType func_225533_a_(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if (tileentity instanceof CastFurnaceTileEntity)
        {
			if(player instanceof ServerPlayerEntity)
				NetworkHooks.openGui((ServerPlayerEntity) player,(CastFurnaceTileEntity) tileentity, new Consumer<PacketBuffer>() {

					@Override
					public void accept(PacketBuffer t) {
						t.writeBlockPos(pos);
						FurnaceTiming timing = ((CastFurnaceTileEntity) tileentity).getTiming();
						t.writeInt(timing.getBurnTime());
						t.writeInt(timing.getItemBurnTime());
						t.writeInt(timing.getCookTime());
						t.writeInt(timing.getItemCookTime());
					}
					
				});
        }
		return ActionResultType.SUCCESS;
    }
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING).add(LIT);
	}
	
	public int getLightValue(BlockState state)
    {
		return ((boolean)state.get(LIT)) ? 10 : 0;
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.with(FACING, placer.getHorizontalFacing().getOpposite()), 2);

        if (stack.hasDisplayName())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof CastFurnaceTileEntity)
            {
                ((CastFurnaceTileEntity)tileentity).setCustomInventoryName(stack.getDisplayName().getFormattedText());
            }
        }
    }
    
    @SuppressWarnings("deprecation")
	@Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) 
    {
    	if (state.getBlock() != newState.getBlock()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof CastFurnaceTileEntity) {
               InventoryHelper.dropInventoryItems(worldIn, pos, (CastFurnaceTileEntity)tileentity);
               worldIn.updateComparatorOutputLevel(pos, this);
            }

            super.onReplaced(state, worldIn, pos, newState, isMoving);
         }
    }
    
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
     }
    
    public boolean hasComparatorInputOverride(BlockState state)
    {
        return true;
    }

    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos)
    {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new CastFurnaceTileEntity();
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) 
	{
        if (stateIn.get(LIT))
        {
            Direction enumfacing = (Direction)stateIn.get(FACING);
            double d0 = (double)pos.getX() + 0.5D;
            double d1 = (double)pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
            double d2 = (double)pos.getZ() + 0.5D;
            double d4 = rand.nextDouble() * 0.6D - 0.3D;

            if (rand.nextDouble() < 0.1D)
            {
                worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            switch (enumfacing)
            {
                case WEST:
                    worldIn.addParticle(ParticleTypes.SMOKE, d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                    worldIn.addParticle(ParticleTypes.FLAME, d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                    break;
                case EAST:
                    worldIn.addParticle(ParticleTypes.SMOKE, d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                    worldIn.addParticle(ParticleTypes.FLAME, d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                    break;
                case NORTH:
                    worldIn.addParticle(ParticleTypes.SMOKE, d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
                    worldIn.addParticle(ParticleTypes.FLAME, d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
                    break;
                case SOUTH:
                    worldIn.addParticle(ParticleTypes.SMOKE, d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
                    worldIn.addParticle(ParticleTypes.FLAME, d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
			default:
				break;
            }
        }
    }
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
	      return BlockRenderType.MODEL;
	}
}
