package net.epiphany.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.FeatureConfig;

public record ChunkWallConfig(Identifier blockID, int height, float wallChance, float doorChance) implements FeatureConfig {
    public static Codec<ChunkWallConfig> CODEC = RecordCodecBuilder.create(
        instance ->
                instance.group( Identifier.CODEC.fieldOf("blockID").forGetter(ChunkWallConfig::blockID)
                              , Codec.INT.fieldOf("height").forGetter(ChunkWallConfig::height)
                              , Codec.FLOAT.fieldOf("wallChance").forGetter(ChunkWallConfig::wallChance)
                              , Codec.FLOAT.fieldOf("doorChance").forGetter(ChunkWallConfig::doorChance))
                        .apply(instance, ChunkWallConfig::new));

    public ChunkWallConfig(Identifier blockID, int height, float wallChance, float doorChance) {
        this.blockID = blockID;
        this.height = height;
        this.wallChance = wallChance;
        this.doorChance = doorChance;
    }

    public Identifier blockID() {
        return blockID;
    }

    public int height() {
        return height;
    }

    public float wallChance() {
        return wallChance;
    }

    public float doorChance() {
        return doorChance;
    }
}