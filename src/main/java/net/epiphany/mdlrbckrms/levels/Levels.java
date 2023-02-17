package net.epiphany.mdlrbckrms.levels;

import java.util.HashSet;
import java.util.Set;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.epiphany.mdlrbckrms.levels.level0.Level0;
import net.epiphany.mdlrbckrms.levels.level0.Level0ChunkGenerator;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
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

    // TODO Move following methods into separate helper class.

    /**
     * Tests to see if the world is the dimension specfied by the given identifier.
     * 
     * @param world     The world to test.
     * @param dimension The dimension to test for.
     * @return Whether the world is the specified dimension.
     */
    public static boolean isDimension(World world, Identifier dimension) {
        Identifier dimensionIdentifier = world.getDimensionKey().getValue();
        return dimensionIdentifier.equals(dimension);
    }

    // TODO Make handle dimensional coordinate scaling.
    // TODO Add random position dimensional teleport.

    /**
     * Teleports an entity into the given dimension at their location.
     * 
     * @param entity    The entity to teleport.
     * @param <E>       The type of the entity.
     * @param dimension The dimension to teleport it into.
     * @return The teleported entity. Note: if non-player the original may be destroyed and it's replacement will be returned.
     */
    public static <E extends Entity> E teleportToDimension(E entity, Identifier dimension) {
        return teleportToDimension(entity, dimension, entity.getPos());
    }

    /**
     * Teleports an entity into the given dimension.
     * 
     * @param entity    The entity to teleport.
     * @param <E>       The type of the entity.
     * @param dimension The dimension to teleport it into.
     * @param x         The destination x-coordinate.
     * @param y         The destination y-coordinate.
     * @param z         The destination z-coordinate.
     * @return The teleported entity. Note: if non-player the original may be destroyed and it's replacement will be returned.
     */
    public static <E extends Entity> E teleportToDimension(E entity, Identifier dimension, double x, double y, double z) {
        return teleportToDimension(entity, dimension, new Vec3d(x, y, z));
    }

    /**
     * Teleports an entity into the given dimension.
     * 
     * @param entity    The entity to teleport.
     * @param <E>       The type of the entity.
     * @param dimension The dimension to teleport it into.
     * @param position  The position to teleport it to.
     * @return The teleported entity. Note: if non-player the original may be destroyed and it's replacement will be returned.
     */
    public static <E extends Entity> E teleportToDimension(E entity, Identifier dimension, Vec3d position) {
        World currentWorld = entity.getWorld();
        ServerWorld newWorld = currentWorld.getServer().getWorld(RegistryKey.of(RegistryKeys.WORLD, dimension));
        
        return FabricDimensions.teleport(entity, newWorld, new TeleportTarget( position
                                                                             , entity.getVelocity()
                                                                             , entity.getYaw(), entity.getPitch()));
    }
}
