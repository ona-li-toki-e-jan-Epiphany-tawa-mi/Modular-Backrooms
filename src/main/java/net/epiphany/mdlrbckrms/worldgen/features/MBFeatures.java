package net.epiphany.mdlrbckrms.worldgen.features;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.epiphany.mdlrbckrms.worldgen.features.chunkwall.ChunkWallConfig;
import net.epiphany.mdlrbckrms.worldgen.features.chunkwall.ChunkWallFeature;
import net.epiphany.mdlrbckrms.worldgen.features.dividerwall.DividerWallConfig;
import net.epiphany.mdlrbckrms.worldgen.features.dividerwall.DividerWallFeature;
import net.epiphany.mdlrbckrms.worldgen.features.fluorescentlightarray.FluorescentLightArrayConfig;
import net.epiphany.mdlrbckrms.worldgen.features.fluorescentlightarray.FluorescentLightArrayFeature;
import net.epiphany.mdlrbckrms.worldgen.features.pillararray.PillarArrayConfig;
import net.epiphany.mdlrbckrms.worldgen.features.pillararray.PillarArrayFeature;
import net.epiphany.mdlrbckrms.worldgen.features.rift.RiftConfig;
import net.epiphany.mdlrbckrms.worldgen.features.rift.RiftFeature;
import net.epiphany.mdlrbckrms.worldgen.features.torch.TorchConfig;
import net.epiphany.mdlrbckrms.worldgen.features.torch.TorchFeature;
import net.epiphany.mdlrbckrms.worldgen.features.walleddoor.WalledDoorConfig;
import net.epiphany.mdlrbckrms.worldgen.features.walleddoor.WalledDoorFeature;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

/**
 * Common methods for all custom features.
 */
public class MBFeatures {
    public static final Feature<ChunkWallConfig> CHUNK_WALL = new ChunkWallFeature(ChunkWallConfig.CODEC);
    public static final Feature<DividerWallConfig> DIVIDER_WALL = new DividerWallFeature(DividerWallConfig.CODEC);
    public static final Feature<FluorescentLightArrayConfig> FLUORESCENT_LIGHT_ARRAY = new FluorescentLightArrayFeature(FluorescentLightArrayConfig.CODEC);
    public static final Feature<WalledDoorConfig> WALLED_DOOR = new WalledDoorFeature(WalledDoorConfig.CODEC);
    public static final Feature<RiftConfig> RIFT = new RiftFeature(RiftConfig.CODEC);
    public static final Feature<PillarArrayConfig> PILLAR_ARRRAY = new PillarArrayFeature(PillarArrayConfig.CODEC);
    public static final Feature<TorchConfig> TORCH = new TorchFeature(TorchConfig.CODEC);



    /**
     * Registers all custom features.
     */
    public static void registerFeatures() {
        registerFeature("chunk_wall", CHUNK_WALL);
        registerFeature("divider_wall", DIVIDER_WALL);
        registerFeature("fluorescent_light_array", FLUORESCENT_LIGHT_ARRAY);
        registerFeature("walled_door", WALLED_DOOR);
        registerFeature("rift", RIFT);
        registerFeature("pillar_array", PILLAR_ARRRAY);
        registerFeature("torch", TORCH);

        RiftFeature.addToAllBiomes();
    }
    
    /**
     * Registers a feature.
     * 
     * @param <C>     The configuration type of the feature.
     * @param idPath  The path of the block's ID (do not include namespace, it will do it for you.)
     * @param feature The feature.
     * @return
     */
    public static <C extends FeatureConfig> Feature<C> registerFeature(String idPath, Feature<C> feature) {
        return Registry.register(Registries.FEATURE, new Identifier(ModularBackrooms.MOD_ID, idPath), feature);
    }

    

    /**
     * Condition selector for choosing how {@link MBFeatures#testPillar(WorldAccess, BlockPos, int, PillarCondition)} operates.
     */
    public static enum PillarCondition {
        /**
         * Checks for a completely solid pillar of blocks.
         */
        SOILD,
        /**
         * Checks for a pillar that is completely made of replacable blocks.
         */
        REPLACABLE
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
                    if (!world.getBlockState(pillarOrigin).isSolidBlock(world, pillarOrigin)) 
                        return false;
                    break;

                case REPLACABLE:
                    if (!world.getBlockState(pillarOrigin).isReplaceable())
                        return false;
                    break;
            }

            pillarOrigin = pillarOrigin.up();
        }

        return true;
    }
}
