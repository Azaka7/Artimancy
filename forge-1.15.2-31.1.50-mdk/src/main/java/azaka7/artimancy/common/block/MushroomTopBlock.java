package azaka7.artimancy.common.block;

import azaka7.artimancy.Artimancy;
import net.minecraft.block.HugeMushroomBlock;

public class MushroomTopBlock extends HugeMushroomBlock{

	public MushroomTopBlock(String name, Properties p_i49982_1_) {
		super(p_i49982_1_);
		this.setRegistryName(Artimancy.MODID+":"+name);
	}

}
