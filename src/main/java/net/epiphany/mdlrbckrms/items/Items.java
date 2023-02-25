package net.epiphany.mdlrbckrms.items;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.epiphany.mdlrbckrms.blocks.Blocks;
import net.epiphany.mdlrbckrms.blocks.YellowedWallpaper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
                       .icon(() -> new ItemStack(YellowedWallpaper.YELLOWED_WALLPAPER_ITEM))
                       .build();

    /**
     * Registers all custom non-block items.
     */
    public static void registerItems() {
        ModularBackrooms.LOGGER.debug("Registering items");

        ChickenItem.register();
        ChickenSnatcherItem.register();

        ItemGroupEvents.modifyEntriesEvent(BACKROOMS_ITEM_GROUP).register(Items::registerItemUnderGroup);

        ModularBackrooms.LOGGER.debug("Item registration complete");
    }

    /**
     * Registers the Backrooms items under their item group for the creative menu.
     */
    private static void registerItemUnderGroup(FabricItemGroupEntries content) {
        ChickenItem.registerItemUnderGroup(content);
        ChickenSnatcherItem.registerItemUnderGroup(content);

        Blocks.registerBlockItemUnderGroup(content);
    }

    /**
     * Registers custom item predicates for dynamic item models.
     */
    @Environment(EnvType.CLIENT)
    public static void registerItemPredicates() {
        ChickenItemPredicate.register();
    }
}
