package net.epiphany.worldgen;

import java.util.List;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.epiphany.mdlrbckrms.mixins.ChunkGeneratorAccessor;
import net.minecraft.util.Util;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.FlatChunkGenerator;
import net.minecraft.world.gen.feature.util.PlacedFeatureIndexer;

/**
 * A version of the {@link FlatChunkGenerator} that accepts a biome source instead of a biome to allow multiple biomes to be used.
 */
public class MultibiomeFlatChunkGenerator extends FlatChunkGenerator {
    public static final Codec<MultibiomeFlatChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> 
            instance.group(MultibiomeFlatChunkGeneratorConfig.CODEC.fieldOf("settings")
                                                                   .forGetter(MultibiomeFlatChunkGenerator::getConfig))
                    .apply(instance, instance.stable(MultibiomeFlatChunkGenerator::new)));
    
    
    
    protected final MultibiomeFlatChunkGeneratorConfig config;
        
    public MultibiomeFlatChunkGenerator(MultibiomeFlatChunkGeneratorConfig config) {
        super(config);
        this.config = config;

        ChunkGeneratorAccessor chunkGeneratorAccessor = (ChunkGeneratorAccessor) this;
        // Overrides for stuff set by FlatChunkGenerator.
        this.biomeSource = config.getBiomeSource();
        chunkGeneratorAccessor.setGenerationSettingsGetter(config::createGenerationSettings);
        chunkGeneratorAccessor.setIndexedFeaturesListSupplier(Suppliers.memoize(() -> 
                PlacedFeatureIndexer.collectIndexedFeatures( List.copyOf(this.biomeSource.getBiomes()), biomeEntry -> 
                        Util.memoize(config::createGenerationSettings).apply(biomeEntry).getFeatures()
                                                           , true)));
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    public MultibiomeFlatChunkGeneratorConfig getConfig() {
        return this.config;
    }
}
