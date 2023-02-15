package net.epiphany.mdlrbckrms.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.FeatureConfig;

/**
 * Configuration options for walled doors.
 */
public record WalledDoorConfig(Identifier doorBlockID) implements FeatureConfig {
    public static Codec<WalledDoorConfig> CODEC = RecordCodecBuilder.create(
        instance ->
                instance.group( Identifier.CODEC.fieldOf("doorBlockID").forGetter(WalledDoorConfig::doorBlockID))
                        .apply(instance, WalledDoorConfig::new));

    /**
     * @param doorBlockID The ID of the door block to use.
     */
    public WalledDoorConfig(Identifier doorBlockID) {
       this.doorBlockID = doorBlockID;
    }

    /**
     * @return The ID of the door block to use.
     */
    public Identifier doorID() {
        return doorBlockID;
    }
}