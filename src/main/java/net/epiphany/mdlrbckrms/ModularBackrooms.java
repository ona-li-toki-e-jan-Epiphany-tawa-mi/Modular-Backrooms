package net.epiphany.mdlrbckrms;

import net.epiphany.mdlrbckrms.blocks.Blocks;
import net.epiphany.mdlrbckrms.features.Features;
import net.epiphany.mdlrbckrms.items.Items;
import net.epiphany.mdlrbckrms.levels.Levels;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO List:
 * - Better textures.
 * - Exits and "exits."
 * - Hallucinations.
 * - Prevent Minecraft's music from playing in Backrooms.
 * - Do something special if a player tries to sleep.
 * - Make non-linear space with immersive portals.
 * - (Possibly) change placement of walls and openings through time.
 * - Add entrances to Backrooms.
 */

public class ModularBackrooms implements ModInitializer {
	public static final String MOD_ID = "mdlrbckrms";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		Levels.registerLevels();
		Features.registerFeatures();
		Blocks.registerBlocks();
		Sounds.registerSounds();
		Items.registerItems();
	}
}
