package net.epiphany.mdlrbckrms.utilities;

import org.jetbrains.annotations.Nullable;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.dimension.DimensionType;

/**
 * Utility functions for working with dimensions.
 */
public class DimensionHelper {
    /**
     * Wraps a dimension identifier inside a world registry key.
     * 
     * @param dimensionID The dimension identifier.
     * @return The resulting world registry key.
     */
    public static RegistryKey<World> wrapDimensionID(Identifier dimensionID) {
        return RegistryKey.of(RegistryKeys.WORLD, dimensionID);
    }

    /**
     * Tests to see if the world is the dimension specfied by the given identifier.
     * 
     * @param world     The world to test.
     * @param dimension The dimension to test for.
     * @return Whether the world is the specified dimension.
     */
    public static boolean isDimension(World world, RegistryKey<World> dimension) {
        return world.getRegistryKey().equals(dimension);
    }

    /**
     * Finds a world by it's registry key.
     * 
     * @param world        Another world to piggyback off of to get the server instance.
     * @param dimensionKey The dimension registry key.
     * @return The world, or {@code null} if it could not be found.
     */
    @Nullable
    public static ServerWorld getWorldByKey(World world, RegistryKey<World> dimensionKey) {
        return world.getServer().getWorld(dimensionKey);
    }



    /**
     * Tells whether the entity is in the void, which is 64 blocks below the world's minimum y level.
     * 
     * @param entity The entity to test.
     * @return Whether the entity is in the void.
     */
    public static boolean isInVoid(Entity entity) {
        return entity.getY() < (double)(entity.getWorld().getBottomY() - 64);
    }



    /**
     * Teleports an entity into the given dimension at their location. If the dimension could not be found the entity will not
     *  be teleported and {@code null} will be returned. Applies Resistance V to make sure they survive the trip.
     * 
     * @param entity       The entity to teleport.
     * @param <E>          The type of the entity.
     * @param dimension    The dimension to teleport it into.
     * @param applyScaling Whether to account for the coordinate scaling factor of the 2 dimensions.
     * @return The teleported entity, or {@code null}, if the teleport failed. Note: if non-player the original may be 
     *  destroyed and it's replacement will be returned.
     */
    @Nullable
    public static <E extends Entity> E teleportToDimension(E entity, RegistryKey<World> dimension, boolean applyScaling) {
        return teleportToDimension(entity, dimension, entity.getPos(), applyScaling);
    }

    /**
     * Teleports an entity into the given dimension. If the dimension could not be found the entity will not
     *  be teleported and {@code null} will be returned. Applies Resistance V to make sure they survive the trip.
     * 
     * @param entity       The entity to teleport.
     * @param <E>          The type of the entity.
     * @param dimension    The dimension to teleport it into.
     * @param x            The destination x-coordinate.
     * @param y            The destination y-coordinate.
     * @param z            The destination z-coordinate.
     * @param applyScaling Whether to account for the coordinate scaling factor of the 2 dimensions.
     * @return The teleported entity, or {@code null}, if the teleport failed. Note: if non-player the original may be 
     *  destroyed and it's replacement will be returned.
     */
    @Nullable
    public static <E extends Entity> E teleportToDimension(E entity, RegistryKey<World> dimension, double x, double y
            , double z, boolean applyScaling) {
        return teleportToDimension(entity, dimension, new Vec3d(x, y, z), applyScaling);
    }

