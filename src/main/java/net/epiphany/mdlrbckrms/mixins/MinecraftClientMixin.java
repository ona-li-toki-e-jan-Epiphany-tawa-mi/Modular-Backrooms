package net.epiphany.mdlrbckrms.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.epiphany.mdlrbckrms.networking.LeftClickPacket;
import net.epiphany.mdlrbckrms.utilities.LeftClickEvents;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    /**
     * Detects when a player left-clicks and calls relavent events and sends off the neccessary packets.
     * 
     * TODO Consider making this part of a library mod.
     */
    @SuppressWarnings("resource")
    @Inject( method = "Lnet/minecraft/client/MinecraftClient;handleInputEvents()V"
           , at = @At( value = "INVOKE"
                     , target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"
                     , ordinal = 0))
    public void onLeftClick(CallbackInfo info) {
        MinecraftClient client = (MinecraftClient)(Object) this;
        int timesPressed = ((KeyBindingAccessor) client.options.attackKey).getTimesPressed();

        if (timesPressed <= 0)
            return;


        
        ClientWorld world = client.world;
        ClientPlayerEntity player = client.player;
        ItemStack item = player.getMainHandStack();

        boolean isValid = ((MinecraftClientAccessor) client).getAttackCooldown() <= 0 && client.crosshairTarget != null 
                       && !player.isRiding() && item.isItemEnabled(world.getEnabledFeatures()); 
        


        LeftClickPacket.sendToServer(timesPressed, isValid);

        // We can just iterate through each press without resetting the count because it will be reset by the code after the 
        //      injection.
        for (int i = 0; i < timesPressed; i++) 
            LeftClickEvents.ON_LEFT_CLICK.invoker().onLeftClick(world, player, item);

        if (isValid)
            for (int i = 0; i < timesPressed; i++) 
                LeftClickEvents.ON_VALID_LEFT_CLICK.invoker().onLeftClick(world, player, item);
    }
}
