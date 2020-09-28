package azaka7.artimancy.common.block;

import java.util.function.Consumer;

import azaka7.artimancy.Artimancy;
import azaka7.artimancy.common.tileentity.ArtimancyTableContainer;
import azaka7.artimancy.common.tileentity.ArtimancyTableTE;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class ArtimancyTableBlock extends ContainerBlock {
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	private static final TranslationTextComponent CONTAINER_NAME = new TranslationTextComponent("artimancy.container.artimancy_table");
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);
	
	public ArtimancyTableBlock(String name, Block.Properties properties) {
		super(properties);
		this.setRegistryName(Artimancy.MODID, name);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
	
	@Override
   public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		TileEntity tileentity = worldIn.getTileEntity(pos);
 		if (tileentity instanceof ArtimancyTableTE)
         {
 			if(player instanceof ServerPlayerEntity) {
 				NetworkHooks.openGui((ServerPlayerEntity) player,(ArtimancyTableTE) tileentity, new Consumer<PacketBuffer>() {

 				@Override
 				public void accept(PacketBuffer t) {
 					t.writeBlockPos(pos);
 				}
 				
 				});
 				
 			}
        }
 		return ActionResultType.SUCCESS;
      
   }
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
		//worldIn.setBlockState(pos, state.with(FACING, placer.isSneaking() ? placer.getHorizontalFacing().getOpposite() : placer.getHorizontalFacing()), 2);
		
        if (stack.hasDisplayName())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof ArtimancyTableTE)
            {
            	((ArtimancyTableTE)tileentity).setCustomName(new StringTextComponent(stack.getDisplayName().getFormattedText()));
            }
        }
	}
	
	@SuppressWarnings("deprecation")
	@Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) 
    {
    	if (state.getBlock() != newState.getBlock()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof ArtimancyTableTE) {
               //InventoryHelper.dropInventoryItems(worldIn, pos, (ArtimancyTableTileEntity)tileentity);
               worldIn.updateComparatorOutputLevel(pos, this);
            }

            super.onReplaced(state, worldIn, pos, newState, isMoving);
         }
    }

	public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
	   return new SimpleNamedContainerProvider((p_220254_2_, p_220254_3_, p_220254_4_) -> {
    	  return new ArtimancyTableContainer(p_220254_2_, p_220254_3_, IWorldPosCallable.of(worldIn, pos));
      }, CONTAINER_NAME);
	}

   /*public BlockState getStateForPlacement(BlockItemUseContext context) {
      return this.getDefaultState().with(FACING, context.getPlayer().isSneaking() ? context.getPlacementHorizontalFacing().getOpposite() : context.getPlacementHorizontalFacing());
   }*/
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
	   builder.add(FACING);
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new ArtimancyTableTE();
	}
   
	@Override
	public BlockRenderType getRenderType(BlockState state) {
	      return BlockRenderType.MODEL;
	}
	
	@Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlayer().isSneaking() ? context.getPlacementHorizontalFacing().getOpposite() : context.getPlacementHorizontalFacing());
        
	}
}