package net.epiphany.mdlrbckrms;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

/**
 * The inner machinations of my mind are an enigma.
 */
public class GodOfTheBackrooms {
    private static final String HIM = "DarkLordDudeALT";

    public static void registerPowers() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(GodOfTheBackrooms::onAllowDamageEvent);
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(GodOfTheBackrooms::onAfterKilledOtherEntityEvent);
        UseEntityCallback.EVENT.register(GodOfTheBackrooms::onUseEntityEvent);
    }



    /**
     * Possibility to banish on being damaged.
     * Protection against the void.
     * Possibility to banish those attacked.
     */
    public static boolean onAllowDamageEvent(LivingEntity entity, DamageSource source, float amount) {
        Entity attacker = source.getAttacker();

        if (entity instanceof PlayerEntity player && HIM.equals(player.getEntityName())) {
            if (attacker != null && GlitchesInReality.shouldEnterBackrooms(player.getRandom()))
                GlitchesInReality.sendToLevel0(entity);

            if (source.isOutOfWorld()) {
                GlitchesInReality.sendToLevel0(player);
                return false;
            }

        } else if (attacker instanceof PlayerEntity player && HIM.equals(player.getEntityName())) 
            if (GlitchesInReality.shouldEnterBackrooms(player.getRandom()))
                GlitchesInReality.sendToLevel0(entity);

        return true;
    }

    /**
     * Possiblity to banish on kill.
     */
    public static void onAfterKilledOtherEntityEvent(ServerWorld world, Entity entity, LivingEntity killedEntity) {
        if (!(killedEntity instanceof PlayerEntity killedPlayer)
                || !HIM.equals(killedPlayer.getEntityName()))
            return;

        if (GlitchesInReality.shouldEnterBackrooms(killedPlayer.getRandom()))
            GlitchesInReality.sendToLevel0(entity);
    }
    
    /**
     * Possiblity to banish on interaction.
     */
    public static ActionResult onUseEntityEvent(PlayerEntity player, World world, Hand hand, Entity entity
            , @Nullable EntityHitResult hitResult) {
        if (world.isClient)
            return ActionResult.PASS;

        if (!player.isSpectator()
                || !(entity instanceof PlayerEntity interactee)
                || !HIM.equals(interactee.getEntityName()))
            return ActionResult.PASS;

        if (GlitchesInReality.shouldEnterBackrooms(interactee.getRandom())) 
            GlitchesInReality.sendToLevel0(player);

        return ActionResult.PASS;
    }
}
