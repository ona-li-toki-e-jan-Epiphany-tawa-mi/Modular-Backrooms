package net.epiphany.mdlrbckrms.blocks;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;

/**
 * Common methods and fields for all custom blocks and block items.
 */
public class Blocks {
    /**
     * A really high blast resistance for blocks that cannot be blown up.
     */
    public static final float UNBLASTABLE = 8960000.0f;
    /**
     * A resistance value such that a block cannot be broken by mining.
     */
    public static final float UNBREAKABLE = -1.0f;

    /**
     * Registers all custom blocks and block items.
     */
    public static void registerBlocks() {
        ModularBackrooms.LOGGER.debug("Registering blocks");

        FluorescentLight.register();
        YellowedWallpaper.register();
        CeilingTile.register();

        /*Registry.register(Registries.BLOCK, ExitDoor.EXIT_DOOR_ID, ExitDoor.EXIT_DOOR);
        Registry.register(Registries.ITEM, ExitDoor.EXIT_DOOR_ID, ExitDoor.EXIT_DOOR_ITEM);*/

        ModularBackrooms.LOGGER.debug("Block registration complete");
    }

    /**
     * Registers the Backrooms block items under their item group for the creative menu.
     */
    public static void registerBlockItemUnderGroup(FabricItemGroupEntries content) {
        FluorescentLight.registerBlockItemUnderGroup(content);
        YellowedWallpaper.registerBlockItemUnderGroup(content);
        CeilingTile.registerBlockItemUnderGroup(content);
    }
}
