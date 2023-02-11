package net.epiphany.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.FeatureConfig;

/**
 * Configuration options for ceiling light arrays.
 */
public record CeilingLightArrayConfig(Identifier blockID, int length, int columns, int rows) implements FeatureConfig {
    public static Codec<CeilingLightArrayConfig> CODEC = RecordCodecBuilder.create(
        instance ->
                instance.group( Identifier.CODEC.fieldOf("blockID").forGetter(CeilingLightArrayConfig::blockID)
                              , Codec.INT.fieldOf("length").forGetter(CeilingLightArrayConfig::length)
                              , Codec.INT.fieldOf("columns").forGetter(CeilingLightArrayConfig::columns)
                              , Codec.INT.fieldOf("rows").forGetter(CeilingLightArrayConfig::rows))
                        .apply(instance, CeilingLightArrayConfig::new));

    /**
     * @param blockID The ID of the block to use as lights.
     * @param length  How long the light is (aligned with x-axis.)
     * @param columns How many columns of lights to make.
     * @param rows    How many rows of lights to make.
     */
    public CeilingLightArrayConfig(Identifier blockID, int length, int columns, int rows) {
        this.blockID = blockID;
        this.length = length;
        this.columns = columns;
        this.rows = rows;
    }

    /**
     * @return The ID of the block to use as lights.
     */
    public Identifier blockID() {
        return blockID;
    }

    /**
     * @return How long the light is (aligned with x-axis.)
     */
    public int length() {
        return length;
    }

    /**
     * @return How many columns of lights to make.
     */
    public int columns() {
        return columns;
    }

    /**
     * @return How many rows of lights to make.
     */
    public int rows() {
        return rows;
    }
}