package net.epiphany.mdlrbckrms.utilities;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.HungerManager;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;

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
        
        if (!player.isCreative()) {
            // onDeath doesn't clear experience.
            player.setExperienceLevel(0);
            player.setExperiencePoints(0);
        }
        // Nor does it clear status effects.
        player.clearStatusEffects();
        // Nor does it reset hunger.
        HungerManager hungerManager = player.getHungerManager();
        hungerManager.setFoodLevel(20);
        hungerManager.setSaturationLevel(5.0f);
    }



    /**
     * Attempts to find a safe position to place the entity within the given chunk.
     * 
     * @param world The world the chunk is in.
     * @param chunk The position of the chunk.
     * @param entity The entity to place there.
     * @return A safe position, or {@code null}, if one could not be found.
     */
    @Nullable
    public static Vec3d findValidPosition(World world, ChunkPos chunk, Entity entity) {
        int minimumY = world.getBottomY()
          , maximumY = world.getTopY();

        BlockPos.Mutable possibleDestination = new BlockPos.Mutable();
        double x = Double.NaN, y = Double.NaN, z = Double.NaN;
        boolean foundSafePosition = false;

    FindSafePosition:
        for (int i = chunk.getStartX(); i <= chunk.getEndX(); i++)
            for (int j = chunk.getStartZ(); j <= chunk.getEndZ(); j++) 
                for (int k = minimumY; k < maximumY; k++) {
                    possibleDestination.set(i, k, j);
                
                    if (SpawnHelper.canSpawn( SpawnRestriction.Location.ON_GROUND
                                            , world, possibleDestination
                                            , entity.getType())) {
                        x = possibleDestination.getX();
                        y = possibleDestination.getY();
                        z = possibleDestination.getZ();
                                                
                        foundSafePosition = true;
                        break FindSafePosition;
                    }
                }

        return foundSafePosition ? new Vec3d(x + 0.5, y + 0.5, z + 0.5) : null;
    }
}