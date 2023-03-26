package net.epiphany.mdlrbckrms.worldgen.features.torch;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.FeatureConfig;

/**
 * Configuration options for torch features.
 */
public record TorchConfig(Identifier torchBlockID, Identifier wallTorchBlockID) implements FeatureConfig {
    public static Codec<TorchConfig> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group( Identifier.CODEC.fieldOf("torchBlockID").forGetter(TorchConfig::torchBlockID)
                          , Identifier.CODEC.fieldOf("wallTorchBlockID").forGetter(TorchConfig::wallTorchBlockID))
                    .apply(instance, TorchConfig::new));

    /**
     * @param torchBlockID The ID of the torch block to use.
     * @param wallTorchBlockID The ID of the wall torch block to use (torches placed on walls use a different block and thus 
     *      different id.)
     */
    public TorchConfig(Identifier torchBlockID, Identifier wallTorchBlockID) {
        this.torchBlockID = torchBlockID;
        this.wallTorchBlockID = wallTorchBlockID;
    }
}
