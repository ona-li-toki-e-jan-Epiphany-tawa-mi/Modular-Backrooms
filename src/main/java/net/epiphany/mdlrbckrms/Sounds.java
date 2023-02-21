package net.epiphany.mdlrbckrms;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

/**
 * Common methods and fields for all custom sounds.
 */
public class Sounds {
    /**
     * A constant drone of fluorescent lights used in levels with them present.
     */
    public static final Identifier MAXIMUM_HUM_BUZZ_ID = new Identifier(ModularBackrooms.MOD_ID, "maximum_hum_buzz");
    public static final SoundEvent MAXIMUM_HUM_BUZZ = SoundEvent.of(MAXIMUM_HUM_BUZZ_ID);

    /**
     * The flickering of a fluorescent light.
     */
    public static final Identifier FLUORESCENT_FLICKER_ID = new Identifier(ModularBackrooms.MOD_ID, "fluorescent_flicker");
    public static final SoundEvent FLUORESCENT_FLICKER = SoundEvent.of(FLUORESCENT_FLICKER_ID);

    /**
     * A completely empty sound used to prevent music from playing.
     */
    public static final Identifier NULL_SOUND_ID = new Identifier(ModularBackrooms.MOD_ID, "null_sound");
    public static final SoundEvent NULL_SOUND = SoundEvent.of(NULL_SOUND_ID);



    /**
     * Registers all custom blocks.
     */
    public static void registerSounds() {
        ModularBackrooms.LOGGER.debug("Registering sounds");

		Registry.register(Registries.SOUND_EVENT, MAXIMUM_HUM_BUZZ_ID, MAXIMUM_HUM_BUZZ);
        Registry.register(Registries.SOUND_EVENT, FLUORESCENT_FLICKER_ID, FLUORESCENT_FLICKER);
        Registry.register(Registries.SOUND_EVENT, NULL_SOUND_ID, NULL_SOUND);

        ModularBackrooms.LOGGER.debug("Sound registration complete");
    }
}
