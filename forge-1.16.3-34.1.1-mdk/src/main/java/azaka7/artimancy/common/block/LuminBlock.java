package azaka7.artimancy.common.block;

import java.util.Random;

import azaka7.artimancy.Artimancy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class LuminBlock extends Block implements IWaterLoggable{
	
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	private static final IntegerProperty LIGHT = IntegerProperty.create("artimancy_light_tier", 0, 5);
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D);
	
	public LuminBlock(String name, Block.Properties props) {
		super(props.func_235838_a_((state -> {return 10 + state.get(LIGHT).intValue();})).notSolid().doesNotBlockMovement());
		this.setRegistryName(Artimancy.MODID+":"+name);
		this.setDefaultState(this.stateContainer.getBaseState().with(LIGHT, 0).with(WATERLOGGED, false));
	}
	
	public BlockState getStateForLevel(int level, World world, BlockPos pos) {
		FluidState ifluidstate = world.getFluidState(pos);
		return this.getDefaultState().with(LIGHT, (level >= 0 && level <6) ? level : 0).with(WATERLOGGED, Boolean.valueOf(ifluidstate.getFluid() == Fluids.WATER));
	}
	
	@Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockPos blockpos = context.getPos();
		FluidState ifluidstate = context.getWorld().getFluidState(blockpos);
		return this.getDefaultState().with(LIGHT, 0).with(WATERLOGGED, Boolean.valueOf(ifluidstate.getFluid() == Fluids.WATER));
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(LIGHT).add(WATERLOGGED);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}
		return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}
	
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
	
	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
		return true;
	}
	
	@OnlyIn(Dist.CLIENT)
	   public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
	      
	      double x = (double)pos.getX() + 0.55D - (double)(rand.nextFloat() * 0.1F);
	      double y = (double)pos.getY() + 0.55D - (double)(rand.nextFloat() * 0.1F) + 0.2;
	      double d2 = (double)pos.getZ() + 0.55D - (double)(rand.nextFloat() * 0.1F);
	      if (rand.nextInt(10) == 0) {
	         worldIn.addParticle(ParticleTypes.END_ROD,x,y,d2,rand.nextGaussian() * 0.005D, Math.abs(rand.nextGaussian()) * 0.020D, rand.nextGaussian() * 0.005D);
	      }

	   }

}
