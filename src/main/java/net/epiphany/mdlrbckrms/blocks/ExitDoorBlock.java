package net.epiphany.mdlrbckrms.blocks;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

// TODO Add item model
public class ExitDoorBlock extends DoorBlock {
    /**
     * Whether the exit door is a portal to somewhere else.
     */
    public static final BooleanProperty PORTAL = BooleanProperty.of("portal");

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(PORTAL);
    }



    public static final Identifier EXIT_DOOR_ID = new Identifier(ModularBackrooms.MOD_ID, "exit_door");
    public static final ExitDoorBlock EXIT_DOOR = new ExitDoorBlock(
        FabricBlockSettings.of(Material.METAL)
                           .strength(Blocks.UNBREAKABLE, Blocks.UNBLASTABLE)
                           .sounds(BlockSoundGroup.METAL)
      , SoundEvents.BLOCK_IRON_DOOR_CLOSE, SoundEvents.BLOCK_IRON_DOOR_OPEN);
    public static final BlockItem EXIT_DOOR_ITEM = new BlockItem( ExitDoorBlock.EXIT_DOOR
                                                                , new FabricItemSettings());

    public static void register() {
        Registry.register(Registries.BLOCK, EXIT_DOOR_ID, EXIT_DOOR);
        Registry.register(Registries.ITEM, EXIT_DOOR_ID, EXIT_DOOR_ITEM);
    }

    public ExitDoorBlock(Settings settings, SoundEvent closeSound, SoundEvent openSound) {
        super(settings, closeSound, openSound);
        this.setDefaultState(getDefaultState().with(PORTAL, false));
    }



    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos position, PlayerEntity player, Hand hand,
            BlockHitResult hit) {
        if (world.isClient)
            return ActionResult.success(true);
        
        state = state.with(PORTAL, world.getRandom().nextBoolean());
        this.setOpen(null, world, state, position, !this.isOpen(state));
        return ActionResult.success(false);


        /* 
        // Ensures that any check is done once on the server side.
        if (world.isClient || !hand.equals(Hand.MAIN_HAND))
            return super.onUse(state, world, position, player, hand, hit);

        ModularBackrooms.LOGGER.info("Right click on exit door!");//TODO remove

        // Properties of the exit door are dependent upon which dimension they are located inside.
        if (DimensionHelper.isDimension(world, Level0.LEVEL_0_DIMENSION_KEY)) {
        }

        return super.onUse(state, world, position, player, hand, hit);*/
    }
}
