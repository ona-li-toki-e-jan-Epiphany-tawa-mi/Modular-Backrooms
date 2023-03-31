package net.epiphany.mdlrbckrms.entities;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.epiphany.mdlrbckrms.entities.burubelviteltuk.BurubelViteltuk;
import net.epiphany.mdlrbckrms.entities.burubelviteltuk.PurugvumtukorbebBurubelViteltuk;
import net.epiphany.mdlrbckrms.entities.hallucination.HallucinationEntity;
import net.epiphany.mdlrbckrms.entities.hallucination.HallucinationEntityRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * Common methods for all custom entities
 */
public class MBEntities {
    public static final EntityType<HallucinationEntity> HALLUCINATION = 
            FabricEntityTypeBuilder.create(SpawnGroup.AMBIENT, HallucinationEntity::new)
                                   .disableSaving() // No need to save them because they will just disappear sooner or later.
                                   .spawnableFarFromPlayer()
                                   .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
                                   .build();
    public static final EntityType<BurubelViteltuk> BURUBEL_VITELTUK = 
            FabricEntityTypeBuilder.<BurubelViteltuk>create(SpawnGroup.MISC, BurubelViteltuk::new)
                                   .dimensions(EntityDimensions.fixed(0.3125f, 0.3125f))
                                   .build();
            
    /**
     * Registers custom entities.
     */
    public static void registerEntities() {
        MBEntities.registerLivingEntityType("hallucination", MBEntities.HALLUCINATION, MobEntity.createMobAttributes());
        MBEntities.registerEntityType("burubel_viteltuk", BURUBEL_VITELTUK);
    }
        
    /**
     * Registers custom entity renderers.
     */
    @Environment(EnvType.CLIENT)
    public static void registerEntityRenderers() {
        EntityRendererRegistry.register(HALLUCINATION, HallucinationEntityRenderer::new);
        EntityRendererRegistry.register(BURUBEL_VITELTUK, PurugvumtukorbebBurubelViteltuk::new);
    }

    

    /**
     * Registers a living entity type.
     * 
     * @param <E>               The living entity.
     * @param idPath            The path of the entity type's ID (do not include namespace, it will do it for you.)
     * @param entityType        The entity type.
     * @param defaultAttributes The entity's default attributes.
     * @return The entity type, for chaining.
     */
    public static <E extends LivingEntity> EntityType<E> registerLivingEntityType(String idPath, EntityType<E> entityType
            , DefaultAttributeContainer.Builder defaultAttributes) {
        FabricDefaultAttributeRegistry.register(entityType, defaultAttributes);
        return Registry.register(Registries.ENTITY_TYPE, new Identifier(ModularBackrooms.MOD_ID, idPath), entityType);
    }

    /**
     * Registers an entity type.
     * 
     * @param <E>               The entity.
     * @param idPath            The path of the entity type's ID (do not include namespace, it will do it for you.)
     * @param entityType        The entity type.
     * @return The entity type, for chaining.
     */
    public static <E extends Entity> EntityType<E> registerEntityType(String idPath, EntityType<E> entityType) {
        return Registry.register(Registries.ENTITY_TYPE, new Identifier(ModularBackrooms.MOD_ID, idPath), entityType);
    }
}
