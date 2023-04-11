package net.epiphany.mdlrbckrms.worldgen;

import com.mojang.serialization.Codec;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.epiphany.mdlrbckrms.worldgen.features.MBFeatures;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.chunk.ChunkGenerator;

/**
 * Common methods for world generation.
 */
public class MBWorldGeneration {
    /**
     * Registers special worldgen-related items like chunk generators.
     */
    public static void registerWorldGenerationStuffs() {
        registerChunkGenerator("multibiome_flat", MultibiomeFlatChunkGenerator.CODEC);

        MBFeatures.registerFeatures();
    }



    /**
     * Registers a chunk generator.
     * 
     * @param <CG>                The chunk generator type.
     * @param idPath              The path of the chunk generator's ID (do not include namespace, it will do it for you.)
     * @param chunkGeneratorCodec The chunk generator's codec.
     * @return The chunk generator codec, for chaining.
     */
    public static <CG extends ChunkGenerator> Codec<CG> registerChunkGenerator(String idPath, Codec<CG> chunkGeneratorCodec) {
        return Registry.register(Registries.CHUNK_GENERATOR, new Identifier(ModularBackrooms.MOD_ID, idPath), chunkGeneratorCodec);
    }
}
