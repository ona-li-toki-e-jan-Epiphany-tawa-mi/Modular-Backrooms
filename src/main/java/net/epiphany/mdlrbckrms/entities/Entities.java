package net.epiphany.mdlrbckrms.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * Common methods for all custom entities
 */
public class Entities {
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
}
