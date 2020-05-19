package azaka7.artimancy.common;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HugeMushroomBlock;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BigMushroomFeatureConfig;

/**
 * Used to store static variables that should not be loaded or referenced until after the game loads.
 * @author Azaka7
 *
 */
public class LateDataStore {
	private static final BlockState STEM_BLOCK = Blocks.MUSHROOM_STEM.getDefaultState().with(HugeMushroomBlock.UP, Boolean.valueOf(false)).with(HugeMushroomBlock.DOWN, Boolean.valueOf(false));
	private static final BlockState WHITE_SHROOM_BLOCK = ModBlocks.instance().white_mushroom_block.getDefaultState();
	
	public static final BigMushroomFeatureConfig WHITE_MUSHROOM_CFG = new BigMushroomFeatureConfig(new SimpleBlockStateProvider(WHITE_SHROOM_BLOCK), new SimpleBlockStateProvider(STEM_BLOCK), 3);
	
}
