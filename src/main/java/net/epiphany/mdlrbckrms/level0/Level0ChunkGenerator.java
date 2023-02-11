package net.epiphany.mdlrbckrms.level0;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.epiphany.mdlrbckrms.ChunkGeneratorBase;
import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.minecraft.block.Blocks;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep.Carver;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.noise.NoiseConfig;

public class Level0ChunkGenerator extends ChunkGeneratorBase {
    public static final Identifier CHUNK_GENERATOR_ID = new Identifier(ModularBackrooms.MOD_ID, Level0.LEVEL_0_ID);
    
    public static final Codec<Level0ChunkGenerator> CODEC = RecordCodecBuilder.create((instance) ->
        instance.group(Biome.REGISTRY_CODEC.fieldOf("biome").forGetter(o -> o.biome))
                .apply(instance, Level0ChunkGenerator::new));
    public final RegistryEntry<Biome> biome;

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    public Level0ChunkGenerator(RegistryEntry<Biome> biome) {
        super(new FixedBiomeSource(biome));
        this.biome = biome;
    }



    @Override
    public void buildSurface(ChunkRegion region, StructureAccessor structures, NoiseConfig noiseConfig, Chunk chunk) {}

    @Override
    public void carve(ChunkRegion chunkRegion, long seed, NoiseConfig noiseConfig, BiomeAccess biomeAccess,
            StructureAccessor structureAccessor, Chunk chunk, Carver carverStep) {}

    // TODO make multithreaded.
    @Override
    public CompletableFuture<Chunk> populateNoise(Executor executor, Blender blender, NoiseConfig noiseConfig,
            StructureAccessor structureAccessor, Chunk chunk) {
        int startX = chunk.getPos().getStartX()
        , endX = chunk.getPos().getEndX();
        int startZ = chunk.getPos().getStartZ()
        , endZ = chunk.getPos().getEndZ();
        int startY = chunk.getBottomY()
        , endY = chunk.getTopY();

        fillRegion(chunk, Blocks.GREEN_WOOL.getDefaultState(), startX, endX, startZ, endZ, startY, -3);
        fillRegion(chunk, Blocks.VOID_AIR.getDefaultState(), startX, endX, startZ, endZ, -2, 2);
        fillRegion(chunk, Blocks.SMOOTH_STONE.getDefaultState(), startX, endX, startZ, endZ, 2, endY);
        
        return CompletableFuture.completedFuture(chunk);
    }

    

    @Override
    public void populateEntities(ChunkRegion region) {}

    

    /**
     * Height for structures is always at y=-2.
     */
    @Override
    public int getHeight(int x, int z, Type heightmap, HeightLimitView world, NoiseConfig noiseConfig) {
        return -2;
    }

    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world, NoiseConfig noiseConfig) {
        return null; // TODO
    }



    /**
     * Level 0 goes from -128 to 127, doesn't need to be any bigger.
     */
    @Override
    public int getMinimumY() {
        return -128;
    }

    @Override
    public int getWorldHeight() {
        return 256;
    }

    /**
     * Sea level dosen't matter in Level 0, so it's just set to somewhere in the bottom.
     */
    @Override
    public int getSeaLevel() {
        return -64;
    }



    @Override
    public void getDebugHudText(List<String> text, NoiseConfig noiseConfig, BlockPos pos) {}
}
