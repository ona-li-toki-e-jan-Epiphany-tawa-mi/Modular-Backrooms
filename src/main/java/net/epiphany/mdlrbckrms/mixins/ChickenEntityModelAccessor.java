package net.epiphany.mdlrbckrms.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.ChickenEntityModel;

// TODO Remove if not needed.
@Mixin(ChickenEntityModel.class)
public interface ChickenEntityModelAccessor {
    @Accessor("head")
    public ModelPart getHead();
    
    @Accessor("body")
    public ModelPart getBody();
}
