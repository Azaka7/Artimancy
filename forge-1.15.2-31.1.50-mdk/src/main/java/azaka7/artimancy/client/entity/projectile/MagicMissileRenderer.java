package azaka7.artimancy.client.entity.projectile;

import azaka7.artimancy.common.entity.projectile.MagicMissileEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class MagicMissileRenderer extends ArrowRenderer<MagicMissileEntity> {

	public MagicMissileRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);
	}

	@Override
	public ResourceLocation getEntityTexture(MagicMissileEntity entity) {
		return MagicMissileEntity.getTextureLocation();
	}

}
