package azaka7.artimancy.common.block;

import azaka7.artimancy.Artimancy;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;

public class BlockMiscStairs extends StairsBlock {

	public BlockMiscStairs(String name, java.util.function.Supplier<BlockState> state, Properties prop) {
		super(state,prop);
		this.setRegistryName(Artimancy.MODID+":"+name);
	}

}