    /**
     * Teleports an entity into the given dimension. If the dimension could not be found the entity will not
     *  be teleported and {@code null} will be returned. Applies Resistance V to make sure they survive the trip.
     * 
     * @param entity       The entity to teleport.
     * @param <E>          The type of the entity.
     * @param dimension    The dimension to teleport it into.
     * @param position     The position to teleport it to.
     * @param applyScaling Whether to account for the coordinate scaling factor of the 2 dimensions.
     * @return The teleported entity, or {@code null}, if the teleport failed. Note: if non-player the original may be 
     *  destroyed and it's replacement will be returned.
     */
    @Nullable
    public static <E extends Entity> E teleportToDimension(E entity, RegistryKey<World> dimension, Vec3d position
            , boolean applyScaling) {
        ServerWorld newWorld = getWorldByKey(entity.getWorld(), dimension);
        if (newWorld == null) {
            ModularBackrooms.LOGGER.warn( "Attemtped to teleport entity to dimension under unknown ID: '" 
                                        + dimension.getValue() + "'!");
            return null;
        }
        
        return teleportToDimension(entity, newWorld, position, applyScaling);
    }

    /**
     * Teleports an entity into the given dimension. Applies Resistance V to make sure they survive the trip.
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
     * Teleports an entity into the given dimension. Applies Resistance V to make sure they survive the trip.
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

        // Life insurance.
        if (entity instanceof LivingEntity livingEntity) 
            livingEntity.addStatusEffect(new StatusEffectInstance( StatusEffects.RESISTANCE
                                                                 , 100
                                                                 , 5
                                                                 , false
                                                                 , false
                                                                 , false));

        return FabricDimensions.teleport(entity, world, new TeleportTarget( position
                                                                          , entity.getVelocity()
                                                                          , entity.getYaw(), entity.getPitch()));
    }

    /**
     * Teleports an entity into the given dimension at a random, but safe, location. If the dimension or a safe spot inside it
     *  could not be found the entity will not be teleported and {@code null} will be returned. Applies Resistance V to make 
     *  sure they survive the trip.
     * 
     * @param entity    The entity to teleport.
     * @param <E>       The type of the entity.
     * @param dimension The dimension to teleport it into.
     * @param random    Random number generator.
     * @return The teleported entity, or {@code null}, if the teleport failed. Note: if non-player the original may be 
     *  destroyed and it's replacement will be returned.
     */
    @Nullable
    public static <E extends Entity> E teleportToDimension(E entity, RegistryKey<World> dimension, Random random) {
        ServerWorld newWorld = getWorldByKey(entity.getWorld(), dimension);
        if (newWorld == null) {
            ModularBackrooms.LOGGER.warn( "Attemtped to teleport entity to dimension under unknown ID: '" 
                                        + dimension.getValue() + "'!");
            return null;
        }

        return teleportToDimension(entity, newWorld, random);
    }

    /**
     * Teleports an entity into the given dimension at a random, but safe, location. If a safe spot inside the dimension
     *  could not be found the entity will not be teleported and {@code null} will be returned. Applies Resistance V to make 
     *  sure they survive the trip.
     * 
     * @param entity The entity to teleport.
     * @param <E>    The type of the entity.
     * @param world  The dimension to teleport it to.
     * @param random Random number generator.
     * @return The teleported entity, or {@code null}, if the teleport failed. Note: if non-player the original may be 
     *  destroyed and it's replacement will be returned.
     */
    @Nullable
    public static <E extends Entity> E teleportToDimension(E entity, ServerWorld world, Random random) {
        WorldBorder newWorldBorder = world.getWorldBorder();
        
        for (int attempts = 3; attempts > 0; attempts--) {
            double xAxisSize = random.nextBoolean() ? newWorldBorder.getBoundSouth() : newWorldBorder.getBoundNorth()
                 , zAxisSize = random.nextBoolean() ? newWorldBorder.getBoundEast()  : newWorldBorder.getBoundWest();
            ChunkPos randomChunk = new ChunkPos( ChunkSectionPos.getSectionCoordFloored(random.nextDouble() * xAxisSize)
                                               , ChunkSectionPos.getSectionCoordFloored(random.nextDouble() * zAxisSize));

            Vec3d destination = MiscellaneousHelpers.findValidPosition(world, randomChunk, entity);

            if (destination != null) 
                return teleportToDimension(entity, world, destination, false);
        }   

        return null;
    }
}
