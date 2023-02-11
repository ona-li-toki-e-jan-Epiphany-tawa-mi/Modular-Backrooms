package net.epiphany.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.FeatureConfig;

public record CeilingLightArrayConfig(Identifier blockID, int length, int columns, int rows) implements FeatureConfig {
    public static Codec<CeilingLightArrayConfig> CODEC = RecordCodecBuilder.create(
        instance ->
                instance.group( Identifier.CODEC.fieldOf("blockID").forGetter(CeilingLightArrayConfig::blockID)
                              , Codec.INT.fieldOf("length").forGetter(CeilingLightArrayConfig::length)
                              , Codec.INT.fieldOf("columns").forGetter(CeilingLightArrayConfig::columns)
                              , Codec.INT.fieldOf("rows").forGetter(CeilingLightArrayConfig::rows))
                        .apply(instance, CeilingLightArrayConfig::new));

    public CeilingLightArrayConfig(Identifier blockID, int length, int columns, int rows) {
        this.blockID = blockID;
        this.length = length;
        this.columns = columns;
        this.rows = rows;
    }

    public Identifier blockID() {
        return blockID;
    }

    public int length() {
        return length;
    }

    public int columns() {
        return columns;
    }

    public int rows() {
        return rows;
    }
}