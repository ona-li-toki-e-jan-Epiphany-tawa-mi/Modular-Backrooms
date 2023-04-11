package net.epiphany.mdlrbckrms.levels;

import java.util.HashSet;
import java.util.Set;

import net.epiphany.mdlrbckrms.levels.level0.Level0;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

/**
 * Common methods for all levels of the Backrooms.
 */
public class Levels {
    /**
     * A set of IDs of each Backrooms level used for various Hing-Digs and Hoo-Has.
     * NOTE: All Backrooms levels must be added to this set for them to work properly.
     */
    private static Set<RegistryKey<World>> backroomsLevels = new HashSet<>();

    /**
     * Registers dimensions, chunk generators, biomes, etc. for the levels of the Backrooms.
     */
    public static void registerLevels() {
        Level0.register(backroomsLevels);
    }



    /**
     * Tells whether the given world is a backrooms level.
     * 
     * @param world The world to test.
     * @return Whether the world is the backrooms.
     */
    public static boolean isBackrooms(World world) {
        return backroomsLevels.contains(world.getRegistryKey());
    }
}
