package net.epiphany.mdlrbckrms.items;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.epiphany.mdlrbckrms.blocks.Blocks;
import net.epiphany.mdlrbckrms.blocks.FluorescentLight;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

/**
 * Common methods and fields for all custom non-block items.
 */
public class Items {
    public static final Identifier BACKROOMS_ITEM_GROUP_ID = new Identifier(ModularBackrooms.MOD_ID, "backrooms_item_group");
    private static final ItemGroup BACKROOMS_ITEM_GROUP = 
        FabricItemGroup.builder(BACKROOMS_ITEM_GROUP_ID)
                       .icon(() -> new ItemStack(FluorescentLight.FLUORESCENT_LIGHT_ITEM))
                       .build();

    /**
     * Registers all custom non-block items.
     */
    public static void registerItems() {
        ModularBackrooms.LOGGER.debug("Registering items");

        BurubeltiJee.register();

        ItemGroupEvents.modifyEntriesEvent(BACKROOMS_ITEM_GROUP).register(Items::registerItemUnderGroup);

        ModularBackrooms.LOGGER.debug("Item registration complete");
    }

    /**
     * Registers the Backrooms items under their item group for the creative menu.
     */
    private static void registerItemUnderGroup(FabricItemGroupEntries content) {
        BurubeltiJee.registerItemUnderGroup(content);

        Blocks.registerBlockItemUnderGroup(content);
    }
}
