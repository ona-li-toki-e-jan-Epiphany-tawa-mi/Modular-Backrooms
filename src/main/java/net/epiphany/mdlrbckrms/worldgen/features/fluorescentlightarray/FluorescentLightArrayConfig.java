package net.epiphany.mdlrbckrms.worldgen.features.fluorescentlightarray;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.FeatureConfig;

/**
 * Configuration options for fluorescent light arrays. 
 */
public record FluorescentLightArrayConfig(Identifier lightBlockID, int length, int columns, int rows, int xSpacing, int zSpacing) 
        implements FeatureConfig {
    public static Codec<FluorescentLightArrayConfig> CODEC = RecordCodecBuilder.create((instance) ->
                instance.group( Identifier.CODEC.fieldOf("lightBlockID").forGetter(FluorescentLightArrayConfig::lightBlockID)
                              , Codec.INT.fieldOf("length").forGetter(FluorescentLightArrayConfig::length)
                              , Codec.INT.fieldOf("columns").forGetter(FluorescentLightArrayConfig::columns)
                              , Codec.INT.fieldOf("rows").forGetter(FluorescentLightArrayConfig::rows)
                              , Codec.INT.fieldOf("xSpacing").forGetter(FluorescentLightArrayConfig::xSpacing)
                              , Codec.INT.fieldOf("zSpacing").forGetter(FluorescentLightArrayConfig::zSpacing))
                        .apply(instance, FluorescentLightArrayConfig::new));

    /**
     * @param blockID  The ID of the block to use as lights.
     * @param length   How long the light is (aligned with z-axis.)
     * @param columns  How many columns of lights to make (aligned with x-axis.)
     * @param rows     How many rows of lights to make (aligned with z-axis.)
     * @param xSpacing How far apart the lights should be on the x-axis.
     * @param zSpacing How far apart the lights should be on the z-axis.
     */
    public FluorescentLightArrayConfig(Identifier lightBlockID, int length, int columns, int rows, int xSpacing, int zSpacing) {
        this.lightBlockID = lightBlockID;
        this.length = length;
        this.columns = columns;
        this.rows = rows;
        this.xSpacing = xSpacing;
        this.zSpacing = zSpacing;
    }
}