package net.epiphany.mdlrbckrms.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;

@Mixin(DebugHud.class)
public interface DebugHudAccessor {
    @Accessor("client")
    MinecraftClient client();
}
