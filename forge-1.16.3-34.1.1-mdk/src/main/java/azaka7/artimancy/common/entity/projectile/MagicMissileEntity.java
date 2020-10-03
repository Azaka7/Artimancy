package azaka7.artimancy.common.entity.projectile;

import azaka7.artimancy.Artimancy;
import azaka7.artimancy.common.entity.ModEntityTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class MagicMissileEntity extends AbstractArrowEntity{
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(Artimancy.MODID,"textures/entity/magic_missile_projectile.png");

	public MagicMissileEntity(EntityType<? extends MagicMissileEntity> type, World worldIn) {
		super(type, worldIn);
		this.setHitSound(SoundEvents.ENTITY_EVOKER_CAST_SPELL);
	}

	public MagicMissileEntity(double x, double y, double z, World worldIn) {
		super(ModEntityTypes.MAGIC_MISSILE, x, y, z, worldIn);
		this.setHitSound(SoundEvents.ENTITY_EVOKER_CAST_SPELL);
	}
	
	public MagicMissileEntity(LivingEntity shooter, World worldIn) {
		super(ModEntityTypes.MAGIC_MISSILE, shooter, worldIn);
		this.setHitSound(SoundEvents.ENTITY_EVOKER_CAST_SPELL);
	}
	
	@Override
	public void tick() {
		super.tick();
		this.world.addParticle(ParticleTypes.PORTAL, this.getPosX(), this.getPosY(), this.getPosZ(), 0, -0.5, 0);
		this.world.addParticle(ParticleTypes.ENCHANTED_HIT, this.getPosX(), this.getPosY(), this.getPosZ(), 0, 0, 0);
		if(this.inGround) {
			this.remove();
		}
	}
	
	@Override
	protected void onEntityHit(EntityRayTraceResult rtresult) {
		Entity entity = rtresult.getEntity();
		float f = (float)this.getMotion().length();
		int i = MathHelper.ceil(Math.max((double)f * this.getDamage(), 0.0D));
		
		if (this.getIsCritical()) {
			i += this.rand.nextInt(i / 2 + 2);
		}

		Entity entity1 = this.func_234616_v_();
		DamageSource damagesource;
		if (entity1 == null) {
			damagesource = DamageSource.causeArrowDamage(this, this).setMagicDamage();
		} else {
			damagesource = DamageSource.causeArrowDamage(this, entity1).setMagicDamage();
			if (entity1 instanceof LivingEntity) {
				((LivingEntity)entity1).setLastAttackedEntity(entity);
			}
		}
		boolean flag = entity.getType() == EntityType.ENDERMAN;
		int j = entity.getFireTimer();
		if (this.isBurning() && !flag) {
			entity.setFire(5);
		}

		if (entity.attackEntityFrom(damagesource, (float)i)) {
			if (flag) {
				return;
			}

			if (entity instanceof LivingEntity) {
				LivingEntity livingentity = (LivingEntity)entity;

				if (!this.world.isRemote && entity1 instanceof LivingEntity) {
					EnchantmentHelper.applyThornEnchantments(livingentity, entity1);
					EnchantmentHelper.applyArthropodEnchantments((LivingEntity)entity1, livingentity);
				}

				this.arrowHit(livingentity);
				if (entity1 != null && livingentity != entity1 && livingentity instanceof PlayerEntity && entity1 instanceof ServerPlayerEntity) {
					((ServerPlayerEntity)entity1).connection.sendPacket(new SChangeGameStatePacket(SChangeGameStatePacket.field_241770_g_, 0.0F));
				}
			}

			this.playSound(this.getHitGroundSound(), 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
			if (this.getPierceLevel() <= 0) {
				this.remove();
			}
		} else {
			entity.setFire(j);
			this.setMotion(this.getMotion().scale(-0.1D));
			this.rotationYaw += 180.0F;
			this.prevRotationYaw += 180.0F;
			this.isAirBorne = false;
			if (!this.world.isRemote && this.getMotion().lengthSquared() < 1.0E-7D) {
				this.remove();
			}
		}
	}

	
	@Override
	protected ItemStack getArrowStack() {
		return ItemStack.EMPTY;
	}

	public static ResourceLocation getTextureLocation() {
		return TEXTURE;
	}
	
	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
