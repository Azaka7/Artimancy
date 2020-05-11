package azaka7.artimancy.common.block;

import azaka7.artimancy.Artimancy;
import net.minecraft.block.Block;

public class MiscBlock extends Block{

	public MiscBlock(String name, Block.Properties props) {
		super(props);
		this.setRegistryName(Artimancy.MODID+":"+name);
	}

}
