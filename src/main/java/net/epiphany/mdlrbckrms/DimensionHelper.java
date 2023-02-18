package net.epiphany.mdlrbckrms;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;

/**
 * Utility functions for working with dimensions.
 */
public class DimensionHelper {
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

    /**
     * Finds a world by it's identifier.
     * 
     * @param world       Another world to piggyback off of to get the server instance.
     * @param dimensionID The dimension identifier.
     * @return The world, or null if it could not be found.
     */
    @Nullable
    public static ServerWorld getWorldByID(World world, Identifier dimensionID) {
        return world.getServer().getWorld(RegistryKey.of(RegistryKeys.WORLD, dimensionID));
    }



    /**
     * Teleports an entity into the given dimension at their location. If the dimension could not be found the entity will not
     *  be teleported.
     * 
     * @param entity       The entity to teleport.
     * @param <E>          The type of the entity.
     * @param dimensionID  The dimension to teleport it into.
     * @param applyScaling Whether to account for the coordinate scaling factor of the 2 dimensions.
     * @return The teleported entity. Note: if non-player the original may be destroyed and it's replacement will be returned.
     */
    public static <E extends Entity> E teleportToDimension(E entity, Identifier dimensionID, boolean applyScaling) {
        return teleportToDimension(entity, dimensionID, entity.getPos(), applyScaling);
    }

    /**
     * Teleports an entity into the given dimension. If the dimension could not be found the entity will not
     *  be teleported.
     * 
     * @param entity      The entity to teleport.
     * @param <E>         The type of the entity.
     * @param dimensionID The dimension to teleport it into.
     * @param x           The destination x-coordinate.
     * @param y           The destination y-coordinate.
     * @param z           The destination z-coordinate.
     * @param applyScaling Whether to account for the coordinate scaling factor of the 2 dimensions.
     * @return The teleported entity. Note: if non-player the original may be destroyed and it's replacement will be returned.
     */
    public static <E extends Entity> E teleportToDimension(E entity, Identifier dimensionID, double x, double y, double z
            , boolean applyScaling) {
        return teleportToDimension(entity, dimensionID, new Vec3d(x, y, z), applyScaling);
    }

    /**
     * Teleports an entity into the given dimension. If the dimension could not be found the entity will not
     *  be teleported.
     * 
     * @param entity       The entity to teleport.
     * @param <E>          The type of the entity.
     * @param dimensionID  The dimension to teleport it into.
     * @param position     The position to teleport it to.
     * @param applyScaling Whether to account for the coordinate scaling factor of the 2 dimensions.
     * @return The teleported entity. Note: if non-player the original may be destroyed and it's replacement will be returned.
     */
    public static <E extends Entity> E teleportToDimension(E entity, Identifier dimensionID, Vec3d position
            , boolean applyScaling) {
        ServerWorld newWorld = getWorldByID(entity.getWorld(), dimensionID);

        if (newWorld == null)
            return entity;
        
        return teleportToDimension(entity, newWorld, position, applyScaling);
    }

    /**
     * Teleports an entity into the given dimension. If the dimension could not be found the entity will not
     *  be teleported.
     * 
     * @param entity       The entity to teleport.
     * @param <E>          The type of the entity.
     * @param world        The dimension to teleport it to.
     * @param x            The destination x-coordinate.
     * @param y            The destination y-coordinate.
     * @param z            The destination z-coordinate.
     * @param applyScaling Whether to account for the coordinate scaling factor of the 2 dimensions.
     * @return The teleported entity. Note: if non-player the original may be destroyed and it's replacement will be returned.
     */
    public static <E extends Entity> E teleportToDimension(E entity, ServerWorld world, double x, double y, double z
            , boolean applyScaling) {
        return teleportToDimension(entity, world, new Vec3d(x, y, z), applyScaling);
    }

    /**
     * Teleports an entity into the given dimension. If the dimension could not be found the entity will not
     *  be teleported.
     * 
     * @param entity       The entity to teleport.
     * @param <E>          The type of the entity.
     * @param world        The dimension to teleport it to.
     * @param position     The position to teleport it to.
     * @param applyScaling Whether to account for the coordinate scaling factor of the 2 dimensions.
     * @return The teleported entity. Note: if non-player the original may be destroyed and it's replacement will be returned.
     */
    public static <E extends Entity> E teleportToDimension(E entity, ServerWorld world, Vec3d position, boolean applyScaling) {
        if (applyScaling && entity.getWorld() != world) {
           double scaleFactor = DimensionType.getCoordinateScaleFactor(entity.getWorld().getDimension(), world.getDimension());
           position = position.multiply(scaleFactor, 1.0, scaleFactor);
        }

        return FabricDimensions.teleport(entity, world, new TeleportTarget( position
                                                                          , entity.getVelocity()
                                                                          , entity.getYaw(), entity.getPitch()));
    }

    /**
     * Teleports an entity into the given dimension at a random, but safe, location. If the dimension could not be found the 
     *  entity will not be teleported.
     * 
     * @param entity      The entity to teleport.
     * @param <E>         The type of the entity.
     * @param dimensionID The dimension to teleport it into.
     * @param random      Random number generator.
     * @return The teleported entity. Note: if non-player the original may be destroyed and it's replacement will be returned.
     */
    public static <E extends Entity> E teleportToDimension(E entity, Identifier dimensionID, Random random) {
        ServerWorld newWorld = getWorldByID(entity.getWorld(), dimensionID);
        if (newWorld == null)
            return entity;

        WorldBorder newWorldBorder = newWorld.getWorldBorder();
        ChunkGenerator newWorldChunkGenerator = newWorld.getChunkManager().getChunkGenerator();
        int minimumY = newWorldChunkGenerator.getMinimumY()
          , maximumY = minimumY + newWorldChunkGenerator.getWorldHeight();

        double x, y, z;
        BlockPos.Mutable possibleDestination = new BlockPos.Mutable();
        
    FindSafeTeleportDestination:
        while (true) {
            double xAxisSize = random.nextBoolean() ? newWorldBorder.getBoundSouth() : newWorldBorder.getBoundNorth()
                 , zAxisSize = random.nextBoolean() ? newWorldBorder.getBoundEast()  : newWorldBorder.getBoundWest();
            ChunkPos randomChunk = new ChunkPos( ChunkSectionPos.getSectionCoordFloored(random.nextDouble() * xAxisSize)
                                               , ChunkSectionPos.getSectionCoordFloored(random.nextDouble() * zAxisSize));

            // Iterates through EVERY SINGLE possible location within the chunk to find a safe location.
            for (int i = randomChunk.getStartX(); i <= randomChunk.getEndX(); ++i)
                for (int j = randomChunk.getStartZ(); j <= randomChunk.getEndZ(); ++j) 
                    for (int k = minimumY; k < maximumY; k++) {
                        possibleDestination.set(i, k, j);
                        
                        if (SpawnHelper.canSpawn( SpawnRestriction.Location.ON_GROUND
                                                , newWorld, possibleDestination
                                                , entity.getType())) {
                            x = possibleDestination.getX();
                            y = possibleDestination.getY();
                            z = possibleDestination.getZ();
                            break FindSafeTeleportDestination;
                        }
                    }
        }

        // Make sure to spawn entity in middle of block to prevent suffocation.
        return teleportToDimension(entity, newWorld, x + 0.5, y + 0.5, z + 0.5, false);
    }
}
