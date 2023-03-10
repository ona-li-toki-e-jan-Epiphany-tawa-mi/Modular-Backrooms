package net.epiphany.mdlrbckrms.blocks;

import net.epiphany.mdlrbckrms.utilities.MBSounds;
import net.minecraft.block.AbstractBlock;
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



    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos position, Random random) {
        if (random.nextFloat() < (1.0f / 100.0f)) {
            cascadeSetLightState(state, world, position, false, random);

            world.playSound( null
                           , position
                           , MBSounds.FLUORESCENT_FLICKER, SoundCategory.BLOCKS
                           , 1.0f, 1.0f + 0.025f * random.nextBetween(-1, 1));
            world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, position);
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos position, Random random) {
        world.setBlockState(position, state.with(ON, true));
    }

    /**
     * Switches adjacent fluorescent lights to be on or off in unison.
     * 
     * TODO see if neighbor update can be used instead.
     * 
     * @param state      The sate of the light block.
     * @param world      The world the light is in.
     * @param position   The position of the light.
     * @param lightState {@code true} for on, {@code false} for off.
     */
    private void cascadeSetLightState(BlockState state, ServerWorld world, BlockPos position, boolean lightState, Random random) {
        Block block = state.getBlock();
        
        if (state.getBlock() instanceof FluorescentLightBlock && state.get(ON) != lightState) {
            world.setBlockState(position, state.with(ON, lightState));
            
            if (!lightState) {
                // Shoots out sparks.
                world.spawnParticles( ParticleTypes.CRIT
                                    , position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5
                                    , random.nextBetween(1, 3)
                                    , 0.25, 0.25, 0.25
                                    , 1.0);
                // Schedules the light to turn back on in a 1/4 of a second for a flicker effect.
                world.scheduleBlockTick(position, block, 5);
            }

            for (Direction direction : AbstractBlock.DIRECTIONS) {
                BlockPos otherBlockPosition = position.offset(direction);
                cascadeSetLightState(world.getBlockState(otherBlockPosition), world, otherBlockPosition, lightState, random); 
            }
        }
    }
}
