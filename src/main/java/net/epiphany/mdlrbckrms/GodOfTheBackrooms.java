package net.epiphany.mdlrbckrms;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

/**
 * The inner machinations of my mind are an enigma.
 */
public class GodOfTheBackrooms {
    /**
     * The one and only
     */
    private static final String HIM = "DarkLordDudeALTA";

    /**
     * A means of identification.
     * 
     * @param entity The entity to test.
     * @return The truth that may now be seen.
     */
    public static boolean isHim(Entity entity) {
        return entity instanceof PlayerEntity player && HIM.equals(player.getEntityName());
    }
}
