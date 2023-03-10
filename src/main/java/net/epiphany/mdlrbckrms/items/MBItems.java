package net.epiphany.mdlrbckrms.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.epiphany.mdlrbckrms.blocks.MBBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

/**
 * Common methods and fields for all custom non-block items.
 */
public class MBItems {
    public static final ItemGroup BACKROOMS_ITEM_GROUP = 
        FabricItemGroup.builder(new Identifier(ModularBackrooms.MOD_ID, "backrooms_item_group"))
                       .icon(() -> new ItemStack(MBBlocks.YELLOWED_WALLPAPER))
                       .build();

    /**
     * Registers the Backrooms items under their item group for the creative menu.
     * TODO Note: if there are preformance issues when opening the creative inventory for the first time with a lot of mods, you 
     *      know why.
     */
    private static void registerItemUnderGroup(FabricItemGroupEntries content) {
        List<Item> items = new ArrayList<>();

        // Possibly sketchy scan through the item registry to find all Modular Backrooms items and adding them to the creative menu.
        // Nothing could possibly go wrong here, right? ;)
        for (Map.Entry<RegistryKey<Item>, Item> entry : Registries.ITEM.getEntrySet()) 
            if (ModularBackrooms.MOD_ID.equals(entry.getKey().getValue().getNamespace()))
                items.add(entry.getValue());

        Collections.sort(items, (item1, item2) -> Item.getRawId(item1) - Item.getRawId(item2));

        for (Item item : items) 
            content.add(item.getDefaultStack());
    }


    
    public static final ChickenSnatcherItem CHICKEN_SNATCHER = new ChickenSnatcherItem(ToolMaterials.WOOD, new FabricItemSettings().maxDamage(64));
    public static final ChickenItem CHICKEN = new ChickenItem(new FabricItemSettings().maxCount(1));
    public static final SuspicousWaterItem SUSPICOUS_WATER = new SuspicousWaterItem(new FabricItemSettings());



    /**
     * Registers custom items.
     */
    public static void registerItems() {
        registerItem("chicken_snatcher", CHICKEN_SNATCHER);
        registerItem("chicken", CHICKEN);
        registerItem("suspicous_water", SUSPICOUS_WATER);

        CompostingChanceRegistry.INSTANCE.add(CHICKEN, 1.0f); // Compostable chickens ;)

        ItemGroupEvents.modifyEntriesEvent(BACKROOMS_ITEM_GROUP).register(MBItems::registerItemUnderGroup);
    }

    /**
     * Registers custom item predicates for dynamic item models.
     */
    @Environment(EnvType.CLIENT)
    public static void registerItemPredicates() {
        ChickenItemPredicate.register();
    }

    

    /**
     * Registers an item.
     * 
     * @param <I>    The item type.
     * @param idPath The path of the item's ID (do not include namespace, it will do it for you.) 
     * @param item   The item.
     * @return The item, for chaining.
     */
    public static <I extends Item> I registerItem(String idPath, I item) {
        return Registry.register(Registries.ITEM, new Identifier(ModularBackrooms.MOD_ID, idPath), item);
    }  
}
