package net.epiphany.mdlrbckrms;

import net.epiphany.mdlrbckrms.levels.Levels;
import net.epiphany.mdlrbckrms.levels.level0.Level0;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
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
    }

    /**
     * A global random chance used to deterimine if an action should send a player the backrooms. 
     *
     * @param random Random number generator.
     * @return Whether the player should be sent to the Backrooms.
     */
    public static boolean shouldEnterBackrooms(Random random) {
        // One in a million chance.
        return random.nextInt(1_000_000) == 0;
    }

    /**
     * Sends an entity to somewhere in Level 0.
     * @param entity The entity to send.
     */
    public static void sendToLevel0(Entity entity) {
        DimensionHelper.teleportToDimension(entity, Level0.LEVEL_0_DIMENSION_ID, entity.getWorld().getRandom());

        if (entity instanceof LivingEntity livingEntity)
            livingEntity.addStatusEffect(new StatusEffectInstance( StatusEffects.RESISTANCE
                                                                 , 60
                                                                 , 5
                                                                 , false
                                                                 , false
                                                                 , false));
    }



    /**
     * Sends the player to the Backrooms on death, preventing it and giving them some health back (between 20-40% 
     *  of their max.)
     * 
     * Guaranteed if death was from void.
     * 1% if death was from suffocation.
     * 1% if death was from drowning.
     * 1% if death was from a fall that dealt 8 or more hearts.
     * 1% if death was from flying into something and taking 6 or more hearts from it.
     * Additionally, all deaths have a small, but non-zero, chance to trigger this.
     */
    public static boolean onAllowDeathEvent(LivingEntity entity, DamageSource damageSource, float damageAmount) {
        if (!(entity instanceof PlayerEntity player) 
                || player.isCreative() || player.isSpectator()
                || Levels.isBackrooms(player.getWorld())) 
            return true;

        Random random = player.getRandom();

        if ((damageSource.isOutOfWorld() && DimensionHelper.isInVoid(player)) // Void death.
                // Suffocation death.
                || ("inWall".equals(damageSource.getName()) && random.nextFloat() <= 0.01f)
                // Drowning death. 
                || ("drown".equals(damageSource.getName()) && random.nextFloat() <= 0.01f)
                // Death from falling.
                || ("fall".equals(damageSource.getName()) && damageAmount >= 16.0f && random.nextFloat() <= 0.01f) 
                // Death from flying into something
                || ("flyIntoWall".equals(damageSource.getName()) && damageAmount >= 12.0f && random.nextFloat() <= 0.01f) 
                /* Random chance */
                || shouldEnterBackrooms(random)) { 
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
     * // TODO Make only function when player wakes up and not when they simply leave the bed.
     */
    public static void onStopSleepingEvent(LivingEntity entity, BlockPos sleepingPosition) {
        if (!(entity instanceof PlayerEntity player)
                || player.isCreative() || player.isSpectator()
                || Levels.isBackrooms(player.getWorld()))
            return;
        
        if (shouldEnterBackrooms(player.getRandom())) 
            sendToLevel0(player);
    }
}
