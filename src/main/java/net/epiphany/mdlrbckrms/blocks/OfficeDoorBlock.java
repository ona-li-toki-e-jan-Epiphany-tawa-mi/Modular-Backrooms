package net.epiphany.mdlrbckrms.blocks;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

//TODO Add custom sound.

/**
 * Office doors that appear throught level 0.
 */
public class OfficeDoorBlock {
    /**
     * Player craftable and destroyable variant of the doors.
     */
    public static final Identifier OFFICE_DOOR_ID = new Identifier(ModularBackrooms.MOD_ID, "office_door");
    public static final DoorBlock OFFICE_DOOR = 
        new DoorBlock( FabricBlockSettings.of(Material.WOOD).strength(3).sounds(BlockSoundGroup.WOOD)
                     , SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK);
    public static final BlockItem OFFICE_DOOR_ITEM = new BlockItem(OFFICE_DOOR, new FabricItemSettings());



    /**
     * Indestructable variant of the doors.
     */
    public static final Identifier UNBREAKABLE_OFFICE_DOOR_ID = 
            new Identifier(ModularBackrooms.MOD_ID, "unbreakable_office_door");
    public static final DoorBlock UNBREAKABLE_OFFICE_DOOR = 
            new DoorBlock( FabricBlockSettings.of(Material.WOOD)
                                              .strength(Blocks.UNBREAKABLE, Blocks.UNBLASTABLE)
                                              .sounds(BlockSoundGroup.WOOD)
                         , SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK);
    public static final BlockItem UNBREAKABLE_OFFICE_DOOR_ITEM = 
            new BlockItem(UNBREAKABLE_OFFICE_DOOR, new FabricItemSettings());

    

    public static void register() {
        Registry.register(Registries.BLOCK, OFFICE_DOOR_ID, OFFICE_DOOR);
        Registry.register(Registries.ITEM, OFFICE_DOOR_ID, OFFICE_DOOR_ITEM);

        Registry.register(Registries.BLOCK, UNBREAKABLE_OFFICE_DOOR_ID, UNBREAKABLE_OFFICE_DOOR);
        Registry.register(Registries.ITEM, UNBREAKABLE_OFFICE_DOOR_ID, UNBREAKABLE_OFFICE_DOOR_ITEM);
    }

    public static void registerBlockItemUnderGroup(FabricItemGroupEntries content) {
        content.add(OFFICE_DOOR_ITEM);
        content.add(UNBREAKABLE_OFFICE_DOOR_ITEM);
    }
}
