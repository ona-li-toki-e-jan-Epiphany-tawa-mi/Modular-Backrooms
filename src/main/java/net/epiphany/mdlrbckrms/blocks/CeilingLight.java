package net.epiphany.mdlrbckrms.blocks;

import net.epiphany.Sounds;
import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
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
                           .strength(-1));

    public CeilingLight(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(ON, true));
    }



    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos position, Random random) {
        if (random.nextFloat() < (1.0f / 9.0f))
            cascadeSetLightState(state, world, position, false);
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
    private void cascadeSetLightState(BlockState state, ServerWorld world, BlockPos position, boolean lightState) {
        cascadeSetLightState(state, world, position, lightState, true);
    }
        
    /**
     * Switches adjacent ceiling lights to be on or off in unison.
     * 
     * @param state      The sate of the light block.
     * @param world      The world the light is in.
     * @param position   The position of the light.
     * @param lightState true for on, false for off.
     * @param playSound  Whether to play the buzzing sound when turning off, only plays once.
     */
    private void cascadeSetLightState(BlockState state, ServerWorld world, BlockPos position, boolean lightState, boolean playSound) {
        if (state.getBlock().equals(CEILING_LIGHT) && state.get(ON) != lightState) {
            world.setBlockState(position, state.with(ON, lightState));
            
            if (!lightState) {
                if (playSound)
                    world.playSound(null, position, Sounds.FLUORESCENT_FLICKER, SoundCategory.BLOCKS, 1.0f, 1.0f);
                // TODO: Add cool particle effect.
                //world.addParticle(ParticleTypes.CLOUD, position.getX(), position.getY() - 1, position.getZ(), -0.01, -0.01, -0.01);
                // Schedules the light to turn back on in a 1/4 of a second for a flicker effect.
                world.scheduleBlockTick(position, CEILING_LIGHT, 5);
            }

            // TODO Look for better way to cascade flicker to other lights.
            for (Direction direction : AbstractBlock.DIRECTIONS) {
                BlockPos otherBlockPosition = position.offset(direction);
                cascadeSetLightState(world.getBlockState(otherBlockPosition), world, otherBlockPosition, lightState, false); 
            }
        }

    }
}
