package net.epiphany.mdlrbckrms;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

/**
 * The inner machinations of my mind are an enigma.
 */
public class GodOfTheBackrooms {
    public static void registerPowers() {
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(GodOfTheBackrooms::onAfterKilledOtherEntityEvent);
    }



    /**
     * The one and only
     */
    public static final String HIM = "DarkLordDudeALT";

    /**
     * A means of identification.
     * 
     * @param entity The entity to test.
     * @return The truth that may now be seen.
     */
    public static boolean isHim(Entity entity) {
        return entity instanceof PlayerEntity player && HIM.equals(player.getEntityName());
    }



    /**
     * Possiblity to banish on kill.
     */
    public static void onAfterKilledOtherEntityEvent(ServerWorld world, Entity entity, LivingEntity killedEntity) {
        if (!(killedEntity instanceof PlayerEntity killedPlayer)
                || !isHim(killedEntity))
            return;

        if (GlitchesInReality.shouldEnterBackrooms(killedPlayer.getRandom()))
            GlitchesInReality.sendToLevel0(entity);
    }
}
