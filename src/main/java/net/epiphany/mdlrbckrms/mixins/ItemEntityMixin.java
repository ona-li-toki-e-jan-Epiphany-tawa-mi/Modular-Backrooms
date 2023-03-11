package net.epiphany.mdlrbckrms.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.epiphany.mdlrbckrms.items.ChickenItem;
import net.minecraft.entity.ItemEntity;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    /**
     * Listener.
     */
    @Inject( method = "Lnet/minecraft/entity/ItemEntity;tick()V"
           , at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;tick()V"))
    private void onTick(CallbackInfo info) {
        ChickenItem.onItemEntityTick((ItemEntity)(Object) this);
    }
}
