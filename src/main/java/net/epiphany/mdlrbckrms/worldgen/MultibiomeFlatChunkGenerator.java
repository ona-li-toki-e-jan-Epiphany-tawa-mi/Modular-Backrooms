package net.epiphany.mdlrbckrms.worldgen;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.GenerationStep.Carver;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.noise.NoiseConfig;

/**
 * A superflat world generator that can handle mutiple biomes.
 */
public class MultibiomeFlatChunkGenerator extends NoiseChunkGenerator {
    public static final Codec<MultibiomeFlatChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> 
            instance.group( MultibiomeFlatChunkGeneratorConfig.CODEC.fieldOf("flat_settings")
                                                                    .forGetter(MultibiomeFlatChunkGenerator::getFlatSettings)        
                          , BiomeSource.CODEC.fieldOf("biome_source")
                                             .forGetter(MultibiomeFlatChunkGenerator::getBiomeSource)
                          , ChunkGeneratorSettings.REGISTRY_CODEC.fieldOf("noise_settings")
                                                                 .forGetter(MultibiomeFlatChunkGenerator::getNoiseSettings)
            ).apply(instance, instance.stable(MultibiomeFlatChunkGenerator::new)));

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }
    


    protected final MultibiomeFlatChunkGeneratorConfig flatSettings;
    protected final RegistryEntry<ChunkGeneratorSettings> noiseSettings;

    /**
     * @param flatSettings The main settings to determine the world's generation.
     * @param biomeSource  Determines what biomes should be placed and where.
     * @param noiseSettings Just used as a reference to a vanilla noise settings to place the biomes with, any data within this may
     *      be overriden by the flat generation settings.
     */
    public MultibiomeFlatChunkGenerator(MultibiomeFlatChunkGeneratorConfig flatSettings, BiomeSource biomeSource
            , RegistryEntry<ChunkGeneratorSettings> noiseSettings) {
        super(biomeSource, noiseSettings);
        this.flatSettings = flatSettings;
        this.noiseSettings = noiseSettings;
    }

    public MultibiomeFlatChunkGeneratorConfig getFlatSettings() {
        return flatSettings;
    }

    public RegistryEntry<ChunkGeneratorSettings> getNoiseSettings() {
        return noiseSettings;
    }



    /**
     * A copy of {@link net.minecraft.world.gen.chunk.FlatChunkGenerator#buildSurface(ChunkRegion, StructureAccessor, NoiseConfig, Chunk)}.
     */
    @Override
    public void buildSurface(ChunkRegion region, StructureAccessor structures, NoiseConfig noiseConfig, Chunk chunk) {}

    /**
     * Basically a copy of {@link net.minecraft.world.gen.chunk.FlatChunkGenerator#populateNoise(Executor, Blender, NoiseConfig, StructureAccessor, Chunk)}.
     */
    @Override
    public CompletableFuture<Chunk> populateNoise(Executor executor, Blender blender, NoiseConfig noiseConfig, StructureAccessor structureAccessor, Chunk chunk) {
        List<BlockState> layerBlocks = this.flatSettings.getLayerBlocks();
        BlockPos.Mutable position = new BlockPos.Mutable();
        
        Heightmap oceanHeightmap = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap surfaceHeightMap = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);

        for (int layer = 0; layer < Math.min(chunk.getHeight(), layerBlocks.size()); layer++) {
            BlockState blockState = layerBlocks.get(layer);
            if (blockState == null) 
                continue;

            int y = chunk.getBottomY() + layer;

            for (int x = 0; x < 16; ++x) 
                for (int z = 0; z < 16; ++z) {
                    chunk.setBlockState(position.set(x, y, z), blockState, false);
                    oceanHeightmap.trackUpdate(x, y, z, blockState);
                    surfaceHeightMap.trackUpdate(x, y, z, blockState);
                }
        }

        return CompletableFuture.completedFuture(chunk);
    }

    /**
     * A copy of {@link net.minecraft.world.gen.chunk.FlatChunkGenerator#carve(ChunkRegion, long, NoiseConfig, BiomeAccess, StructureAccessor, Chunk, Carver)}.
     */
    @Override
    public void carve(ChunkRegion chunkRegion, long seed, NoiseConfig noiseConfig, BiomeAccess biomeAccess,
            StructureAccessor structureAccessor, Chunk chunk2, Carver carverStep) {}



    /**
     * Basically a copy of {@link net.minecraft.world.gen.chunk.FlatChunkGenerator#getSpawnHeight(HeightLimitView)}.
     */
    @Override
    public int getSpawnHeight(HeightLimitView world) {
        return world.getBottomY() + Math.min(world.getHeight(), this.flatSettings.getLayerBlocks().size());
    }
    
    /**
     * Basically a copy of {@link net.minecraft.world.gen.chunk.FlatChunkGenerator#getHeight(int, int, net.minecraft.world.Heightmap.Type, HeightLimitView, NoiseConfig)}.
     */
    @Override
    public int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView world, NoiseConfig noiseConfig) {
        List<BlockState> layerBlocks = this.flatSettings.getLayerBlocks();

        for (int layer = Math.min(layerBlocks.size(), world.getTopY()) - 1; layer >= 0; layer++) {
            BlockState blockState = layerBlocks.get(layer);
            if (blockState == null || !heightmap.getBlockPredicate().test(blockState)) 
                continue;

            return world.getBottomY() + layer + 1;
        }

        return world.getBottomY();
    }

    /**
     * Basically a copy of {@link net.minecraft.world.gen.chunk.FlatChunkGenerator#getColumnSample(int, int, HeightLimitView, NoiseConfig)}.
     */
    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world, NoiseConfig noiseConfig) {
        return new VerticalBlockSample( world.getBottomY()
                                      , this.flatSettings.getLayerBlocks()
                                                         .stream()
                                                         .limit(world.getHeight())
                                                         .map((state) -> state == null ? Blocks.AIR.getDefaultState() : state)
                                                         .toArray(BlockState[]::new));
    }

    /**
     * Overrides the sea level to that set in the flat generation settings.
     */
    @Override
    public int getSeaLevel() {
        return this.flatSettings.getSeaLevel();
    }

    /**
     * Overrides the minimum y level to that set in the flat generation settings.
     */
    @Override
    public int getMinimumY() {
        return this.flatSettings.getMinumumY();
    }

    /**
     * Overrides the world height to be the size defined by the layers in the flat generation settings.
     */
    @Override
    public int getWorldHeight() {
        return this.flatSettings.getLayerBlocks().size();
    }
}
