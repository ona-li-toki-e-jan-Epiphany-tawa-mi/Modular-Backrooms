package net.epiphany.mdlrbckrms.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.SpyglassItem;

@Mixin(SpyglassItem.class)
public interface SpyglassItemInvoker {
    @Invoker("playStopUsingSound")
    public void invokePlayStopUsingSound(LivingEntity user);
}
