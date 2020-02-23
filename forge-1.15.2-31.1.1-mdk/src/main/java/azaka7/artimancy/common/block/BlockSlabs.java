package azaka7.artimancy.common.block;

import azaka7.artimancy.Artimancy;
import net.minecraft.block.SlabBlock;

public class BlockSlabs extends SlabBlock{

	public BlockSlabs(String name, Properties builder) {
		super(builder);
		this.setRegistryName(Artimancy.MODID+":"+name);
	}

}
