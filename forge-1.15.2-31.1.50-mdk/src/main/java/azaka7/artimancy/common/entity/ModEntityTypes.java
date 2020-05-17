package azaka7.artimancy.common.entity;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import azaka7.artimancy.common.entity.projectile.MagicMissileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;

public class ModEntityTypes {
	
	private static final ArrayList<EntityType<?>> entityTypes = new ArrayList<EntityType<?>>();
	
	public static final EntityType<? extends MagicMissileEntity> MAGIC_MISSILE = register("artimancy:magic_missile", EntityType.Builder.<MagicMissileEntity>create(MagicMissileEntity::new,EntityClassification.MISC).size(0.5F, 0.5F));
	
	private static final <T extends Entity> EntityType<T> register(String key, EntityType.Builder<T> builder) {
		EntityType<T> ret = builder.build(key);
		ret.setRegistryName(key);
		entityTypes.add(ret);
		return ret;
	}
	
	public static final List<EntityType<?>> getEntityTypesForRegister(){
		return ImmutableList.copyOf(entityTypes);
	}
	
}
