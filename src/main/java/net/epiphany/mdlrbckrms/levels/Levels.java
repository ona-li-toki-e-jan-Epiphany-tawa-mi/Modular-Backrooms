package net.epiphany.mdlrbckrms.levels;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.epiphany.mdlrbckrms.levels.level0.Level0ChunkGenerator;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

/**
 * Common methods for all levels of the Backrooms.
 */
public class Levels {
    /**
     * Registers dimensions, chunk generators, biomes, etc. for the levels of the Backrooms.
     */
    public static void registerLevels() {
        ModularBackrooms.LOGGER.debug("Registering backrooms levels");

        Registry.register(Registries.CHUNK_GENERATOR, Level0ChunkGenerator.CHUNK_GENERATOR_ID, Level0ChunkGenerator.CODEC);

        ModularBackrooms.LOGGER.debug("Backrooms level registration complete");
    }
}
