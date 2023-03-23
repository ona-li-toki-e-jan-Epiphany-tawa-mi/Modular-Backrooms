package net.epiphany.mdlrbckrms.worldgen.features.dividerwall;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.FeatureConfig;

/**
 * Configuration options for divider walls.
 */
public record DividerWallConfig(Identifier mainBlockID, Identifier topBlockID, int minimumHeight, int maximumHeight
        , int minimumLength, int maximumLength) implements FeatureConfig {
    public static Codec<DividerWallConfig> CODEC = RecordCodecBuilder.create((instance) ->
                instance.group( Identifier.CODEC.fieldOf("mainBlockID").forGetter(DividerWallConfig::mainBlockID)
                              , Identifier.CODEC.fieldOf("topBlockID").forGetter(DividerWallConfig::topBlockID)
                              , Codec.INT.fieldOf("minHeight").forGetter(DividerWallConfig::minimumHeight)
                              , Codec.INT.fieldOf("maxHeight").forGetter(DividerWallConfig::maximumHeight)
                              , Codec.INT.fieldOf("minLength").forGetter(DividerWallConfig::minimumLength)
                              , Codec.INT.fieldOf("maxLength").forGetter(DividerWallConfig::maximumLength))
                        .apply(instance, DividerWallConfig::new));

    /**
     * @param mainBlockID     The ID of the block to make most of the wall out of.
     * @param topBlockID      The ID of the block to make the top of the wall out of.
     * @param minimumHeight   The minimum height of the wall.
     * @param maximumHeight   The maximum height of the wall.
     * @param minimumLength   The minimum length of the wall.
     * @param maximumLength   The maximum length of the wall.
     */
    public DividerWallConfig(Identifier mainBlockID, Identifier topBlockID, int minimumHeight, int maximumHeight
            , int minimumLength, int maximumLength) {
        this.mainBlockID     = mainBlockID;
        this.topBlockID      = topBlockID;
        this.minimumHeight   = minimumHeight;
        this.maximumHeight   = maximumHeight;
        this.minimumLength   = minimumLength;
        this.maximumLength   = maximumLength;
    }
}