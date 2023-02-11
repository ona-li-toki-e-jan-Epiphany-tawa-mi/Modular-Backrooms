package net.epiphany.mdlrbckrms;

import net.epiphany.features.CeilingLightArrayFeature;
import net.epiphany.features.ChunkWallFeature;
import net.epiphany.features.SubWallFeature;
import net.epiphany.mdlrbckrms.level0.Level0ChunkGenerator;
import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModularBackrooms implements ModInitializer {
	public static final String MOD_ID = "mdlrbckrms";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		Registry.register(Registries.FEATURE, ChunkWallFeature.WALL_ID, ChunkWallFeature.WALL_FEATURE);
		Registry.register(Registries.FEATURE, CeilingLightArrayFeature.LIGHT_ARRAY_ID, CeilingLightArrayFeature.LIGHT_ARRAY_FEATURE);
		Registry.register(Registries.FEATURE, SubWallFeature.SUBWALL_ID, SubWallFeature.SUBWALL_FEATURE);

		Registry.register(Registries.CHUNK_GENERATOR, Level0ChunkGenerator.CHUNK_GENERATOR_ID, Level0ChunkGenerator.CODEC);

		final Identifier MAXIMUM_HUM_BUZZ_ID = new Identifier(MOD_ID, "maximum_hum_buzz");
		Registry.register(Registries.SOUND_EVENT, MAXIMUM_HUM_BUZZ_ID, SoundEvent.of(MAXIMUM_HUM_BUZZ_ID));
	}
}
