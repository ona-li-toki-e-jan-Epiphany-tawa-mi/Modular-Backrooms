package net.epiphany.mdlrbckrms;

import net.epiphany.mdlrbckrms.levels.Levels;
import net.epiphany.mdlrbckrms.levels.level0.Level0;
import net.epiphany.mdlrbckrms.utilities.DimensionHelper;
import net.epiphany.mdlrbckrms.utilities.MiscellaneousHelpers;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
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
    }



    /**
     * Sends the player to the Backrooms on death by the void, preventing it and giving them some health back (between 20-40% 
     *  of their max.) Additionally, prevents players from leaving backrooms by dying.
     */
    public static boolean onAllowDeathEvent(LivingEntity entity, DamageSource damageSource, float damageAmount) {
        if (!(entity instanceof ServerPlayerEntity player) || player.isSpectator()) 
            return true;

        Random random = player.getRandom();
        ServerWorld world = player.getWorld();

        // Prevents players from leaving by dying.
        if (Levels.isBackrooms(world)) {
            MiscellaneousHelpers.fakePlayerDeath(player, damageSource);
            player.setHealth(1.0f);
            DimensionHelper.teleportToDimension(player, world, random);

            return false;

        // Entrance by void death.
        } else if (damageSource.isOutOfWorld() && DimensionHelper.isInVoid(player)) { 
            player.setHealth(player.getMaxHealth() * (random.nextFloat() * 0.2f + 0.2f));
            DimensionHelper.teleportToDimension(player, Level0.LEVEL_0, random);

            return false;
        }
        
       return true; 
    }
}
