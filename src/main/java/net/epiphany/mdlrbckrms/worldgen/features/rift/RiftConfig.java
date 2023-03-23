package net.epiphany.mdlrbckrms.worldgen.features.rift;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.gen.feature.FeatureConfig;

/**
 * Configuration options for rifts.
 */
public record RiftConfig(float axisChance) implements FeatureConfig {
    public static Codec<RiftConfig> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(Codec.FLOAT.fieldOf("axis_chance").forGetter(RiftConfig::axisChance))
                    .apply(instance, RiftConfig::new));   

    /**
     * @param axisChance Compared against a random number between 0 and 1. If lower, the rift will be aligned to the x axis, if higher,
     *      to the z axis instead.
     */
    public RiftConfig(float axisChance) {
        this.axisChance = axisChance;
    }
}
