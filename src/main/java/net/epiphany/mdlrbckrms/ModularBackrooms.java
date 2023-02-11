package net.epiphany.mdlrbckrms;

import net.epiphany.features.ChunkWallFeature;
import net.epiphany.mdlrbckrms.level0.Level0;
import net.epiphany.mdlrbckrms.level0.Level0ChunkGenerator;
import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModularBackrooms implements ModInitializer {
	public static final String MOD_ID = "mdlrbckrms";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		Registry.register(Registries.FEATURE, ChunkWallFeature.WALL_ID, ChunkWallFeature.WALL_FEATURE);
		Registry.register(Registries.CHUNK_GENERATOR, Level0.CHUNK_GENERATOR_ID, Level0ChunkGenerator.CODEC);
	}
}
