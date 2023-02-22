package net.epiphany.mdlrbckrms.features;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

/**
 * Common methods for all custom features.
 */
public class Features {
    /**
     * Registers all custom features.
     */
    public static void registerFeatures() {
        ModularBackrooms.LOGGER.debug("Registering features");

        ChunkWallFeature.register();
        FluorescentLightArrayFeature.register();
        DividerWallFeature.register();
        WalledDoorFeature.register();

        ModularBackrooms.LOGGER.debug("Feature registration complete");
    }

    

    /**
     * Condition selector for choosing how {@link Features#testPillar(WorldAccess, BlockPos, int, PillarCondition)} operates.
     */
    public static enum PillarCondition {
        /**
         * Checks for a completely solid (non-air) pillar of blocks.
         */
        SOILD,
        /**
         * Checks for a pillar that is completely made of air.
         */
        AIR
    }

    /**
     * Tests to see if a pillar of blocks meet the given condition.
     * 
     * @param world        The world the pillar is in,.
     * @param pillarOrigin The position of the bottom block of the pillar.
     * @param height       The height of the pillar.
     * @param condition    The condition to check for.
     * @return {@code true}, if the pillar meets the condition.
     */
    public static boolean testPillar(WorldAccess world, BlockPos pillarOrigin, int height, PillarCondition condition) {
        for (int i = 0; i < height; i++) {
            switch (condition) {
                case SOILD:
                    if (world.getBlockState(pillarOrigin).isAir()) 
                        return false;
                    break;

                case AIR:
                    if (!world.getBlockState(pillarOrigin).isAir())
                        return false;
                    break;
            }

            pillarOrigin = pillarOrigin.up();
        }

        return true;
    }
}
