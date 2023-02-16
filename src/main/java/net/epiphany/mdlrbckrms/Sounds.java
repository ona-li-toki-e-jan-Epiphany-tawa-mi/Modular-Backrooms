package net.epiphany.mdlrbckrms;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

/**
 * Common methods and fields for all custom sounds.
 */
public class Sounds {
    public static final Identifier MAXIMUM_HUM_BUZZ_ID = new Identifier(ModularBackrooms.MOD_ID, "maximum_hum_buzz");
    public static final SoundEvent MAXIMUM_HUM_BUZZ = SoundEvent.of(MAXIMUM_HUM_BUZZ_ID);

    public static final Identifier FLUORESCENT_FLICKER_ID = new Identifier(ModularBackrooms.MOD_ID, "fluorescent_flicker");
    public static final SoundEvent FLUORESCENT_FLICKER = SoundEvent.of(FLUORESCENT_FLICKER_ID);

    /**
     * Registers all custom blocks.
     */
    public static void registerSounds() {
        ModularBackrooms.LOGGER.debug("Registering sounds");

		Registry.register(Registries.SOUND_EVENT, MAXIMUM_HUM_BUZZ_ID, MAXIMUM_HUM_BUZZ);
        Registry.register(Registries.SOUND_EVENT, FLUORESCENT_FLICKER_ID, FLUORESCENT_FLICKER);

        ModularBackrooms.LOGGER.debug("Sound registration complete");
    }
}
