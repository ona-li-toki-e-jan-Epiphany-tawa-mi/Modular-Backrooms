package net.epiphany.mdlrbckrms.utilities;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

/**
 * Common methods and fields for all custom sounds.
 */
public class MBSounds {
    /**
     * A constant drone of fluorescent lights used in levels with them present.
     */
    public static final SoundEvent MAXIMUM_HUM_BUZZ = SoundEvent.of(new Identifier(ModularBackrooms.MOD_ID, "ambient.mdlrbckrms.maximum_hum_buzz"));
    /**
     * The flickering of a fluorescent light.
     */
    public static final SoundEvent FLUORESCENT_FLICKER = SoundEvent.of(new Identifier(ModularBackrooms.MOD_ID, "block.mdlrbckrms.fluorescent_light.flicker"));
    /**
     * A completely empty sound used to prevent music from playing.
     */
    public static final SoundEvent NULL_SOUND = SoundEvent.of(new Identifier(ModularBackrooms.MOD_ID, "mdlrbckrms.null_sound"));
    /**
     * Creepy door creaking sounds.
     */
    public static final SoundEvent DOOR_CREAKS = SoundEvent.of(new Identifier(ModularBackrooms.MOD_ID, "block.mdlrbckrms.office_door.creak"));
    /**
     * Random creepy sounds to use as mood sounds and auditory hallucinations.
     */
    public static final SoundEvent CREEPY_SOUNDS = SoundEvent.of(new Identifier(ModularBackrooms.MOD_ID, "mood.mdlrbckrms.creepy_sounds"));



    /**
     * Registers all custom blocks.
     */
    public static void registerSounds() {
		registerSoundEvent(MAXIMUM_HUM_BUZZ);
        registerSoundEvent(FLUORESCENT_FLICKER);
        registerSoundEvent(NULL_SOUND);
        registerSoundEvent(DOOR_CREAKS);
        registerSoundEvent(CREEPY_SOUNDS);
    }



    /**
     * Registers a sound event.
     * 
     * @param soundEvent The sound event.
     * @return The sound event, for chaining.
     */
    public static SoundEvent registerSoundEvent(SoundEvent soundEvent) {
        return Registry.register(Registries.SOUND_EVENT, soundEvent.getId(), soundEvent);
    }
}
