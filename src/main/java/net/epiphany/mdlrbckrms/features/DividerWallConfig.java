package net.epiphany.mdlrbckrms.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.FeatureConfig;

/**
 * Configuration options for divider walls.
 */
public record DividerWallConfig(Identifier mainBlockID, Identifier topBlockID, int minimumHeight, int maximumHeight
        , int minimumLength, int maximumLength, float faceSouthChance) implements FeatureConfig {
    public static Codec<DividerWallConfig> CODEC = RecordCodecBuilder.create(
        instance ->
                instance.group( Identifier.CODEC.fieldOf("mainBlockID").forGetter(DividerWallConfig::mainBlockID)
                              , Identifier.CODEC.fieldOf("topBlockID").forGetter(DividerWallConfig::topBlockID)
                              , Codec.INT.fieldOf("minHeight").forGetter(DividerWallConfig::minimumHeight)
                              , Codec.INT.fieldOf("maxHeight").forGetter(DividerWallConfig::maximumHeight)
                              , Codec.INT.fieldOf("minLength").forGetter(DividerWallConfig::minimumLength)
                              , Codec.INT.fieldOf("maxLength").forGetter(DividerWallConfig::maximumLength)
                              , Codec.FLOAT.fieldOf("faceSouthChance").forGetter(DividerWallConfig::faceSouthChance))
                        .apply(instance, DividerWallConfig::new));

    /**
     * @param mainBlockID     The ID of the block to make most of the wall out of.
     * @param topBlockID      The ID of the block to make the top of the wall out of.
     * @param minimumHeight   The minimum height of the wall.
     * @param maximumHeight   The maximum height of the wall.
     * @param minimumLength   The minimum length of the wall.
     * @param maximumLength   The maximum length of the wall.
     * @param faceSouthChance The chance that the door should face south instead of east.
     */
    public DividerWallConfig(Identifier mainBlockID, Identifier topBlockID, int minimumHeight, int maximumHeight
            , int minimumLength, int maximumLength, float faceSouthChance) {
        this.mainBlockID     = mainBlockID;
        this.topBlockID      = topBlockID;
        this.minimumHeight   = minimumHeight;
        this.maximumHeight   = maximumHeight;
        this.minimumLength   = minimumLength;
        this.maximumLength   = maximumLength;
        this.faceSouthChance = faceSouthChance;
    }

    /**
     * @return The ID of the block to make most of the wall out of.
     */
    public Identifier mainBlockID() {
        return mainBlockID;
    }

    /**
     * @return The ID of the block to make the top of the wall out of.
     */
    public Identifier topBlockID() {
        return topBlockID;
    }

    /**
     * @return The minimum height of the wall.
     */
    public int minimumHeight() {
        return minimumHeight;
    }

    /**
     * @return The maximum height of the wall.
     */
    public int maximumHeight() {
        return maximumHeight;
    }

    /**
     * @return The minimum length of the wall.
     */
    public int minimumLength() {
        return minimumLength;
    }

    /**
     * @return The maximum length of the wall.
     */
    public int maximumLength() {
        return maximumLength;
    }

    /**
     * @return The chance that the door should face south instead of east.
     */
    public float faceSouthChance() {
        return faceSouthChance;
    }
}