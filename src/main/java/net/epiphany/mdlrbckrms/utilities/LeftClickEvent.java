package net.epiphany.mdlrbckrms.utilities;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Events relating to players left-clicking.
 */
public class LeftClickEvent {
    /**
     * An event that is called when a player left clicks.
     * 
     * TODO Consider making this part of a library mod.
     * 
     * Runs on both the client and server side.
     * Works irregardless of whether the player left-clicks an entity, a block, the air, and whether they are using an item.
     * Note: does not account for things like cooldown or if a player is actively riding something. If you desire that behavior you
     *      will have to check for that yourself. Also does not check if they are in spectator mode.
     * Called BEFORE vanilla does anything with the left click, take care.
     */
    public static final Event<OnLeftClick> ON_LEFT_CLICK = 
            EventFactory.createArrayBacked(OnLeftClick.class, callbacks -> (world, position, player) -> {            
        for (OnLeftClick callback : callbacks) 
            callback.onLeftClick(world, position, player);
    });



    @FunctionalInterface
    public interface OnLeftClick {
        /**
         * Called when a player left clicks
         * 
         * @param world    The world the player is in.
         * @param position The player.
         * @param player   The item in the player's main hand used to left-click.
         * @return The destination of the portal, or null, to ignore.
         */
        void onLeftClick(World world, PlayerEntity player, ItemStack item);
    }
}
