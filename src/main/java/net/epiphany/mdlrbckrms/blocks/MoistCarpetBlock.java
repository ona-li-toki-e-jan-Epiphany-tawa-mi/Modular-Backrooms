package net.epiphany.mdlrbckrms.blocks;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.epiphany.mdlrbckrms.items.SuspicousWaterItem;
import net.epiphany.mdlrbckrms.mixins.GlassBottleItemInvoker;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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



    /**
     * Allows players to fill glass bottles with the "water" in the carpet.
     */
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hit) {
        ItemStack item = hand == Hand.MAIN_HAND ? player.getMainHandStack() : player.getOffHandStack();
        
        if (!Items.GLASS_BOTTLE.equals(item.getItem()))
            return ActionResult.PASS;
        if (world.isClient)
            return ActionResult.success(true);


        // Gross sound effect like wringing out moist carpet ;).
        world.playSound( null, player.getX(), player.getY(), player.getZ()
                       , BlockSoundGroup.MOSS_BLOCK.getStepSound(), SoundCategory.NEUTRAL
                       , 1.0f, 1.0f);


        // Fills bottle with "water."
        if (world.getRandom().nextInt(7) == 0) {
            ItemStack water = new ItemStack(SuspicousWaterItem.SUSPICOUS_WATER);
            ((GlassBottleItemInvoker) Items.GLASS_BOTTLE).invokeFill(item, player, water);
            
            world.playSound( null, player.getX(), player.getY(), player.getZ()
                        , SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL
                        , 1.0f, 1.0f);
        }


        return ActionResult.success(false);
    }
}
