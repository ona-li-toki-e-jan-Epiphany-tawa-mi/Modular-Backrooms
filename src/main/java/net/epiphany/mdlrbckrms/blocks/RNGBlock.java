package net.epiphany.mdlrbckrms.blocks;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
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



    public static final Identifier RNG_ID = new Identifier(ModularBackrooms.MOD_ID, "random_number_generator");
    public static final RNGBlock RNG = new RNGBlock(
        FabricBlockSettings.of(Material.GLASS)
                           .ticksRandomly()
                           .strength(0.3f)
                           .sounds(BlockSoundGroup.GLASS));
    public static final BlockItem RNG_ITEM = new BlockItem(RNG, new FabricItemSettings());

    public static void register() {
        Registry.register(Registries.BLOCK, RNG_ID, RNG);
        Registry.register(Registries.ITEM, RNG_ID, RNG_ITEM);
    }

    public static void registerBlockItemUnderGroup(FabricItemGroupEntries content) {
        content.add(RNG_ITEM);
    }



    public RNGBlock(Settings settings) {
        super(settings);
        this.setDefaultState(getDefaultState().with(POWER, 15));
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
