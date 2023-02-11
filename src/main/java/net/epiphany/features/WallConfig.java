package net.epiphany.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.FeatureConfig;

public record WallConfig(Identifier blockID) implements FeatureConfig {
    public static Codec<WallConfig> CODEC = RecordCodecBuilder.create(
        instance ->
                instance.group(Identifier.CODEC.fieldOf("blockID").forGetter(WallConfig::blockID))
                        .apply(instance, WallConfig::new));

    public WallConfig(Identifier blockID) {
        this.blockID = blockID;
    }
 
    

    public Identifier blockID() {
        return blockID;
    }
}