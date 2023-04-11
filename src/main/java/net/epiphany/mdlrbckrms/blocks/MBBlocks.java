package net.epiphany.mdlrbckrms.blocks;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.epiphany.mdlrbckrms.blocks.exitdoor.ExitDoorBlock;
import net.epiphany.mdlrbckrms.blocks.exitdoor.ExitDoorBlockEntity;
import net.epiphany.mdlrbckrms.blocks.rift.RiftBlock;
import net.epiphany.mdlrbckrms.items.MBItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.Material;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

/**
 * Common methods and fields for all custom blocks and block items.
 */
public class MBBlocks {
    /**
     * A really high blast resistance for blocks that cannot be blown up.
     */
    public static final float UNBLASTABLE = 8960000.0f;
    /**
     * A resistance value such that a block cannot be broken by mining.
     */
    public static final float UNBREAKABLE = -1.0f;



    // Misc.
    public static final RNGBlock RNG = new RNGBlock(FabricBlockSettings.of(Material.GLASS).ticksRandomly().strength(0.3f).sounds(BlockSoundGroup.GLASS));
    public static final RiftBlock RIFT = new RiftBlock(FabricBlockSettings.copyOf(Blocks.NETHER_PORTAL));
    // Ceiling tiles.
    public static final Block CEILING_TILE = new Block(FabricBlockSettings.of(Material.WOOL).strength(0.8f).sounds(BlockSoundGroup.WOOL));
    public static final Block UNBREAKABLE_CEILING_TILE = new Block(FabricBlockSettings.copyOf(CEILING_TILE).strength(UNBREAKABLE, UNBLASTABLE));
    // Fluorescent lights.
    public static final Block DISABLED_FLUORESCENT_LIGHT = new FluorescentLightBlock(FabricBlockSettings.of(Material.REDSTONE_LAMP).strength(0.3f).sounds(BlockSoundGroup.GLASS));
    public static final Block DISABLED_UNBREAKABLE_FLUORESCENT_LIGHT = new FluorescentLightBlock(FabricBlockSettings.copyOf(DISABLED_FLUORESCENT_LIGHT).strength(UNBREAKABLE, UNBLASTABLE));
    public static final FluorescentLightBlock FLUORESCENT_LIGHT = new FluorescentLightBlock(FabricBlockSettings.copyOf(DISABLED_FLUORESCENT_LIGHT).luminance(FluorescentLightBlock::getLuminance).ticksRandomly());
    public static final FluorescentLightBlock UNBREAKABLE_FLUORESCENT_LIGHT = new FluorescentLightBlock(FabricBlockSettings.copyOf(FLUORESCENT_LIGHT).strength(UNBREAKABLE, UNBLASTABLE));
    // Moist carpet.
    public static final MoistCarpetBlock MOIST_CARPET = new MoistCarpetBlock(FabricBlockSettings.of(Material.WOOL).strength(0.8f).sounds(BlockSoundGroup.MOSS_BLOCK));
    public static final MoistCarpetBlock UNBREAKABLE_MOIST_CARPET = new MoistCarpetBlock(FabricBlockSettings.copyOf(MOIST_CARPET).strength(UNBREAKABLE, UNBLASTABLE));
    // Office doors.
    public static final DoorBlock OFFICE_DOOR = new DoorBlock(FabricBlockSettings.of(Material.WOOD).strength(3.0f).sounds(BlockSoundGroup.WOOD), MBBlockSetTypes.CREAKY_WOOD);
    public static final DoorBlock UNBREAKABLE_OFFICE_DOOR = new DoorBlock(FabricBlockSettings.copyOf(OFFICE_DOOR).strength(UNBREAKABLE, UNBLASTABLE), MBBlockSetTypes.CREAKY_WOOD);
    // Yellowed wallpaper.
    public static final Block YELLOWED_WALLPAPER = new Block(FabricBlockSettings.of(Material.STONE).strength(0.8f).requiresTool());
    public static final SlabBlock YELLOWED_WALLPAPER_SLAB = new SlabBlock(FabricBlockSettings.copyOf(YELLOWED_WALLPAPER));
    public static final StairsBlock YELLOWED_WALLPAPER_STAIRS = new StairsBlock(YELLOWED_WALLPAPER.getDefaultState(), FabricBlockSettings.copyOf(YELLOWED_WALLPAPER));
    public static final WallBlock YELLOWED_WALLPAPER_WALL = new WallBlock(FabricBlockSettings.copyOf(YELLOWED_WALLPAPER));
    public static final Block UNBREAKABLE_YELLOWED_WALLPAPER = new Block(FabricBlockSettings.copyOf(YELLOWED_WALLPAPER).strength(UNBREAKABLE, UNBLASTABLE));
    public static final SlabBlock UNBREAKABLE_YELLOWED_WALLPAPER_SLAB = new SlabBlock(FabricBlockSettings.copyOf(UNBREAKABLE_YELLOWED_WALLPAPER));
    public static final StairsBlock UNBREAKABLE_YELLOWED_WALLPAPER_STAIRS = new StairsBlock(UNBREAKABLE_YELLOWED_WALLPAPER.getDefaultState(), FabricBlockSettings.copyOf(UNBREAKABLE_YELLOWED_WALLPAPER));
    public static final WallBlock UNBREAKABLE_YELLOWED_WALLPAPER_WALL = new WallBlock(FabricBlockSettings.copyOf(UNBREAKABLE_YELLOWED_WALLPAPER));
    // Exit doors.
    public static final OpenableMetalDoorBlock EXIT_DOOR = new OpenableMetalDoorBlock(FabricBlockSettings.of(Material.METAL).strength(5.0f, 5.0f).requiresTool().sounds(BlockSoundGroup.METAL), BlockSetType.IRON);
    public static final ExitDoorBlock INTERDIMENSIONAL_EXIT_DOOR = new ExitDoorBlock(FabricBlockSettings.copyOf(EXIT_DOOR).strength(UNBREAKABLE, UNBLASTABLE).luminance(ExitDoorBlock::getLuminance), BlockSetType.IRON);



