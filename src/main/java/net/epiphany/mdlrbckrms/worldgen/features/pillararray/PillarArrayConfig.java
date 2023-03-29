package net.epiphany.mdlrbckrms.worldgen.features.pillararray;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.FeatureConfig;

/**
 * Configuration options for pillar arrays. 
 */
public record PillarArrayConfig(Identifier blockID, int length, int height, int columns, int rows, int xSpacing, int zSpacing) 
        implements FeatureConfig {
    public static Codec<PillarArrayConfig> CODEC = RecordCodecBuilder.create((instance) ->
                instance.group( Identifier.CODEC.fieldOf("blockID").forGetter(PillarArrayConfig::blockID)
                              , Codec.INT.fieldOf("length").forGetter(PillarArrayConfig::length)
                              , Codec.INT.fieldOf("height").forGetter(PillarArrayConfig::height)
                              , Codec.INT.fieldOf("columns").forGetter(PillarArrayConfig::columns)
                              , Codec.INT.fieldOf("rows").forGetter(PillarArrayConfig::rows)
                              , Codec.INT.fieldOf("xSpacing").forGetter(PillarArrayConfig::xSpacing)
                              , Codec.INT.fieldOf("zSpacing").forGetter(PillarArrayConfig::zSpacing))
                        .apply(instance, PillarArrayConfig::new));

    /**
     * @param blockID  The ID of the block to make the pillars out of.
     * @param length   The length of the pillars (square in shape.)
     * @param height   How high to make the pillars.
     * @param columns  How many columns of pillars to make (aligned with x-axis.)
     * @param rows     How many rows of pillars to make (aligned with z-axis.)
     * @param xSpacing How far apart the pillars should be on the x-axis.
     * @param zSpacing How far apart the pillars should be on the z-axis.
     */
    public PillarArrayConfig(Identifier blockID, int length, int height, int columns, int rows, int xSpacing, int zSpacing) {
        this.blockID = blockID;
        this.length = length;
        this.height = height;
        this.columns = columns;
        this.rows = rows;
        this.xSpacing = xSpacing;
        this.zSpacing = zSpacing;
    }
}