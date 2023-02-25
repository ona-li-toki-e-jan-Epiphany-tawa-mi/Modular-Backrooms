package net.epiphany.mdlrbckrms.blocks;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * The yellowed wallpaper that appears on the walls of Level 0 in block form.
 */
public class YellowedWallpaper  {
    /**
     * Player craftable and destroyable variant of the wallpaper.
     */
    public static final Identifier YELLOWED_WALLPAPER_ID = new Identifier(ModularBackrooms.MOD_ID, "yellowed_wallpaper");
    public static final Block YELLOWED_WALLPAPER = new Block(
            FabricBlockSettings.of(Material.STONE).strength(0.8f).requiresTool());
    public static final BlockItem YELLOWED_WALLPAPER_ITEM = new BlockItem(YELLOWED_WALLPAPER, new FabricItemSettings());

    /**
     * Slab variant of the player craftable wallpaper.
     */
    public static final Identifier YELLOWED_WALLPAPER_SLAB_ID = new Identifier( ModularBackrooms.MOD_ID
                                                                              , "yellowed_wallpaper_slab");
    public static final SlabBlock YELLOWED_WALLPAPER_SLAB = new SlabBlock(
            FabricBlockSettings.copyOf(YELLOWED_WALLPAPER));
    public static final BlockItem YELLOWED_WALLPAPER_SLAB_ITEM = new BlockItem( YELLOWED_WALLPAPER_SLAB
                                                                              , new FabricItemSettings());

    /**
     * Stairs variant of the player craftable wallpaper.
     */
    public static final Identifier YELLOWED_WALLPAPER_STAIRS_ID = new Identifier( ModularBackrooms.MOD_ID
                                                                              , "yellowed_wallpaper_stairs");
    public static final StairsBlock YELLOWED_WALLPAPER_STAIRS = 
            new StairsBlock( YELLOWED_WALLPAPER.getDefaultState()
                           , FabricBlockSettings.copyOf(YELLOWED_WALLPAPER));
    public static final BlockItem YELLOWED_WALLPAPER_STAIRS_ITEM = new BlockItem( YELLOWED_WALLPAPER_STAIRS
                                                                                , new FabricItemSettings());

    /**
     * Wall variant of the player craftable wallpaper.
     */
    public static final Identifier YELLOWED_WALLPAPER_WALL_ID = new Identifier( ModularBackrooms.MOD_ID
                                                                              , "yellowed_wallpaper_wall");
    public static final WallBlock YELLOWED_WALLPAPER_WALL = 
            new WallBlock(FabricBlockSettings.copyOf(YELLOWED_WALLPAPER));
    public static final BlockItem YELLOWED_WALLPAPER_WALL_ITEM = new BlockItem( YELLOWED_WALLPAPER_WALL
                                                                              , new FabricItemSettings());



    /**
     * Indestructable variant of the wallpaper.
     */
    public static final Identifier UNBREAKABLE_YELLOWED_WALLPAPER_ID = 
            new Identifier(ModularBackrooms.MOD_ID, "unbreakable_yellowed_wallpaper");
    public static final Block UNBREAKABLE_YELLOWED_WALLPAPER = new Block(
            FabricBlockSettings.of(Material.STONE).strength(Blocks.UNBREAKABLE, Blocks.UNBLASTABLE));
    public static final BlockItem UNBREAKABLE_YELLOWED_WALLPAPER_ITEM = new BlockItem( UNBREAKABLE_YELLOWED_WALLPAPER
                                                                                     , new FabricItemSettings());

    /**
     * Slab variant of the indestructable wallpaper.
     */
    public static final Identifier UNBREAKABLE_YELLOWED_WALLPAPER_SLAB_ID = 
            new Identifier( ModularBackrooms.MOD_ID, "unbreakable_yellowed_wallpaper_slab");
    public static final SlabBlock UNBREAKABLE_YELLOWED_WALLPAPER_SLAB = new SlabBlock(
            FabricBlockSettings.copyOf(UNBREAKABLE_YELLOWED_WALLPAPER));
    public static final BlockItem UNBREAKABLE_YELLOWED_WALLPAPER_SLAB_ITEM = new BlockItem(
            UNBREAKABLE_YELLOWED_WALLPAPER_SLAB, new FabricItemSettings());

    /**
     * Stairs variant of the indestructable wallpaper.
     */
    public static final Identifier UNBREAKABLE_YELLOWED_WALLPAPER_STAIRS_ID = 
            new Identifier( ModularBackrooms.MOD_ID, "unbreakable_yellowed_wallpaper_stairs");
    public static final StairsBlock UNBREAKABLE_YELLOWED_WALLPAPER_STAIRS = 
            new StairsBlock( UNBREAKABLE_YELLOWED_WALLPAPER.getDefaultState()
                           , FabricBlockSettings.copyOf(UNBREAKABLE_YELLOWED_WALLPAPER));
    public static final BlockItem UNBREAKABLE_YELLOWED_WALLPAPER_STAIRS_ITEM = 
            new BlockItem(UNBREAKABLE_YELLOWED_WALLPAPER_STAIRS, new FabricItemSettings());