    /**
     * Registers all custom blocks and related bits.
     */
    public static void registerBlocks() {
        registerBlock("random_number_generator", RNG, true);
        registerBlock("ceiling_tile", CEILING_TILE, true);
        registerBlock("unbreakable_ceiling_tile", UNBREAKABLE_CEILING_TILE, true);
        registerBlock("disabled_fluorescent_light", DISABLED_FLUORESCENT_LIGHT, true);
        registerBlock("disabled_unbreakable_fluorescent_light", DISABLED_UNBREAKABLE_FLUORESCENT_LIGHT, true);
        registerBlock("fluorescent_light", FLUORESCENT_LIGHT, true);
        registerBlock("unbreakable_fluorescent_light", UNBREAKABLE_FLUORESCENT_LIGHT, true);
        registerBlock("moist_carpet", MOIST_CARPET, true);
        registerBlock("unbreakable_moist_carpet", UNBREAKABLE_MOIST_CARPET, true);
        registerBlock("office_door", OFFICE_DOOR, true);
        registerBlock("unbreakable_office_door", UNBREAKABLE_OFFICE_DOOR, true);
        registerBlock("yellowed_wallpaper", YELLOWED_WALLPAPER, true);
        registerBlock("yellowed_wallpaper_slab", YELLOWED_WALLPAPER_SLAB, true);
        registerBlock("yellowed_wallpaper_stairs", YELLOWED_WALLPAPER_STAIRS, true);
        registerBlock("yellowed_wallpaper_wall", YELLOWED_WALLPAPER_WALL, true);
        registerBlock("unbreakable_yellowed_wallpaper", UNBREAKABLE_YELLOWED_WALLPAPER, true);
        registerBlock("unbreakable_yellowed_wallpaper_slab", UNBREAKABLE_YELLOWED_WALLPAPER_SLAB, true);
        registerBlock("unbreakable_yellowed_wallpaper_stairs", UNBREAKABLE_YELLOWED_WALLPAPER_STAIRS, true);
        registerBlock("unbreakable_yellowed_wallpaper_wall", UNBREAKABLE_YELLOWED_WALLPAPER_WALL, true);
        registerBlock("exit_door", EXIT_DOOR, true);
        registerBlock("interdimensional_exit_door", INTERDIMENSIONAL_EXIT_DOOR, true);
        registerBlock("rift", RIFT, true);

        FlammableBlockRegistry registry = FlammableBlockRegistry.getDefaultInstance();
        registry.add(CEILING_TILE, 30, 60);
        registry.add(MOIST_CARPET, 15, 60);
        registry.add(YELLOWED_WALLPAPER, 30, 60);
        registry.add(YELLOWED_WALLPAPER_SLAB, 30, 60);
        registry.add(YELLOWED_WALLPAPER_STAIRS, 30, 60);
        registry.add(YELLOWED_WALLPAPER_WALL, 30, 60);

        registerBlockEntities();
    }

    /**
     * Registers all custom block entities.
     */
    private static void registerBlockEntities() {
        ExitDoorBlockEntity.register();
    }
    
    /**
     * Registers custom color providers for dynamic block model coloration.
     */
    @Environment(EnvType.CLIENT)
    public static void registerColorProviders() {
        RNGBlock.registerColorProviders();
    }



    /**
     * Registers a block and a block item for it.
     * 
     * @param <B>            The block type.
     * @param id             The path of the block's ID (do not include namespace, it will do it for you.)
     * @param block          The block.
     * @param addToItemGroup Whether to add the block's item to the backrooms item group.
     * @return The block, for chaining.
     */
    public static <B extends Block> B registerBlock(String idPath, B block, boolean addToItemGroup) {    
        Identifier id = new Identifier(ModularBackrooms.MOD_ID, idPath);

        BlockItem blockItem = Registry.register(Registries.ITEM, id, new BlockItem(block, new FabricItemSettings()));
        if (addToItemGroup)
            MBItems.items.add(blockItem);

        return Registry.register(Registries.BLOCK, id, block);
    }

     /**
     * Registers a block entity type.
     * 
     * @param <E>             The block entity.
     * @param id              The path of the block entity type's ID (do not include namespace, it will do it for you.)
     * @param blockEntityType The block entity type.
     * @return The block entity type, for chaining.
     */
    public static <E extends BlockEntity> BlockEntityType<E> registerBlockEntityType(String idPath
            , BlockEntityType<E> blockEntityType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(ModularBackrooms.MOD_ID, idPath), blockEntityType);
    }
}
