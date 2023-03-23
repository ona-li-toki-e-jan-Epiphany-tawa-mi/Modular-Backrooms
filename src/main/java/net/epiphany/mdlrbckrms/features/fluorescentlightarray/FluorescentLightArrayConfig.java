package net.epiphany.mdlrbckrms.features.fluorescentlightarray;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.FeatureConfig;

/**
 * Configuration options for fluorescent light arrays.
 */
public record FluorescentLightArrayConfig(Identifier lightBlockID, int length, int columns, int rows) implements FeatureConfig {
    public static Codec<FluorescentLightArrayConfig> CODEC = RecordCodecBuilder.create((instance) ->
                instance.group( Identifier.CODEC.fieldOf("lightBlockID").forGetter(FluorescentLightArrayConfig::lightBlockID)
                              , Codec.INT.fieldOf("length").forGetter(FluorescentLightArrayConfig::length)
                              , Codec.INT.fieldOf("columns").forGetter(FluorescentLightArrayConfig::columns)
                              , Codec.INT.fieldOf("rows").forGetter(FluorescentLightArrayConfig::rows))
                        .apply(instance, FluorescentLightArrayConfig::new));

    /**
     * @param blockID The ID of the block to use as lights.
     * @param length  How long the light is (aligned with x-axis.)
     * @param columns How many columns of lights to make.
     * @param rows    How many rows of lights to make.
     */
    public FluorescentLightArrayConfig(Identifier lightBlockID, int length, int columns, int rows) {
        this.lightBlockID = lightBlockID;
        this.length = length;
        this.columns = columns;
        this.rows = rows;
    }
}