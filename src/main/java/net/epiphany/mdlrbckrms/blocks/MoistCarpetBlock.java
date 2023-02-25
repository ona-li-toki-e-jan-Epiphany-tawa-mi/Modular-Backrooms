package net.epiphany.mdlrbckrms.blocks;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * The moist carpeting that appears in Level 0.
 */
public class MoistCarpetBlock extends Block {
     /**
     * Player craftable and destroyable variant of the carpet.
     */
    public static final Identifier MOIST_CARPET_ID = new Identifier(ModularBackrooms.MOD_ID, "moist_carpet");
    public static final MoistCarpetBlock MOIST_CARPET = new MoistCarpetBlock(
            FabricBlockSettings.of(Material.WOOL).strength(0.8f).sounds(BlockSoundGroup.MOSS_BLOCK));
    public static final BlockItem MOIST_CARPET_ITEM = new BlockItem(MOIST_CARPET, new FabricItemSettings());

    /**
     * Player craftable and destroyable variant of the carpet.
     */
    public static final Identifier UNBREAKABLE_MOIST_CARPET_ID = 
            new Identifier(ModularBackrooms.MOD_ID, "unbreakable_moist_carpet");
    public static final MoistCarpetBlock UNBREAKABLE_MOIST_CARPET = new MoistCarpetBlock(
            FabricBlockSettings.of(Material.WOOL)
                               .strength(Blocks.UNBREAKABLE, Blocks.UNBLASTABLE)
                               .sounds(BlockSoundGroup.MOSS_BLOCK));
    public static final BlockItem UNBREAKABLE_MOIST_CARPET_ITEM = 
            new BlockItem(UNBREAKABLE_MOIST_CARPET, new FabricItemSettings());

    public static void register() {
        Registry.register(Registries.BLOCK, MOIST_CARPET_ID, MOIST_CARPET);
        Registry.register(Registries.ITEM, MOIST_CARPET_ID, MOIST_CARPET_ITEM);
        FlammableBlockRegistry.getDefaultInstance().add(MOIST_CARPET, 15, 60);

        Registry.register(Registries.BLOCK, UNBREAKABLE_MOIST_CARPET_ID, UNBREAKABLE_MOIST_CARPET);
        Registry.register(Registries.ITEM, UNBREAKABLE_MOIST_CARPET_ID, UNBREAKABLE_MOIST_CARPET_ITEM);
    }

    public static void registerBlockItemUnderGroup(FabricItemGroupEntries content) {
        content.add(MOIST_CARPET_ITEM);
        content.add(UNBREAKABLE_MOIST_CARPET_ITEM);
    }



    public MoistCarpetBlock(Settings settings) {
        super(settings);
    }



    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hit) {
        return ActionResult.PASS; //TODO
    }
}
