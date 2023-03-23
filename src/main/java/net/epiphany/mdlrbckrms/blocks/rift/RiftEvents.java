package net.epiphany.mdlrbckrms.blocks.rift;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Events relating to the rift.
 */
public class RiftEvents {
    /**
     * An event that is called when an entity enters a rift.
     * 
     * The result determines which dimension the entity will be sent to as a result. Returning null will pass the call onto the next
     *  callback. If no dimension is returned the rift will destory itself.
     * 
     * This is mainly meant to be configured per dimension.
     */
    public static final Event<OnEntityEnterRift> ON_ENTITY_ENTER_RIFT = 
            EventFactory.createArrayBacked(OnEntityEnterRift.class, callbacks -> (world, position, entity) -> {            
        for (OnEntityEnterRift callback : callbacks) {
            RegistryKey<World> result = callback.onEntityEnterRift(world, position, entity);
            
            if (result != null)
                return result;
        }
        
        return null;
    });



    @FunctionalInterface
    public interface OnEntityEnterRift {
        /**
        * Called when an entity enters a rift. 
        *
        * Note: you can only control the destination dimension, rifts teleport entities into a chunk relative to 
        *   their current position.
        * 
        * @param world    The world the rift is in.
        * @param position The position of the rift block.
        * @param player   The entity.
        * @return The world to teleport the entity to, or null, to ignore. If no dimension is returned the rift will destory itself.
        */
        @Nullable
        RegistryKey<World> onEntityEnterRift(World world, BlockPos position, Entity entity);
    }
}