    /**
     * Wall variant of the indestructable wallpaper.
     */
    public static final Identifier UNBREAKABLE_YELLOWED_WALLPAPER_WALL_ID = 
        new Identifier(ModularBackrooms.MOD_ID, "unbreakable_yellowed_wallpaper_wall");
    public static final WallBlock UNBREAKABLE_YELLOWED_WALLPAPER_WALL = 
            new WallBlock(FabricBlockSettings.copyOf(UNBREAKABLE_YELLOWED_WALLPAPER));
    public static final BlockItem UNBREAKABLE_YELLOWED_WALLPAPER_WALL_ITEM = 
            new BlockItem( UNBREAKABLE_YELLOWED_WALLPAPER_WALL, new FabricItemSettings());
    


    public static void register() {
        Registry.register(Registries.BLOCK, YELLOWED_WALLPAPER_ID, YELLOWED_WALLPAPER);
        Registry.register(Registries.ITEM, YELLOWED_WALLPAPER_ID, YELLOWED_WALLPAPER_ITEM);

        Registry.register(Registries.BLOCK, YELLOWED_WALLPAPER_SLAB_ID, YELLOWED_WALLPAPER_SLAB);
        Registry.register(Registries.ITEM, YELLOWED_WALLPAPER_SLAB_ID, YELLOWED_WALLPAPER_SLAB_ITEM);

        Registry.register(Registries.BLOCK, YELLOWED_WALLPAPER_STAIRS_ID, YELLOWED_WALLPAPER_STAIRS);
        Registry.register(Registries.ITEM, YELLOWED_WALLPAPER_STAIRS_ID, YELLOWED_WALLPAPER_STAIRS_ITEM);

        Registry.register(Registries.BLOCK, YELLOWED_WALLPAPER_WALL_ID, YELLOWED_WALLPAPER_WALL);
        Registry.register(Registries.ITEM, YELLOWED_WALLPAPER_WALL_ID, YELLOWED_WALLPAPER_WALL_ITEM);

        
        Registry.register(Registries.BLOCK, UNBREAKABLE_YELLOWED_WALLPAPER_ID, UNBREAKABLE_YELLOWED_WALLPAPER);
        Registry.register(Registries.ITEM, UNBREAKABLE_YELLOWED_WALLPAPER_ID, UNBREAKABLE_YELLOWED_WALLPAPER_ITEM);

        Registry.register(Registries.BLOCK, UNBREAKABLE_YELLOWED_WALLPAPER_SLAB_ID, UNBREAKABLE_YELLOWED_WALLPAPER_SLAB);
        Registry.register(Registries.ITEM, UNBREAKABLE_YELLOWED_WALLPAPER_SLAB_ID, UNBREAKABLE_YELLOWED_WALLPAPER_SLAB_ITEM);

        Registry.register(Registries.BLOCK, UNBREAKABLE_YELLOWED_WALLPAPER_STAIRS_ID, UNBREAKABLE_YELLOWED_WALLPAPER_STAIRS);
        Registry.register(Registries.ITEM, UNBREAKABLE_YELLOWED_WALLPAPER_STAIRS_ID, UNBREAKABLE_YELLOWED_WALLPAPER_STAIRS_ITEM);

        Registry.register(Registries.BLOCK, UNBREAKABLE_YELLOWED_WALLPAPER_WALL_ID, UNBREAKABLE_YELLOWED_WALLPAPER_WALL);
        Registry.register(Registries.ITEM, UNBREAKABLE_YELLOWED_WALLPAPER_WALL_ID, UNBREAKABLE_YELLOWED_WALLPAPER_WALL_ITEM);
    }

    public static void registerBlockItemUnderGroup(FabricItemGroupEntries content) {
        content.add(YELLOWED_WALLPAPER_ITEM);
        content.add(YELLOWED_WALLPAPER_SLAB_ITEM);
        content.add(YELLOWED_WALLPAPER_STAIRS_ITEM);
        content.add(YELLOWED_WALLPAPER_WALL);

        content.add(UNBREAKABLE_YELLOWED_WALLPAPER_ITEM);
        content.add(UNBREAKABLE_YELLOWED_WALLPAPER_SLAB_ITEM);
        content.add(UNBREAKABLE_YELLOWED_WALLPAPER_STAIRS_ITEM);
        content.add(UNBREAKABLE_YELLOWED_WALLPAPER_WALL);
    }
}
