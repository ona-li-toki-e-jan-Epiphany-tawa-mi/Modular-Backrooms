package net.epiphany.mdlrbckrms.blocks;

import net.epiphany.mdlrbckrms.utilities.MBSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;

/**
 * A fluorescent light that flickers every so often based on randomTickSpeed. Adjacent lights flicker in unison.
 */
public class FluorescentLightBlock extends Block {
    public static final BooleanProperty ON = BooleanProperty.of("on");

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(ON);
    }

    /**
     * Gets the light produced by a fluorescent light.
     * 
     * @param state The block state.
     * @return 15 if on, 0 if off.
     */
    public static int getLuminance(BlockState state) {
        return state.get(ON) ? 15 : 0;
    }

    

    public FluorescentLightBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(ON, true));
    }



    /**
     * Causes the lights to randomly flicker.
     */
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos position, Random random) {
        if (state.get(ON) && random.nextFloat() < (1.0f / 100.0f)) {
            world.setBlockState(position, state.with(ON, false));
            world.scheduleBlockTick(position, this, 5);

            this.spawnSparkParticles(world, position);

            world.playSound( null
                           , position
                           , MBSounds.FLUORESCENT_FLICKER, SoundCategory.BLOCKS
                           , 1.0f, 1.0f + 0.025f * random.nextBetween(-1, 1));
            world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, position);
        }
    }

    /**
     * Turns back on the light after it has flickered.
     */
    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos position, Random random) {
        world.setBlockState(position, state.with(ON, true));
    }

    /**
     * Propagates flickering of fluorescent lights.
     */
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction updateDirection, BlockState neighborState,
            WorldAccess world, BlockPos position, BlockPos neighborPosition) {
        if (!neighborState.isOf(this))
            return state;

        boolean shouldTurnOff = !neighborState.get(ON) && state.get(ON);
        state = shouldTurnOff ? state.with(ON, false) : state;


        if (world.isClient()) 
            return state;

    
        if (shouldTurnOff) {
            ServerWorld serverWorld = (ServerWorld) world;

            this.spawnSparkParticles(serverWorld, position);
            serverWorld.scheduleBlockTick(position, this, 5);
        }

        return state;
    }



    /**
     * Spawns the sparks for when the light flickers.
     * 
     * @param world    The world to spawn them in.
     * @param position The position to spawn them at.
     */
    protected void spawnSparkParticles(ServerWorld world, BlockPos position) {
        world.spawnParticles( ParticleTypes.CRIT
                            , position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5
                            , world.getRandom().nextBetween(1, 3)
                            , 0.25, 0.25, 0.25
                            , 1.0);
    }
}
