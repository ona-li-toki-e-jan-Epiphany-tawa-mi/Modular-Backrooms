package net.epiphany.mdlrbckrms.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * The wallpaper covering the walls of Level 0.
 */
public class WallpaperBlock  {
    /**
     * Replaces wallpaper blocks destoryed by fire with sandstone to simulate the wallpaper burning off.
     * 
     * @param world         The world the block is in.
     * @param position      The position of the block.
     * @param originalState The state of the block before it was destoryed.
     */
    public static void onBlockDestoryedByFire(World world, BlockPos position, BlockState originalState) {
        if (originalState.isOf(MBBlocks.YELLOWED_WALLPAPER)) {
            world.setBlockState(position, Blocks.SANDSTONE.getDefaultState());
        } else if (originalState.isOf(MBBlocks.YELLOWED_WALLPAPER_SLAB)) {
            world.setBlockState(position, Blocks.SANDSTONE_SLAB.getDefaultState()
                                                               .with(SlabBlock.TYPE,        originalState.get(SlabBlock.TYPE))
                                                               .with(SlabBlock.WATERLOGGED, originalState.get(SlabBlock.WATERLOGGED)));
        } else if (originalState.isOf(MBBlocks.YELLOWED_WALLPAPER_STAIRS)) {
            world.setBlockState(position, Blocks.SANDSTONE_STAIRS.getDefaultState()
                                                                 .with(StairsBlock.FACING,      originalState.get(StairsBlock.FACING))
                                                                 .with(StairsBlock.HALF,        originalState.get(StairsBlock.HALF))
                                                                 .with(StairsBlock.SHAPE,       originalState.get(StairsBlock.SHAPE))
                                                                 .with(StairsBlock.HALF,        originalState.get(StairsBlock.HALF))
                                                                 .with(StairsBlock.WATERLOGGED, originalState.get(StairsBlock.WATERLOGGED)));
        } else if (originalState.isOf(MBBlocks.YELLOWED_WALLPAPER_WALL)) 
            world.setBlockState(position, Blocks.SANDSTONE_WALL.getDefaultState()
                                                               .with(WallBlock.EAST_SHAPE,  originalState.get(WallBlock.EAST_SHAPE))
                                                               .with(WallBlock.NORTH_SHAPE, originalState.get(WallBlock.NORTH_SHAPE))
                                                               .with(WallBlock.SOUTH_SHAPE, originalState.get(WallBlock.SOUTH_SHAPE))
                                                               .with(WallBlock.WEST_SHAPE,  originalState.get(WallBlock.WEST_SHAPE))
                                                               .with(WallBlock.UP,          originalState.get(WallBlock.UP))
                                                               .with(WallBlock.WATERLOGGED, originalState.get(WallBlock.WATERLOGGED)));
    }
}
