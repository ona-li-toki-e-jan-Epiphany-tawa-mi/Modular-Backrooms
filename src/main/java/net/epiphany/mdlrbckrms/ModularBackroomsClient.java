package net.epiphany.mdlrbckrms;

import net.epiphany.mdlrbckrms.items.Items;
import net.fabricmc.api.ClientModInitializer;

/**
 * Client proxy for registering client-side only portions of Modular Backrooms.
 */
public class ModularBackroomsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Items.registerItemPredicates();
    }    
}
