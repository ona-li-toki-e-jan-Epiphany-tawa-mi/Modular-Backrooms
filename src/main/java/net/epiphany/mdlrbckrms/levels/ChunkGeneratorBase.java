package net.epiphany.mdlrbckrms.levels;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public abstract class ChunkGeneratorBase extends ChunkGenerator {
    public ChunkGeneratorBase(BiomeSource biomeSource) {
        super(biomeSource);
    } 

    protected static void fillRegion(Chunk chunk, BlockState block, int startX, int endX, int startZ, int endZ, int startY, 
            int endY) {
        for (int x = startX; x <= endX; x++) 
            for (int z = startZ; z <= endZ; z++)
                for (int y = startY; y <= endY; y++)
                    chunk.setBlockState(new BlockPos(x, y, z), block, false);
    }
}
