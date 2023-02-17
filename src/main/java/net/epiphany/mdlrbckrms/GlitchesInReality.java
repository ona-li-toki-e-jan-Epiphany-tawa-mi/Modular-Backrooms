package net.epiphany.mdlrbckrms;

import net.epiphany.mdlrbckrms.levels.Levels;
import net.epiphany.mdlrbckrms.levels.level0.Level0;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

/**
 * Contains the code responsible for allowing players to glitch through reality into the Backrooms
 */
public class GlitchesInReality {
    /**
     * Registers the listeners needed to create the "glitches".
     */
    public static void registerGlitches() {
        ServerLivingEntityEvents.ALLOW_DEATH.register(GlitchesInReality::onAllowDeathEvent);
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(GlitchesInReality::onAfterPlayerChangeWorldEvent);
        EntitySleepEvents.STOP_SLEEPING.register(GlitchesInReality::onStopSleepingEvent);
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(GlitchesInReality::onAllowDamageEvent);
    }

    /**
     * A global random chance used to deterimine if an action should send a player the backrooms. 
     *
     * @param random Random number generator.
     * @return Whether the player should be sent to the Backrooms.
     */
    public static boolean shouldEnterBackrooms(Random random) {
        return true; // TODO Remove

        // Literal one in a billion chance.
        //return random.nextInt(1_000_000_000) == 0;
    }

    /**
     * Sends an entity to somewhere in Level 0.
     * @param entity The entity to send.
     */
    public static void sendToLevel0(Entity entity) {
        Levels.teleportToDimension(entity, Level0.LEVEL_0_DIMENSION_ID, entity.getX(), 0.0, entity.getY());
    }



    /**
     * Sends the player to the Backrooms on death, preventing it and giving them some health back (between 20-40% 
     *  of their max.)
     */
    public static boolean onAllowDeathEvent(LivingEntity entity, DamageSource damageSource, float damageAmount) {
        if (!(entity instanceof PlayerEntity player) 
                || player.isCreative() || player.isSpectator()
                || Levels.isBackrooms(player.getWorld())) 
            return true;

        Random random = player.getRandom();

        if (shouldEnterBackrooms(random)) {
            player.setHealth(player.getMaxHealth() * (random.nextFloat() * 0.2f + 0.2f));
            sendToLevel0(player);
            return false;
        }
        
       return true; 
    }

    /**
     * Reroutes players travelling to and from non-backrooms dimensions into the Backrooms.
     */
    public static void onAfterPlayerChangeWorldEvent(ServerPlayerEntity player, ServerWorld origin, ServerWorld destination) {
        if (player.isCreative() || player.isSpectator()
                || Levels.isBackrooms(origin) 
                || Levels.isBackrooms(destination))
            return;

        if (shouldEnterBackrooms(origin.getRandom()))
            sendToLevel0(player);
    }

    /**
     * Sends players to the Backrooms when they wake up.
     */
    public static void onStopSleepingEvent(LivingEntity entity, BlockPos sleepingPosition) {
        if (!(entity instanceof PlayerEntity player)
                || player.isCreative() || player.isSpectator()
                || Levels.isBackrooms(player.getWorld()))
            return;
        
        if (shouldEnterBackrooms(player.getRandom())) 
            sendToLevel0(player);
    }

    /**
     * Sends players to the Backrooms depending when damaged depending on various factors as follows:
     * - Falling into the void.
     * - Falling and taking 50% or more of your maximum health in damage.
     * - Suffocating.
     * - Flying into a wall on elytra and taking 50% or more of your maximum health in damage.
     * - Being damaged by an entity related to the end.
     */
    public static boolean onAllowDamageEvent(LivingEntity entity, DamageSource source, float amount) {
        if (!(entity instanceof PlayerEntity player)
                || player.isCreative() || player.isSpectator()
                || Levels.isBackrooms(player.getWorld()))
            return true;

        Random random = player.getRandom();
        
        if (source.isOutOfWorld() && shouldEnterBackrooms(random)) {
            sendToLevel0(player);
            return false;

        } else if (source.isFromFalling() && amount >= player.getMaxHealth() * 0.5f && shouldEnterBackrooms(random)) {
            sendToLevel0(player);
            return false;

        } else if ("inWall".equals(source.getName()) && shouldEnterBackrooms(random)) {
            sendToLevel0(player);
            return false;

        } else if ("flyIntoWall".equals(source.getName()) && amount >= player.getMaxHealth() * 0.5f 
                && shouldEnterBackrooms(random)) {
            sendToLevel0(player);
            return false;

        } else {
            Entity attacker = source.getAttacker();

            if (attacker != null
                    && ( attacker instanceof EndermanEntity
                     || attacker instanceof EndermiteEntity
                     || attacker instanceof EnderDragonEntity
                     || attacker instanceof EndCrystalEntity
                     || attacker instanceof ShulkerEntity)
                    && shouldEnterBackrooms(random)) {
                sendToLevel0(entity);
                return false;
            }
        }

        return true;
    }
}
