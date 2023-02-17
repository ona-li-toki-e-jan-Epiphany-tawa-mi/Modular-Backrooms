package net.epiphany.mdlrbckrms.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.FeatureConfig;

/**
 * Configuration options for walled doors.
 */
public record WalledDoorConfig(Identifier doorBlockID, float openChance) implements FeatureConfig {
    public static Codec<WalledDoorConfig> CODEC = RecordCodecBuilder.create(
        instance ->
                instance.group( Identifier.CODEC.fieldOf("doorBlockID").forGetter(WalledDoorConfig::doorBlockID)
                              , Codec.FLOAT.fieldOf("openChance").forGetter(WalledDoorConfig::openChance))
                        .apply(instance, WalledDoorConfig::new));

    /**
     * @param doorBlockID The ID of the door block to use.
     * @param openChance  The chance that the door is generated being open.
     */
    public WalledDoorConfig(Identifier doorBlockID, float openChance) {
       this.doorBlockID = doorBlockID;
       this.openChance = openChance;
    }
}