package net.epiphany.mdlrbckrms.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;

/**
 * A random number generator that outputs a random restone signal strength from 0 to 15 when randomly ticked.
 */
public class RNGBlock extends Block {
    public static final IntProperty POWER = Properties.POWER;

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(POWER);
    }

    public RNGBlock(Settings settings) {
        super(settings);
        this.setDefaultState(getDefaultState().with(POWER, 15));
    }

    /**
     * Makes RNGs be colored similar to that of redstone dust.
     */
    @Environment(EnvType.CLIENT)
    public static void registerColorProviders() {
        ColorProviderRegistry.BLOCK.register( (state, world, position, tintIndex) -> 
            RedstoneWireBlock.getWireColor(state.get(POWER)), MBBlocks.RNG);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> RedstoneWireBlock.getWireColor(15), MBBlocks.RNG.asItem());
    }



    /**
     * Randomly selects a signal strength to ouptut.
     */
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.setBlockState(pos, state.with(POWER, random.nextInt(16)));
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(POWER);
    }
}
