package net.epiphany.mdlrbckrms.mixins;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.util.PlacedFeatureIndexer;

@Mixin(ChunkGenerator.class)
public interface ChunkGeneratorAccessor {
    @Accessor("generationSettingsGetter")
    public void setGenerationSettingsGetter(Function<RegistryEntry<Biome>, GenerationSettings> generationSettingsGetter);

    @Accessor("indexedFeaturesListSupplier")
    public void setIndexedFeaturesListSupplier(Supplier<List<PlacedFeatureIndexer.IndexedFeatures>> indexedFeaturesListSupplier);
}
