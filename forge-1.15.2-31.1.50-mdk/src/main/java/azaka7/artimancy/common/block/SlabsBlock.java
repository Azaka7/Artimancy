package azaka7.artimancy.common.block;

import azaka7.artimancy.Artimancy;
import net.minecraft.block.SlabBlock;

public class SlabsBlock extends SlabBlock{

	public SlabsBlock(String name, Properties builder) {
		super(builder);
		this.setRegistryName(Artimancy.MODID+":"+name);
	}

}
