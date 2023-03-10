package net.epiphany.mdlrbckrms;

import net.epiphany.mdlrbckrms.blocks.MBBlocks;
import net.epiphany.mdlrbckrms.entities.MBEntities;
import net.epiphany.mdlrbckrms.features.MBFeatures;
import net.epiphany.mdlrbckrms.items.MBItems;
import net.epiphany.mdlrbckrms.levels.Levels;
import net.epiphany.mdlrbckrms.utilities.MBSounds;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO List:
 * - Exits and "exits."
 * - Add more variation to level 0.
 * - Make player-crafted wallpaper flammable and burning it leaves behind sandstone.
 * - Do something special if a player tries to sleep.
 * - Add randomly generated rifts.
 * - Debug placement of divider walls and walled doors (walled doors can face into walls and divider walls can spawn butted up to chunk walls.)
 * - Advancements:
 * 	* Enter Backrooms.
 * 	* Kill chicken with chicken.
 *  * Catch chicken.
 *  * Kill player or boss mob with chicken.
 *  * Drink "water."
 *  * Place a RNG.
 *
 * viteltukorbeb burubelbul:
 *  --R
 *  DSG
 *  T-T
 *  R - purugtukorbeb rebben, D - trapdoor, S - kruv, G - jeebeb xlemor!ek)), T - selti
 * 
 * - Add discovery method for recipes using destroyed labs and lab notes.
 * - (Possibly) change placement of walls and openings through time
 * - Give self the ability to open rifts between worlds and display cool particle effects.
 */
public class ModularBackrooms implements ModInitializer {
	public static final String MOD_ID = "mdlrbckrms";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		MBItems.registerItems();
		MBBlocks.registerBlocks();
		MBEntities.registerEntities();

		Levels.registerLevels();
		MBFeatures.registerFeatures();

		MBSounds.registerSounds();

		GodOfTheBackrooms.registerPowers();
		GlitchesInReality.registerGlitches();
		
	}
}
