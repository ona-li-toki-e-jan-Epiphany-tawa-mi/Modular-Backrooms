package net.epiphany.mdlrbckrms;

import net.epiphany.mdlrbckrms.blocks.Blocks;
import net.epiphany.mdlrbckrms.entities.Entities;
import net.epiphany.mdlrbckrms.items.Items;
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
        Items.registerItemPredicates();
        Blocks.registerColorProviders();
        Entities.registerEntityRenderers();
    }    
}
