package azaka7.artimancy.common.block;

import azaka7.artimancy.Artimancy;
import net.minecraft.block.RotatedPillarBlock;

public class BlockRotated extends RotatedPillarBlock{

	public BlockRotated(String name, Properties builder) {
		super(builder);
		this.setRegistryName(Artimancy.MODID+":"+name);
	}

}
