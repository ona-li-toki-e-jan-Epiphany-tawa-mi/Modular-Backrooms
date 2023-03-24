package net.epiphany.mdlrbckrms;

import net.epiphany.mdlrbckrms.blocks.MBBlocks;
import net.epiphany.mdlrbckrms.entities.MBEntities;
import net.epiphany.mdlrbckrms.items.MBItems;
import net.epiphany.mdlrbckrms.levels.Levels;
import net.epiphany.mdlrbckrms.utilities.MBSounds;
import net.epiphany.mdlrbckrms.worldgen.MBWorldGeneration;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
TODO Add these to level 0 and fix biome distribution to "feel right"
TODO Decrease hallucination spawn rates in open spaces or just remove them entirely.
 {
                            "biome": "mdlrbckrms:level_0/pillars",
                            "parameters": {
                                "temperature": 0.2,
                                "humidity": 0.7,
                                "continentalness": 0.3,
                                "erosion": 0,
                                "weirdness": 0.55,
                                "depth": 0,
                                "offset": 0
                            }
                        },
                        {
                            "biome": "mdlrbckrms:level_0/dark_pillars",
                            "parameters": {
                                "temperature": 0.1,
                                "humidity": 0.7,
                                "continentalness": 0.3,
                                "erosion": 0,
                                "weirdness": 1,
                                "depth": 0,
                                "offset": 0
                            }
                        }
 */

/**
 * TODO List:
 * - Fix odd placment of fluorescent lights at biome borders.
 * - Make player-crafted wallpaper flammable and burning it leaves behind sandstone.
 * - Debug placement of divider walls and walled doors (walled doors can face into walls and divider walls can spawn butted up to chunk walls.)
 *
 * viteltukorbeb burubelbul:
 *  --R
 *  DSG
 *  T-T
 *  R - purugtukorbeb rebben, D - trapdoor, S - kruv, G - jeebeb xlemor!ek)), T - selti
 * 
 * - Add discovery method for recipes using destroyed labs and lab notes.
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
		MBWorldGeneration.registerWordGenerationStuffs();

		MBSounds.registerSounds();

		GlitchesInReality.registerGlitches();
	}
}
