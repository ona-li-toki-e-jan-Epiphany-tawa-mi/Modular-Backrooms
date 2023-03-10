package net.epiphany.mdlrbckrms.blocks.exitdoor;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Events relating to the usage of an interdimensional exit door.
 */
public class ExitDoorEvents {
    /**
     * An event that is called when a player opens an interdimensional exit door that does not currently have a portal inside.
     * 
     * The result determines if the portal should be created and where. If null, the portal will not be created.
     * Returning a non-null result to create the portal will prevent other listeners from recieving it.
     * 
     * This is mainly meant to be used to configure the interdimensional exit door's behavior per dimension.
     */
    public static final Event<TryCreatePortal> TRY_CREATE_PORTAL = 
            EventFactory.createArrayBacked(TryCreatePortal.class, callbacks -> (world, position, player) -> {            
        for (TryCreatePortal callback : callbacks) {
            PortalDestination result = callback.tryCreatePortal(world, position, player);

            if (result != null)
                return result;
        }

        return null;
    });



    @FunctionalInterface
    public interface TryCreatePortal {
        /**
         * Called when a player tries to use a interdimensional exit door that doesn't have a portal yet.
         * 
         * @param world    The world the door is in.
         * @param position The position of the door.
         * @param player   The player who opened it.
         * @return The destination of the portal, or null, to ignore.
         */
        @Nullable
        PortalDestination tryCreatePortal(World world, BlockPos position, PlayerEntity player);
    }

    /**
     * Represents the destination of an interdimenisional exit door.
     * @param location The location of the destination.
     * @param world    The world the destination lies in.
     * @param lifespan The amount of ticks before the portal disappears, set to -1 to make permanent.
     */
    public record PortalDestination(BlockPos location, RegistryKey<World> world, int lifespan) {}
}
