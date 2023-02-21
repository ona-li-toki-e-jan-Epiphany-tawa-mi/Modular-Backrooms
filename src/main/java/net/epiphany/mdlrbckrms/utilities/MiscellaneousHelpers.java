package net.epiphany.mdlrbckrms.utilities;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Various helper functions and what-not.
 */
public class MiscellaneousHelpers {
    /**
     * Fakes the death of the player, applying all the corresponding effects without actually killing the player.
     * 
     * @param player       The player to "kill"
     * @param damageSource The damage source that dealt the "lethal" blow.
     */
    public static void fakePlayerDeath(ServerPlayerEntity player, DamageSource damageSource) {
        player.onDeath(damageSource);
        player.setExperienceLevel(0);
        player.setExperiencePoints(0);
    }
}