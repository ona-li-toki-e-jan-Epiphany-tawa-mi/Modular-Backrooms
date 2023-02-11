package net.epiphany.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.FeatureConfig;

/**
 * Configuration options for chunk walls.
 */
public record SubWallConfig(Identifier blockID, Identifier topBlockID, int minimumHeight, int maximumHeight, int minimumLength, 
        int maximumLength, float faceSouthChance) implements FeatureConfig {
    public static Codec<SubWallConfig> CODEC = RecordCodecBuilder.create(
        instance ->
                instance.group( Identifier.CODEC.fieldOf("blockID").forGetter(SubWallConfig::blockID)
                              , Identifier.CODEC.fieldOf("topBlockID").forGetter(SubWallConfig::topBlockID)
                              , Codec.INT.fieldOf("minHeight").forGetter(SubWallConfig::minimumHeight)
                              , Codec.INT.fieldOf("maxHeight").forGetter(SubWallConfig::maximumHeight)
                              , Codec.INT.fieldOf("minLength").forGetter(SubWallConfig::minimumLength)
                              , Codec.INT.fieldOf("maxLength").forGetter(SubWallConfig::maximumLength)
                              , Codec.FLOAT.fieldOf("faceSouthChance").forGetter(SubWallConfig::faceSouthChance))
                        .apply(instance, SubWallConfig::new));

    /**
     * @param blockID    The ID of the block to make the wall out of.
     * @param height     The height of the wall.
     */
    public SubWallConfig(Identifier blockID, Identifier topBlockID, int minimumHeight, int maximumHeight, int minimumLength, 
            int maximumLength, float faceSouthChance) {
        this.blockID         = blockID;
        this.topBlockID      = topBlockID;
        this.minimumHeight   = minimumHeight;
        this.maximumHeight   = maximumHeight;
        this.minimumLength   = minimumLength;
        this.maximumLength   = maximumLength;
        this.faceSouthChance = faceSouthChance;
    }

    /**
     * @return The ID of the block to make the wall out of.
     */
    public Identifier blockID() {
        return blockID;
    }

    public Identifier topBlockID() {
        return topBlockID;
    }

    /**
     * @return The height of the wall.
     */
    public int minimumHeight() {
        return minimumHeight;
    }

    public int maximumHeight() {
        return maximumHeight;
    }

    public int minimumLength() {
        return minimumLength;
    }

    public int maximumLength() {
        return maximumLength;
    }

    public float faceSouthChance() {
        return faceSouthChance;
    }
}