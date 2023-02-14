package net.epiphany.mdlrbckrms.blocks;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

/**
 * Common methods for all custom blocks.
 */
public class Blocks {
    /**
     * Registers all custom blocks.
     */
    public static void registerBlocks() {
        ModularBackrooms.LOGGER.debug("Registering blocks and block items");

        Registry.register(Registries.BLOCK, CeilingLight.CEILING_LIGHT_ID, CeilingLight.CEILING_LIGHT);
        Registry.register(Registries.ITEM, CeilingLight.CEILING_LIGHT_ID, new BlockItem(CeilingLight.CEILING_LIGHT, new FabricItemSettings()));

        ModularBackrooms.LOGGER.debug("Block and block item registration complete");
    }
}
