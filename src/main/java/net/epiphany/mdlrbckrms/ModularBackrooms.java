package net.epiphany.mdlrbckrms;

import net.epiphany.mdlrbckrms.blocks.MBBlocks;
import net.epiphany.mdlrbckrms.entities.MBEntities;
import net.epiphany.mdlrbckrms.items.MBItems;
import net.epiphany.mdlrbckrms.levels.Levels;
import net.epiphany.mdlrbckrms.networking.LeftClickPacket;
import net.epiphany.mdlrbckrms.utilities.MBSounds;
import net.epiphany.mdlrbckrms.worldgen.MBWorldGeneration;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO List:
 * - Make player-crafted wallpaper flammable and burning it leaves behind sandstone.
 * - viteltukorag burubelbul
 * - Have opening and closing of office doors use distinct sounds.
 * - Add discovery method for recipes using destroyed labs and lab notes.
 * - Give self the ability to open rifts between worlds and display cool particle effects.
 */
public class ModularBackrooms implements ModInitializer {
	public static final String MOD_ID = "mdlrbckrms";
	public static final String MINECRAFT_NAMESPACE = "minecraft";
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

		LeftClickPacket.registerServerListener();
	}
}
