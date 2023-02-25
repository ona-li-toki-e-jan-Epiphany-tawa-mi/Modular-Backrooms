package net.epiphany.mdlrbckrms;

import net.epiphany.mdlrbckrms.blocks.Blocks;
import net.epiphany.mdlrbckrms.features.Features;
import net.epiphany.mdlrbckrms.items.Items;
import net.epiphany.mdlrbckrms.levels.Levels;
import net.epiphany.mdlrbckrms.utilities.Sounds;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO List:
 * - Better textures.
 * - Make block models depend on eachother where possible.
 * - Exits and "exits."
 * - Make player-crafted wallpaper flammable and burning it leaves behind sandstone.
 * - Hallucinations.
 * - Do something special if a player tries to sleep.
 * - Add randomly generated rifts.
 * - Debug placement of divider walls and walled doors (walled doors can face into walls and divider walls can spawn butted up to chunk walls.)
 * - Advancements:
 * 	* Enter Backrooms.
 * 	* Kill chicken with chicken.
 *  * Catch chicken.
 *  * Kill player or boss mob with chicken.
 *  * Drink "water."
 * 
 * - Make player craftable and destroyable versions of the blocks used in level 0 with the following recepies:
 * Fluorescent Light (makes 16?):
 * 	IRI
 *  GEG
 *  -G-
 *  > I - iron ingot, G - glass, E - end rod, R - purugorbeb rebbentuk.
 * 
 * purugorbeb rebbentuk:
 *  GCG
 *  G-G
 *  GPG
 *  G - kruvvumev, C - burubelti, P - pressure plate.
 * vitelorbeb burubeltibul:
 *  --R
 *  DSG
 *  T-T
 *  R - purugorbeb rebbentuk, D - trapdoor, S - kruv, G - jeebeb xlemor!ek)), T - seltuk
 * 
 * - Add discovery method for recipes using destroyed labs and lab notes.
 * - (Possibly) Make non-linear space with immersive portals.
 * - (Possibly) change placement of walls and openings through time
 * - Give self the ability to open rifts between worlds and display cool particle effects.
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
