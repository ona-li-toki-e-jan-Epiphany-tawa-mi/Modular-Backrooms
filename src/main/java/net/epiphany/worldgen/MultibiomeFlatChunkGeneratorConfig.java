package net.epiphany.worldgen;

import java.util.List;
import java.util.Optional;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.epiphany.mdlrbckrms.mixins.FlatChunkGeneratorConfigAccessor;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.structure.StructureSet;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorLayer;
import net.minecraft.world.gen.feature.MiscPlacedFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;

/**
 * Modified version of {@link FlatChunkGeneratorConfig} for {@link net.epiphany.worldgen.MultibiomeFlatChunkGenerator}
 */
public class MultibiomeFlatChunkGeneratorConfig extends FlatChunkGeneratorConfig {
    // For the most part this is a copy of FlatChunkGeneratorConfig's codec
    // The fallback plains biome was removed and the biome replaced with a biome source.
    // TODO Add verifier for layer height.
    public static final Codec<MultibiomeFlatChunkGeneratorConfig> CODEC = RecordCodecBuilder.create(instance -> 
            instance.group( RegistryCodecs.entryList(RegistryKeys.STRUCTURE_SET)
                                          .optionalFieldOf("structure_overrides")
                                          .forGetter(config -> ((FlatChunkGeneratorConfigAccessor) config).getStructureOverrides())
                          , FlatChunkGeneratorLayer.CODEC.listOf()
                                                         .fieldOf("layers")
                                                         .forGetter(FlatChunkGeneratorConfig::getLayers)
                          , Codec.BOOL.fieldOf("lakes")
                                      .orElse(false)
                                      .forGetter(config -> ((FlatChunkGeneratorConfigAccessor) config).getHasLakes())
                          , Codec.BOOL.fieldOf("features")
                                      .orElse(false)
                                      .forGetter(config -> ((FlatChunkGeneratorConfigAccessor) config).getHasFeatures())
                          , BiomeSource.CODEC.fieldOf("biome_source")
                                             .forGetter(config -> config.biomeSource)
                          , RegistryOps.getEntryCodec(MiscPlacedFeatures.LAKE_LAVA_UNDERGROUND)
                          , RegistryOps.getEntryCodec(MiscPlacedFeatures.LAKE_LAVA_SURFACE))
                    .apply(instance, MultibiomeFlatChunkGeneratorConfig::new));



    protected final BiomeSource biomeSource;

    /**
     * @param structureOverrides         If present, selects which structures spawn irregardless of biome settings.
     * @param layers                     The block layers of the superflat world.
     * @param lakes                      Whether to generate lava lake features.
     * @param features                   Whether to generate non-lake features.
     * @param biomeSource                The biome source to generate biomes with.
     * @param undergroundLavaLakeFeature A reference to the underground lava lake feature.
     * @param surfaceLavaLakeFeature     A reference to the surface lava lake feature.
     */
    public MultibiomeFlatChunkGeneratorConfig(Optional<RegistryEntryList<StructureSet>> structureOverrides
            , List<FlatChunkGeneratorLayer> layers, boolean lakes, boolean features, BiomeSource biomeSource
            , RegistryEntry<PlacedFeature> undergroundLavaLakeFeature, RegistryEntry<PlacedFeature> surfaceLavaLakeFeature) {
        // Note: the biome field for the "underlying" flat chunk generator config is set to null, beware!
        // The getBiome() method will operate fine though, it will return a proper default value.
        super(structureOverrides, null, List.of(undergroundLavaLakeFeature, surfaceLavaLakeFeature));

        // Stuff from FlatChunkGeneratorConfig's private constructor we can't (resonably) access.
        if (lakes)
            this.enableLakes();
        if (features)
            this.enableFeatures();
        this.getLayers().addAll(layers);
        this.updateLayerBlocks();


        // New stuff provided by this class.
        this.biomeSource = biomeSource;
    }

    public BiomeSource getBiomeSource() {
        return this.biomeSource;
    }



    /**
     * Overrides to simply return the generation settings of the biome, as the extra code within 
     *      {@link net.minecraft.world.gen.chunk.FlatChunkGenerator} is not neccessary.
     */
    @Override
    public GenerationSettings createGenerationSettings(RegistryEntry<Biome> biomeEntry) {
        return biomeEntry.value().getGenerationSettings();
    }

    /**
     * @return The first biome from the biome source as the default biome (mainly just for the subclass.)
     */
    @Override
    public RegistryEntry<Biome> getBiome() {
        return biomeSource.getBiomes().stream().findFirst().get();
    }
}
