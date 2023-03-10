package net.epiphany.mdlrbckrms;

import net.epiphany.mdlrbckrms.blocks.MBBlocks;
import net.epiphany.mdlrbckrms.entities.MBEntities;
import net.epiphany.mdlrbckrms.items.MBItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * Client proxy for registering client-side only portions of Modular Backrooms.
 */
@Environment(EnvType.CLIENT)
public class ModularBackroomsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MBItems.registerItemPredicates();
        MBBlocks.registerColorProviders();
        MBEntities.registerEntityRenderers();
    }    
}
