package net.epiphany.mdlrbckrms;

import net.epiphany.mdlrbckrms.levels.Levels;
import net.epiphany.mdlrbckrms.levels.level0.Level0;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
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
     * A global random chance used to deterimine if an action should send a player the backrooms. 
     *
     * @param random Random number generator.
     * @return Whether the player should be sent to the Backrooms.
     */
    private static boolean shouldEnterBackrooms(Random random) {
        return true; // TODO Remove

        // Literal one in a billion chance.
        //return random.nextInt(1_000_000_000) == 0;
    }



    /**
     * Sends the player to the Backrooms on death, preventing it and giving them some health back (between 20-40% 
     *  of their max.)
     */
    public static boolean onAllowDeathEvent(LivingEntity entity, DamageSource damageSource, float damageAmount) {
        if (!(entity instanceof PlayerEntity) || Levels.isBackrooms(entity.getWorld())) 
            return true;

        Random random = entity.getRandom();
        if (!shouldEnterBackrooms(random))
            return true;

        entity.setHealth(entity.getMaxHealth() * (random.nextFloat() * 0.2f + 0.2f));
        Levels.teleportToDimension(entity, Level0.LEVEL_0_DIMENSION_ID, entity.getX(), 0.0, entity.getY());
        return false;
    }
}
