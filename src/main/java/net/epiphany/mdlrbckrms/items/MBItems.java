package net.epiphany.mdlrbckrms.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.epiphany.mdlrbckrms.blocks.MBBlocks;
import net.epiphany.mdlrbckrms.items.predicates.ChickenItemPredicate;
import net.epiphany.mdlrbckrms.items.predicates.PredicatebebViteloragBurubelbul;
import net.epiphany.mdlrbckrms.utilities.LeftClickEvents;
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
import net.minecraft.util.Identifier;

/**
 * Common methods and fields for all custom non-block items.
 */
public class MBItems {
    /**
     * Stores the items to load into the backrooms item group.
     */
    public static final List<Item> items = new ArrayList<>();

    public static final ChickenSnatcherItem CHICKEN_SNATCHER = new ChickenSnatcherItem(ToolMaterials.WOOD, new FabricItemSettings().maxDamage(64));
    public static final ChickenItem CHICKEN = new ChickenItem(new FabricItemSettings().maxCount(1));
    public static final SuspicousWaterItem SUSPICOUS_WATER = new SuspicousWaterItem(new FabricItemSettings());
    public static final Item BURUBEL_VITELTUK = new Item(new FabricItemSettings().maxCount(4));
    public static final ViteltukoragBurubelbul VITELTUKORAG_BURUBELBUL = new ViteltukoragBurubelbul(new FabricItemSettings().maxDamage(256));



    /**
     * Registers custom items.
     */
    public static void registerItems() {
        registerItem("chicken_snatcher", CHICKEN_SNATCHER, true);
        registerItem("chicken", CHICKEN, true);
        registerItem("suspicous_water", SUSPICOUS_WATER, true);
        registerItem("burubel_viteltuk", BURUBEL_VITELTUK, false);
        registerItem("viteltukorag_burubelbul", VITELTUKORAG_BURUBELBUL, false);

        CompostingChanceRegistry.INSTANCE.add(CHICKEN, 1.0f); // Compostable chickens ;)

        LeftClickEvents.ON_VALID_LEFT_CLICK.register(ViteltukoragBurubelbul::onLeftClick);

        ItemGroupEvents.modifyEntriesEvent(BACKROOMS_ITEM_GROUP).register(MBItems::registerItemsUnderGroup);
    }

    /**
     * Registers custom item predicates for dynamic item models.
     */
    @Environment(EnvType.CLIENT)
    public static void registerItemPredicates() {
        ChickenItemPredicate.register();
        PredicatebebViteloragBurubelbul.register();
    }



    public static final ItemGroup BACKROOMS_ITEM_GROUP = 
        FabricItemGroup.builder(new Identifier(ModularBackrooms.MOD_ID, "backrooms_item_group"))
                       .icon(() -> new ItemStack(MBBlocks.YELLOWED_WALLPAPER))
                       .build();

    /**
     * Registers the Backrooms items under their item group for the creative menu.
     */
    private static void registerItemsUnderGroup(FabricItemGroupEntries content) {
        Collections.sort(items, (item1, item2) -> Item.getRawId(item1) - Item.getRawId(item2));

        for (Item item : items) 
            content.add(item.getDefaultStack());
    }

    

    /**
     * Registers an item.
     * 
     * @param <I>            The item type.
     * @param idPath         The path of the item's ID (do not include namespace, it will do it for you.) 
     * @param item           The item.
     * @param addToItemGroup Whether to add the item to the backrooms item group.
     * @return The item, for chaining.
     */
    public static <I extends Item> I registerItem(String idPath, I item, boolean addToItemGroup) {
        if (addToItemGroup)
            items.add(item);

        return Registry.register(Registries.ITEM, new Identifier(ModularBackrooms.MOD_ID, idPath), item);
    }  
}
