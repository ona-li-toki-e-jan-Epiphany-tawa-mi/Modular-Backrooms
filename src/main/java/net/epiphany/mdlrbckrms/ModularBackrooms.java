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
 * - (Possibly) Make non-linear space with immersive portals.
 * - (Possibly) change placement of walls and openings through time.
 * - Add randomly generated rifts.
 * - Give self the ability to open rifts between worlds and display cool particle effects.
 * - Prevent ender portals from being able to open in backrooms. (Hint: EnterEyeItem)
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
		GodOfTheBackrooms.registerPowers();
		GlitchesInReality.registerGlitches();
	}
}
