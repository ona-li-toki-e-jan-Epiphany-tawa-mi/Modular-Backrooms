package net.epiphany.mdlrbckrms.worldgen.features.rift;

import com.mojang.serialization.Codec;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.epiphany.mdlrbckrms.blocks.MBBlocks;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.util.FeatureContext;

/**
 * Used to generate random rifts in worlds.
 */
public class RiftFeature extends Feature<RiftConfig> {
    public RiftFeature(Codec<RiftConfig> configCodec) {
        super(configCodec);
    }



    @Override
    public boolean generate(FeatureContext<RiftConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();

        if (!world.getBlockState(origin).isReplaceable())
            return false;


        float axisChance = MathHelper.clamp(context.getConfig().axisChance(), 0.0f, 1.0f);
        Direction.Axis axis = context.getRandom().nextFloat() < axisChance ? Direction.Axis.X : Direction.Axis.Z;

        ModularBackrooms.LOGGER.info(origin.toString());

        world.setBlockState(origin, MBBlocks.RIFT.getDefaultState().with(NetherPortalBlock.AXIS, axis), 0);

        return true;
    }



    public static final RegistryKey<PlacedFeature> RANDOM_RIFT = PlacedFeatures.of(ModularBackrooms.MOD_ID + ":random_rift");

    /**
     * Allows the rifts to appear anywhere in any dimension in the world.
     */
    public static void addToAllBiomes() {
        BiomeModifications.addFeature(BiomeSelectors.all(), GenerationStep.Feature.TOP_LAYER_MODIFICATION, RANDOM_RIFT);
    }
}
