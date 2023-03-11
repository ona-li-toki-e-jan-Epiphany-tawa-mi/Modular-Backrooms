package net.epiphany.mdlrbckrms.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.epiphany.mdlrbckrms.levels.Levels;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    /**
     * Prevents the death screen from being shown when you die in the backrooms because you will just respawn in an instant.
     */
    @Redirect( method = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;onDeathMessage(Lnet/minecraft/network/packet/s2c/play/DeathMessageS2CPacket;)V"
             , at = @At( value  = "INVOKE"
                       , target = "Lnet/minecraft/client/network/ClientPlayerEntity;showsDeathScreen()Z"))
    private boolean showsDeathScreen(ClientPlayerEntity self) {
        return !Levels.isBackrooms(self.getWorld()) && self.showsDeathScreen();
    }
}
