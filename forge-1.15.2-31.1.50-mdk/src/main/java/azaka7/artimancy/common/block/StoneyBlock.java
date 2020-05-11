package azaka7.artimancy.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StoneyBlock extends MiscBlock {
	
	private final Block dropped;

	public StoneyBlock(String name, Block drop, Properties props) {
		super(name, props);
		dropped = drop;
	}
	
	public IItemProvider getItemDropped(BlockState state, World worldIn, BlockPos pos, int fortune) {
		return dropped;
	}
	
}
