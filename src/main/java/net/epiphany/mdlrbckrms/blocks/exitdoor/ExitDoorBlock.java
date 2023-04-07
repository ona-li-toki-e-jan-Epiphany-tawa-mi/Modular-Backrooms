package net.epiphany.mdlrbckrms.blocks.exitdoor;

import net.epiphany.mdlrbckrms.blocks.OpenableMetalDoorBlock;
import net.epiphany.mdlrbckrms.blocks.exitdoor.ExitDoorEvents.PortalDestination;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

/**
 * An exit door. Forms one of the main ways of transferring throught the Backrooms.
 */
public class ExitDoorBlock extends OpenableMetalDoorBlock implements BlockEntityProvider {
    /**
     * Whether the exit door is a portal to somewhere else.
     */
    public static final BooleanProperty PORTAL = BooleanProperty.of("portal");

    public ExitDoorBlock(Settings settings, BlockSetType blockSetType) {
        super(settings, blockSetType);
        this.setDefaultState(getDefaultState().with(PORTAL, false));
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(PORTAL);
    }

     /**
     * Gets the light produced by a exit door.
     * 
     * @param state The block state.
     * @return 11 if open and has a portal, 0 if off.
     */
    public static int getLuminance(BlockState state) {
        return state.get(DoorBlock.OPEN) && state.get(PORTAL) ? 11 : 0;
    }

    /**
     * Allows changes of the door's portal state to propagate to the other half.
     */
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
            WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction.getAxis() == Direction.Axis.Y && neighborState.isOf(this))
            state = state.with(PORTAL, neighborState.get(PORTAL));
                    
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }



    @Override
    public BlockEntity createBlockEntity(BlockPos position, BlockState state) {
        // There only needs to be one block entity per door, so we restrict it to the upper half.
        return state.get(DoorBlock.HALF) == DoubleBlockHalf.UPPER ? new ExitDoorBlockEntity(position, state) : null;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state,
            BlockEntityType<T> type) {
        return type != ExitDoorBlockEntity.EXIT_DOOR_ENTITY ? null : (worldd, position, statee, blockEntity) -> {
            ExitDoorBlockEntity.tick(worldd, position, statee, (ExitDoorBlockEntity) blockEntity);
        };
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
        // If the portal isn't being shown we can just render it as a normal door.
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

    /**
     * Modified copy of {@link net.minecraft.block.NetherPortalBlock} to have the same particle effects.
     */
    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!state.get(DoorBlock.OPEN) || !state.get(PORTAL))
            return;

        if (random.nextInt(100) == 0) 
            world.playSound( pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5
                           , SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS
                           , 0.5f, random.nextFloat() * 0.4f + 0.8f, false);

        for (int i = 0; i < 4; ++i) {
            double randomX = pos.getX() + random.nextDouble();
            double randomY = pos.getY() + random.nextDouble();
            double randomZ = pos.getZ() + random.nextDouble();
            double xOffset = ((double)random.nextFloat() - 0.5) * 0.5;
            double yOffset = ((double)random.nextFloat() - 0.5) * 0.5;
            double zOffset = ((double)random.nextFloat() - 0.5) * 0.5;
            int particleCount = random.nextInt(2) * 2 - 1;

            Direction facing = state.get(DoorBlock.FACING);
            if (facing == Direction.SOUTH || facing == Direction.NORTH) {
                randomZ = pos.getZ() + 0.5 + 0.25 * particleCount;
                zOffset = random.nextFloat() * 2.0f * particleCount;
            } else {
                randomX = pos.getX() + 0.5 + 0.25 * particleCount;
                xOffset = random.nextFloat() * 2.0f * particleCount;
            }
            
            world.addParticle(ParticleTypes.PORTAL, randomX, randomY, randomZ, xOffset, yOffset, zOffset);
        }
    }



    /**
     * Handles the creation of a portal in the door when a player opens it.
     */
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos position, PlayerEntity player, Hand hand
            , BlockHitResult hit) {
        if (world.isClient)
            return super.onUse(state, world, position, player, hand, hit);

        BlockPos blockEntityPosition = state.get(DoorBlock.HALF) == DoubleBlockHalf.UPPER ? position : position.up();
        ExitDoorBlockEntity blockEntity = world.getBlockEntity(blockEntityPosition, ExitDoorBlockEntity.EXIT_DOOR_ENTITY).orElse(null);

        if (blockEntity != null && !blockEntity.hasPortal()) {
            PortalDestination destination = ExitDoorEvents.TRY_CREATE_PORTAL.invoker().tryCreatePortal(world, position, player);

            if (destination != null) {
                state = state.with(PORTAL, true);
                blockEntity.createPortal(destination.location(), destination.world(), destination.lifespan());
            }
        }
        
        return super.onUse(state, world, position, player, hand, hit);
    }

    /**
     * Allows entering the door's portal by colliding with it, should it exist.
     * Note: only runs if door is open and the entity enters the blocks the door occupies.
     */
    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos position, Entity entity) {
        if (world.isClient)
            return;
        if (!state.get(DoorBlock.OPEN) || !state.get(PORTAL))
            return;

        ExitDoorBlockEntity blockEntity = world.getBlockEntity(position, ExitDoorBlockEntity.EXIT_DOOR_ENTITY).orElse(null);
        if (blockEntity != null)
            blockEntity.tryTeleportEntity(entity);
    }
}
