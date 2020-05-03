package azaka7.artimancy.common.magic;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class TeleportSpell extends AbstractSpell {

	public TeleportSpell(String id) {
		super(id);
	}

	@Override
	public boolean canCastSpell(LivingEntity caster) {
		return true;
	}

	@Override
	public boolean castSpell(LivingEntity caster, int power, int focus) {
		
		World world = caster.getEntityWorld();
		caster.playSound(SoundEvents.ENTITY_ENDER_PEARL_THROW, 1.0F, (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.2F + 1.0F);
		
		EnderPearlEntity pearl = new EnderPearlEntity(world, caster);
		pearl.shoot(caster, caster.rotationPitch, caster.rotationYaw, 0.0F, (float)Math.pow(1.2F, 1+power), (float) Math.pow(20.0F, 1 - Math.sqrt(0.5F*focus)));
		pearl.setGlowing(true);
		world.addEntity(pearl);
		
		return true;
	}

	@Override
	public int baseCost(LivingEntity caster) {
		return 10;
	}

}
