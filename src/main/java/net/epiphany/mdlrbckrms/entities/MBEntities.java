package net.epiphany.mdlrbckrms.entities;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.epiphany.mdlrbckrms.entities.hallucination.HallucinationEntity;
import net.epiphany.mdlrbckrms.entities.hallucination.HallucinationEntityRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * Common methods for all custom entities
 */
public class MBEntities {
    /**
     * Registers custom entities.
     */
    public static void registerEntities() {
        HallucinationEntity.register();
    }
        
    /**
     * Registers custom entity renderers.
     */
    @Environment(EnvType.CLIENT)
    public static void registerEntityRenderers() {
        HallucinationEntityRenderer.register();
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
}
