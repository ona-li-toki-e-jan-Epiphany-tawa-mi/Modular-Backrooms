package net.epiphany.mdlrbckrms.blocks.rift;

import net.epiphany.mdlrbckrms.blocks.MBBlocks;
import net.epiphany.mdlrbckrms.utilities.DimensionHelper;
import net.epiphany.mdlrbckrms.utilities.MiscellaneousHelpers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;

/**
 * A transient rift between worlds that grows out and swallows the earth (just cool randomly generated portals to where-ever.)
 */
public class RiftBlock extends NetherPortalBlock {
    /**
     * Used to control how far a rift can grow from it's starting point.
     */
    public static final IntProperty DISTANCE = Properties.DISTANCE_0_7;

    public RiftBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(DISTANCE, 0));
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(DISTANCE);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return MBBlocks.RIFT.asItem().getDefaultStack();
    }

    


    @Override
    public void onPlaced(World world, BlockPos position, BlockState state, LivingEntity placer, ItemStack itemStack) {
        world.playSound(placer, position, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.0f, -0.5f);
    }

    /**
     * Ensures that the rift's axis is aligned to the player when placed.
     */
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return this.getDefaultState().with(AXIS, context.getHorizontalPlayerFacing().rotateYClockwise().getAxis());
    }



    /**
     * Causes the entire rift to break if one of it's blocks breaks.
     */
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos position, BlockState newState, boolean moved) {
        // If the rift was moved for some reason it's probably better not to break it as the caller likely doesn't want it to.
        if (moved)
            return;

        Direction.Axis axis = state.get(NetherPortalBlock.AXIS);

        tryCascadeBreak(axis, world, position.offset(Direction.UP));
        tryCascadeBreak(axis, world, position.offset(Direction.DOWN));
        if (axis == Direction.Axis.X) {
            tryCascadeBreak(axis, world, position.offset(Direction.EAST));
            tryCascadeBreak(axis, world, position.offset(Direction.WEST));
        } else {
            tryCascadeBreak(axis, world, position.offset(Direction.NORTH));
            tryCascadeBreak(axis, world, position.offset(Direction.SOUTH));
        }
    }

    /**
     * Attempts to cascade rift blocks breaking. 
     * 
     * Succeeds if the block is a rift block and it is aligned with the given axis. If succeeded, the block will be broken 
     *      2 ticks later.
     * 
     * @param axis     The axis of the source rift block.
     * @param world    The world the possible rift block is in.
     * @param position The position of the possible rift block.
     */
    private void tryCascadeBreak(Direction.Axis axis, World world, BlockPos position) {
        BlockState state = world.getBlockState(position);

        if (state.isOf(this) && state.get(NetherPortalBlock.AXIS) == axis)
            world.scheduleBlockTick(position, this, 2);
    }

    /**
     * Used to delay rift block cascade breaking for the A M B I A N C E.
     */
    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.breakBlock(pos, false);
    }

    

    /**
     * Allows rifts to grow out.
     */
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos position, Random random) {
        int distance = state.get(DISTANCE);

        if (distance < 7) {
            Direction growDirection;

            if (random.nextBoolean()) {
                growDirection = random.nextBoolean() ? Direction.UP : Direction.DOWN;
            } else if (state.get(NetherPortalBlock.AXIS) == Direction.Axis.X) { 
                growDirection = random.nextBoolean() ? Direction.WEST : Direction.EAST;
            } else
                growDirection = random.nextBoolean() ? Direction.NORTH : Direction.SOUTH;

            BlockPos growToPosition = position.offset(growDirection);

            if (world.getBlockState(growToPosition).isReplaceable()) {
                // The randomness on distance helps to make portal growth more chaotic.
                world.setBlockState(growToPosition, state.with(DISTANCE, Math.min(7, distance + random.nextBetween(1, 4))));
                
                world.playSound(null, growToPosition, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.0f, -0.5f);
                world.emitGameEvent(null, GameEvent.BLOCK_PLACE, growToPosition);
            }
        }
    }



    /**
     * If there is a destination, the entity will be teleported to there. If not, the rift will be destoryed.
     */
    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos position, Entity entity) {
        if (world.isClient)
            return;

        RegistryKey<World> worldTo = RiftEvents.ON_ENTITY_ENTER_RIFT.invoker().onEntityEnterRift(world, position, entity);
    
        if (worldTo == null) {
            world.scheduleBlockTick(position, this, 2);

        } else {
            World newWorld = world.getServer().getWorld(worldTo);
            Vec3d destination = newWorld != null ? MiscellaneousHelpers.findValidPosition(newWorld, new ChunkPos(position), entity) 
                                                 : null;

            if (destination != null) 
                DimensionHelper.teleportToDimension( entity
                                                   , worldTo, destination.getX(), destination.getY(), destination.getZ()
                                                   , true);
        }
    }



    /**
     * Prevents unwanted behaivor in {@link NetherPortalBlock#getStateForNeighborUpdate(BlockState, Direction, BlockState, WorldAccess, BlockPos, BlockPos)}
     *      from running.
     */
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState
            , WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return state;
    }
}
