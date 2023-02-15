package net.epiphany.mdlrbckrms.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.FeatureConfig;

/**
 * Configuration options for chunk walls.
 */
public record ChunkWallConfig(Identifier blockID, int height, float wallChance, float doorChance) implements FeatureConfig {
    public static Codec<ChunkWallConfig> CODEC = RecordCodecBuilder.create(
        instance ->
                instance.group( Identifier.CODEC.fieldOf("blockID").forGetter(ChunkWallConfig::blockID)
                              , Codec.INT.fieldOf("height").forGetter(ChunkWallConfig::height)
                              , Codec.FLOAT.fieldOf("wallChance").forGetter(ChunkWallConfig::wallChance)
                              , Codec.FLOAT.fieldOf("doorChance").forGetter(ChunkWallConfig::doorChance))
                        .apply(instance, ChunkWallConfig::new));

    /**
     * @param blockID    The ID of the block to make the wall out of.
     * @param height     The height of the wall.
     * @param wallChance The chance of generating the south and east ends of the chunk wall, applied individually.
     * @param doorChance The chance that, if the wall generates, that an opening will be generated in it.
     */
    public ChunkWallConfig(Identifier blockID, int height, float wallChance, float doorChance) {
        this.blockID = blockID;
        this.height = height;
        this.wallChance = wallChance;
        this.doorChance = doorChance;
    }

    /**
     * @return The ID of the block to make the wall out of.
     */
    public Identifier blockID() {
        return blockID;
    }

    /**
     * @return The height of the wall.
     */
    public int height() {
        return height;
    }

    /**
     * @return The chance of generating the south and east ends of the chunk wall, applied individually.
     */
    public float wallChance() {
        return wallChance;
    }

    /**
     * @return The chance that, if the wall generates, that an opening will be generated in it.
     */
    public float doorChance() {
        return doorChance;
    }
}