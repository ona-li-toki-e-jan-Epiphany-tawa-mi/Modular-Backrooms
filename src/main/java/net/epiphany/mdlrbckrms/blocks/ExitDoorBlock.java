package net.epiphany.mdlrbckrms.blocks;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.DoorHinge;
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
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

// TODO switch to using a single separate sound for opening and closing each.
public class ExitDoorBlock extends DoorBlock {
    /**
     * Whether the exit door is a portal to somewhere else.
     */
    public static final BooleanProperty PORTAL = BooleanProperty.of("portal");

    public ExitDoorBlock(Settings settings, SoundEvent closeSound, SoundEvent openSound) {
        super(settings, closeSound, openSound);
        this.setDefaultState(getDefaultState().with(PORTAL, true));
    }

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

    public static void registerBlockItemUnderGroup(FabricItemGroupEntries content) {
        content.add(EXIT_DOOR_ITEM);
    }

    

    protected static final VoxelShape NORTH_LEFT_OPEN_PORTAL_SHAPE = VoxelShapes.union(DoorBlock.WEST_SHAPE, 
            Block.createCuboidShape(3.0, 0.0, 12.0, 16.0, 16.0, 15.0));
    protected static final VoxelShape NORTH_RIGHT_OPEN_PORTAL_SHAPE = VoxelShapes.union(DoorBlock.EAST_SHAPE, 
            Block.createCuboidShape(0.0, 0.0, 12.0, 13.0, 16.0, 15.0));
    protected static final VoxelShape SOUTH_LEFT_OPEN_PORTAL_SHAPE = VoxelShapes.union(DoorBlock.EAST_SHAPE, 
            Block.createCuboidShape(0.0, 0.0, 1.0, 13.0, 16.0, 4.0));
    protected static final VoxelShape SOUTH_RIGHT_OPEN_PORTAL_SHAPE = VoxelShapes.union(DoorBlock.WEST_SHAPE, 
            Block.createCuboidShape(3.0, 0.0, 1.0, 16.0, 16.0, 4.0));
    protected static final VoxelShape EAST_LEFT_OPEN_PORTAL_SHAPE = VoxelShapes.union(DoorBlock.NORTH_SHAPE, 
            Block.createCuboidShape(1.0, 0.0, 3.0, 4.0, 16.0, 16.0));
    protected static final VoxelShape EAST_RIGHT_OPEN_PORTAL_SHAPE = VoxelShapes.union(DoorBlock.SOUTH_SHAPE, 
            Block.createCuboidShape(1.0, 0.0, 0.0, 4.0, 16.0, 13.0));
    protected static final VoxelShape WEST_LEFT_OPEN_PORTAL_SHAPE = VoxelShapes.union(DoorBlock.SOUTH_SHAPE, 
            Block.createCuboidShape(12.0, 0.0, 0.0, 15.0, 16.0, 13.0));
    protected static final VoxelShape WEST_RIGHT_OPEN_PORTAL_SHAPE = VoxelShapes.union(DoorBlock.NORTH_SHAPE,
            Block.createCuboidShape(12.0, 0.0, 3.0, 15.0, 16.0, 16.0));

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (!state.get(PORTAL) || !state.get(DoorBlock.OPEN))
            return super.getOutlineShape(state, world, pos, context);


        Direction facing = state.get(DoorBlock.FACING);
        boolean hingesRight = state.get(DoorBlock.HINGE) == DoorHinge.RIGHT;
        
        switch (facing) {
            case SOUTH: 
                return hingesRight ? SOUTH_RIGHT_OPEN_PORTAL_SHAPE : SOUTH_LEFT_OPEN_PORTAL_SHAPE;
            case WEST: 
                return hingesRight ? WEST_RIGHT_OPEN_PORTAL_SHAPE : WEST_LEFT_OPEN_PORTAL_SHAPE;
            case NORTH:
                return hingesRight ? NORTH_RIGHT_OPEN_PORTAL_SHAPE : NORTH_LEFT_OPEN_PORTAL_SHAPE;
            default: 
                return hingesRight ? EAST_RIGHT_OPEN_PORTAL_SHAPE : EAST_LEFT_OPEN_PORTAL_SHAPE;
        }
    }



    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos position, PlayerEntity player, Hand hand
            , BlockHitResult hit) {
        if (world.isClient)
            ActionResult.success(true);

        this.setOpen(player, world, state, position, !state.get(DoorBlock.OPEN));
        
        return ActionResult.success(false);


        /* 
        // Ensures that any check is done once on the server side.
        if (world.isClient || !hand.equals(Hand.MAIN_HAND))
            return super.onUse(state, world, position, player, hand, hit);

        ModularBackrooms.LOGGER.info("Right click on exit door!");//TODO Add portal shit

        // Properties of the exit door are dependent upon which dimension they are located inside.
        if (DimensionHelper.isDimension(world, Level0.LEVEL_0_DIMENSION_KEY)) {
        }

        return super.onUse(state, world, position, player, hand, hit);*/
    }
}
