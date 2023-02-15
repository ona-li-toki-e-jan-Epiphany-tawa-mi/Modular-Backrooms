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

        Registry.register(Registries.FEATURE, ChunkWallFeature.WALL_ID, ChunkWallFeature.WALL_FEATURE);
        Registry.register(Registries.FEATURE, CeilingLightArrayFeature.LIGHT_ARRAY_ID, CeilingLightArrayFeature.LIGHT_ARRAY_FEATURE);
        Registry.register(Registries.FEATURE, SubWallFeature.SUBWALL_ID, SubWallFeature.SUBWALL_FEATURE);
        Registry.register(Registries.FEATURE, WalledDoorFeature.WALLED_DOOR_ID, WalledDoorFeature.WALLED_DOOR_FEATURE);

        ModularBackrooms.LOGGER.debug("Feature registration complete");
    }
}
