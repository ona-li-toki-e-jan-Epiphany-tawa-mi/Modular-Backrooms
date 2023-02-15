package net.epiphany.mdlrbckrms.blocks;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.epiphany.mdlrbckrms.Sounds;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;

/**
 * A ceiling light that flickers every so often based on randomTickSpeed. Adjacent lights flicker in unison.
 */
public class CeilingLight extends Block {
    public static final BooleanProperty ON = BooleanProperty.of("on");

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(ON);
    }



    public static final Identifier CEILING_LIGHT_ID = new Identifier(ModularBackrooms.MOD_ID, "ceiling_light");
    public static final CeilingLight CEILING_LIGHT = new CeilingLight(
        FabricBlockSettings.of(Material.REDSTONE_LAMP)
                           .luminance(state -> state.get(ON) ? 15 : 0)
                           .ticksRandomly()
                           .strength(Blocks.UNBREAKABLE, Blocks.UNBLASTABLE));

    public CeilingLight(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(ON, true));
    }



    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos position, Random random) {
        if (random.nextFloat() < (1.0f / 50.0f)) {
            cascadeSetLightState(state, world, position, false, random);
            world.playSound( null
                           , position
                           , Sounds.FLUORESCENT_FLICKER, SoundCategory.BLOCKS
                           , 2.0f, 1.0f + 0.025f * random.nextBetween(-1, 1));
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos position, Random random) {
        world.setBlockState(position, state.with(ON, true));
    }

    /**
     * Switches adjacent ceiling lights to be on or off in unison.
     * 
     * @param state      The sate of the light block.
     * @param world      The world the light is in.
     * @param position   The position of the light.
     * @param lightState true for on, false for off.
     */
    private void cascadeSetLightState(BlockState state, ServerWorld world, BlockPos position, boolean lightState, Random random) {
        if (CEILING_LIGHT.equals(state.getBlock()) && state.get(ON) != lightState) {
            world.setBlockState(position, state.with(ON, lightState));
            
            if (!lightState) {
                // Shoots out sparks.
                world.spawnParticles( ParticleTypes.CRIT
                                    , position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5
                                    , random.nextBetween(1, 3)
                                    , 0.25, 0.25, 0.25
                                    , 1.0);
                // Schedules the light to turn back on in a 1/4 of a second for a flicker effect.
                world.scheduleBlockTick(position, CEILING_LIGHT, 5);
            }

            // TODO Look for better way to cascade flicker to other lights.
            for (Direction direction : AbstractBlock.DIRECTIONS) {
                BlockPos otherBlockPosition = position.offset(direction);
                cascadeSetLightState(world.getBlockState(otherBlockPosition), world, otherBlockPosition, lightState, random); 
            }
        }
    }
}
