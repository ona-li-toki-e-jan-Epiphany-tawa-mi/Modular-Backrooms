package net.epiphany.mdlrbckrms.levels;

import java.util.HashSet;
import java.util.Set;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.epiphany.mdlrbckrms.levels.level0.Level0;
import net.epiphany.mdlrbckrms.levels.level0.Level0ChunkGenerator;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

/**
 * Common methods for all levels of the Backrooms.
 */
public class Levels {
    /**
     * A set of IDs of each Backrooms level used for various Hing-Digs and Hoo-Has.
     * NOTE: All Backrooms levels must be added to this set for them to work properly.
     */
    private static Set<Identifier> backroomsLevels = new HashSet<>();

    /**
     * Registers dimensions, chunk generators, biomes, etc. for the levels of the Backrooms.
     */
    public static void registerLevels() {
        ModularBackrooms.LOGGER.debug("Registering backrooms levels");

        Registry.register(Registries.CHUNK_GENERATOR, Level0ChunkGenerator.LEVEL_0_CHUNK_GENERATOR_ID, Level0ChunkGenerator.CODEC);
        backroomsLevels.add(Level0.LEVEL_0_DIMENSION_ID);

        ModularBackrooms.LOGGER.debug("Backrooms level registration complete");
    }



    /**
     * Tells whether the given world is a backrooms level.
     * 
     * @param world The world to test.
     * @return Whether the world is the backrooms.
     */
    public static boolean isBackrooms(World world) {
        Identifier dimensionIdentifier = world.getDimensionKey().getValue();
        return backroomsLevels.contains(dimensionIdentifier);
    }
}
