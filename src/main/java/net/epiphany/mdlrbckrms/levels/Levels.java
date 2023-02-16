package net.epiphany.mdlrbckrms.levels;

import java.util.HashSet;
import java.util.Set;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.epiphany.mdlrbckrms.levels.level0.Level0;
import net.epiphany.mdlrbckrms.levels.level0.Level0ChunkGenerator;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
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

    /**
     * Teleports an entity into the given dimension at their location.
     * 
     * @param entity    The entity to teleport.
     * @param dimension The dimension to teleport it into.
     */
    public static void teleportToDimension(Entity entity, Identifier dimension) {
        // TODO Make handle dimensional coordinate scaling.
        teleportToDimension(entity, dimension, entity.getX(), entity.getY(), entity.getZ());
    }

    /**
     * Teleports an entity into the given dimension.
     * 
     * @param entity    The entity to teleport.
     * @param dimension The dimension to teleport it into.
     * @param x         The destination x-coordinate.
     * @param y         The destination y-coordinate.
     * @param z         The destination z-coordinate.
     */
    public static void teleportToDimension(Entity entity, Identifier dimension, double x, double y, double z) {
        World currentWorld = entity.getWorld();
        ServerWorld newWorld = currentWorld.getServer().getWorld(RegistryKey.of(RegistryKeys.WORLD, dimension));
        
        if (currentWorld.getDimensionKey().equals(newWorld.getDimensionKey()))
            return;

        
        // Preload destination chunk for speedy transport baby ;).
        newWorld.getChunkManager().addTicket( ChunkTicketType.POST_TELEPORT
                                            , new ChunkPos(entity.getBlockPos()), 1
                                            , entity.getId());
        
        // If the entity is a player we need to use a special method so that everything is properly synced.
        if (entity instanceof ServerPlayerEntity playerEntity) {
            playerEntity.teleport( newWorld
                                 , x, y, z
                                 , playerEntity.getYaw(), playerEntity.getPitch());

        } else {
            entity.moveToWorld(newWorld);
            entity.teleport(x, y, z);
        }
    }
}
