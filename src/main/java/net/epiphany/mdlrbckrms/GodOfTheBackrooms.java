package net.epiphany.mdlrbckrms;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

/**
 * The inner machinations of my mind are an enigma.
 */
public class GodOfTheBackrooms {
    public static void registerPowers() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(GodOfTheBackrooms::onAllowDamageEvent);
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
     * Possibility to banish on being damaged.
     * Protection against the void.
     * Possibility to banish those attacked.
     */
    public static boolean onAllowDamageEvent(LivingEntity entity, DamageSource source, float amount) {
        Entity attacker = source.getAttacker();

        if (entity instanceof PlayerEntity player && isHim(player)) {
            if (attacker != null && GlitchesInReality.shouldEnterBackrooms(player.getRandom()))
                GlitchesInReality.sendToLevel0(attacker);

            if (source.isOutOfWorld() && GlitchesInReality.isInVoid(player)) {
                GlitchesInReality.sendToLevel0(player);
                return false;
            }

        } else if (attacker instanceof PlayerEntity playerAttacker && isHim(playerAttacker)) 
            if (GlitchesInReality.shouldEnterBackrooms(playerAttacker.getRandom())) {
                GlitchesInReality.sendToLevel0(entity);
                return false;
            }

        return true;
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
