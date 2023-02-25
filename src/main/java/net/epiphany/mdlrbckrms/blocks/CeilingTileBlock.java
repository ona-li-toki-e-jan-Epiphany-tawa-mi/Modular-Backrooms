package net.epiphany.mdlrbckrms.blocks;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

/**
 * The ceiling tiles that appear in Level 0.
 */
public class CeilingTileBlock {
    /**
     * Player craftable and destroyable variant of the tiles.
     */
    public static final Identifier CEILING_TILE_ID = new Identifier(ModularBackrooms.MOD_ID, "ceiling_tile");
    public static final Block CEILING_TILE = new Block(
            FabricBlockSettings.of(Material.WOOL).strength(0.8f).sounds(BlockSoundGroup.WOOL));
    public static final BlockItem CEILING_TILE_ITEM = new BlockItem(CEILING_TILE, new FabricItemSettings());



    /**
     * Player craftable and destroyable variant of the tiles.
     */
    public static final Identifier UNBREAKABLE_CEILING_TILE_ID = 
            new Identifier(ModularBackrooms.MOD_ID, "unbreakable_ceiling_tile");
    public static final Block UNBREAKABLE_CEILING_TILE = new Block(
            FabricBlockSettings.of(Material.WOOL).strength(Blocks.UNBREAKABLE, Blocks.UNBLASTABLE).sounds(BlockSoundGroup.WOOL));
    public static final BlockItem UNBREAKABLE_CEILING_TILE_ITEM = 
            new BlockItem(UNBREAKABLE_CEILING_TILE, new FabricItemSettings());



    public static void register() {
        Registry.register(Registries.BLOCK, CEILING_TILE_ID, CEILING_TILE);
        Registry.register(Registries.ITEM, CEILING_TILE_ID, CEILING_TILE_ITEM);
        FlammableBlockRegistry.getDefaultInstance().add(CEILING_TILE, 30, 60);

        Registry.register(Registries.BLOCK, UNBREAKABLE_CEILING_TILE_ID, UNBREAKABLE_CEILING_TILE);
        Registry.register(Registries.ITEM, UNBREAKABLE_CEILING_TILE_ID, UNBREAKABLE_CEILING_TILE_ITEM);
    }

    public static void registerBlockItemUnderGroup(FabricItemGroupEntries content) {
        content.add(CEILING_TILE_ITEM);
        content.add(UNBREAKABLE_CEILING_TILE_ITEM);
    }
}
