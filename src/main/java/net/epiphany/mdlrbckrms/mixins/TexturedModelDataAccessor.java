package net.epiphany.mdlrbckrms.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.TextureDimensions;
import net.minecraft.client.model.TexturedModelData;

@Mixin(TexturedModelData.class)
public interface TexturedModelDataAccessor {
    @Accessor("data")
    public ModelData getData();

    @Accessor("dimensions")
    public TextureDimensions getDimensions();
}