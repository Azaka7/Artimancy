package azaka7.artimancy.common.block;

import java.util.Random;

import azaka7.artimancy.Artimancy;
import azaka7.artimancy.common.LateDataStore;
import azaka7.artimancy.common.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.MushroomBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.BigMushroomFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.server.ServerWorld;

public class MushroomyBlock extends MushroomBlock{
	
	public MushroomyBlock(String name, Properties properties) {
		super(properties);
		this.setRegistryName(Artimancy.MODID+":"+name);
	}
	
	@Override
	public boolean func_226940_a_(ServerWorld serverWorld, BlockPos pos, BlockState blockState, Random random) {
		serverWorld.removeBlock(pos, false);
		ConfiguredFeature<BigMushroomFeatureConfig, ?> configuredfeature;
		if (this == ModBlocks.instance().white_mushroom) {
			configuredfeature = Feature.HUGE_BROWN_MUSHROOM.withConfiguration(LateDataStore.WHITE_MUSHROOM_CFG);
		} else {
			serverWorld.setBlockState(pos, blockState, 3);
			return false;
		}
		
		if (configuredfeature.func_242765_a(serverWorld, serverWorld.getChunkProvider().getChunkGenerator(), random, pos)) {
			return true;
		} else {
			serverWorld.setBlockState(pos, blockState, 3);
			return false;
		}
	}
}
