package net.epiphany.mdlrbckrms;

import org.jetbrains.annotations.Nullable;

import net.epiphany.mdlrbckrms.blocks.rift.RiftEvents;
import net.epiphany.mdlrbckrms.levels.Levels;
import net.epiphany.mdlrbckrms.levels.level0.Level0;
import net.epiphany.mdlrbckrms.utilities.DimensionHelper;
import net.epiphany.mdlrbckrms.utilities.MiscellaneousHelpers;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

/**
 * Contains the code responsible for allowing players to glitch through reality into the Backrooms
 */
public class GlitchesInReality {
    /**
     * Registers the listeners needed to create the "glitches".
     */
    public static void registerGlitches() {
        ServerLivingEntityEvents.ALLOW_DEATH.register(GlitchesInReality::onAllowDeathEvent);
        RiftEvents.ON_ENTITY_ENTER_RIFT.register(GlitchesInReality::onEntityEnterRift);
    }



    /**
     * Sends the player to the Backrooms on death by the void, preventing it and giving them some health back (between 20-40% 
     *  of their max.) Additionally, prevents players from leaving backrooms by dying.
     */
    private static boolean onAllowDeathEvent(LivingEntity entity, DamageSource damageSource, float damageAmount) {
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
        } else if (damageSource.isOf(DamageTypes.OUT_OF_WORLD) && DimensionHelper.isInVoid(player)) { 
            player.setHealth(player.getMaxHealth() * (random.nextFloat() * 0.2f + 0.2f));
            DimensionHelper.teleportToDimension(player, Level0.LEVEL_0, random);

            return false;
        }
        
       return true; 
    }



    /**
     * Sends those entering rifts from non-backrooms dimensions into Level 0.
     */
    @Nullable
    private static RegistryKey<World> onEntityEnterRift(World world, BlockPos position, Entity entity) {
        if (!Levels.isBackrooms(world)) 
            return Level0.LEVEL_0;

        return null;
    }
}
