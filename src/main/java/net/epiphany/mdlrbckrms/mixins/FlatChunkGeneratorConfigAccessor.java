package net.epiphany.mdlrbckrms.mixins;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.structure.StructureSet;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;

@Mixin(FlatChunkGeneratorConfig.class)
public interface FlatChunkGeneratorConfigAccessor {
    @Accessor("hasLakes")
    public boolean getHasLakes();

    @Accessor("hasFeatures")
    public boolean getHasFeatures();

    @Accessor("structureOverrides")
    public Optional<RegistryEntryList<StructureSet>> getStructureOverrides();
}
