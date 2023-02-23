package net.epiphany.mdlrbckrms.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.epiphany.mdlrbckrms.items.ChickenItem;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;

/**
 * Injects a listener in the tick method for use with item frame entities.
 */
@Mixin(AbstractDecorationEntity.class)
public class AbstractDecorationEntityMixin {
    @Inject(method = "Lnet/minecraft/entity/decoration/AbstractDecorationEntity;tick()V", at = @At("HEAD"))
    private void onTick(CallbackInfo info) {
        if (((Object) this) instanceof ItemFrameEntity itemFrame) 
            ChickenItem.onItemFrameTick(itemFrame);
    }
}
