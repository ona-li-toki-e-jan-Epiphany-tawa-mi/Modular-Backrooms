package net.epiphany.mdlrbckrms.features;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

/**
 * Common methods for all custom features.
 */
public class Features {
    /**
     * Registers all custom features.
     */
    public static void registerFeatures() {
        ModularBackrooms.LOGGER.debug("Registering features");

        Registry.register(Registries.FEATURE, ChunkWallFeature.CHUNK_WALL_ID, ChunkWallFeature.CHUNK_WALL_FEATURE);
        Registry.register(Registries.FEATURE, FluorescentLightArrayFeature.FLUORESCENT_LIGHT_ARRAY_ID, FluorescentLightArrayFeature.FLUORESCENT_LIGHT_ARRAY_FEATURE);
        Registry.register(Registries.FEATURE, DividerWallFeature.DIVIDER_WALL_ID, DividerWallFeature.DIVIDER_WALL_FEATURE);
        Registry.register(Registries.FEATURE, WalledDoorFeature.WALLED_DOOR_ID, WalledDoorFeature.WALLED_DOOR_FEATURE);

        ModularBackrooms.LOGGER.debug("Feature registration complete");
    }
}
