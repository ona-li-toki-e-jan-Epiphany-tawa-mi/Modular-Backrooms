package net.epiphany.mdlrbckrms.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.epiphany.mdlrbckrms.networking.LeftClickPacket;
import net.epiphany.mdlrbckrms.utilities.LeftClickEvent;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    /**
     * Detects when a player left-clicks.
     * 
     * TODO Consider making this part of a library mod.
     * 
     * Works irregardless of whether the player left-clicks an entity, a block, the air, and whether they are using an item.
     * Does not account for things like cooldown or if a player is actively riding something. If you desire that behavior you
     *      will have to check for that yourself. Also does not check if they are in spectator mode.
     * Called BEFORE vanilla does anything with the left click, take care.
     */
    @Inject( method = "Lnet/minecraft/client/MinecraftClient;handleInputEvents()V"
           , at = @At( value = "INVOKE"
                     , target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"
                     , ordinal = 0))
    public void onLeftClick(CallbackInfo info) {
        MinecraftClient client = MinecraftClient.getInstance();
        int timesPressed = ((KeyBindingAccessor) client.options.attackKey).getTimesPressed();

        if (timesPressed <= 0)
            return;


        LeftClickPacket.sendToServer(timesPressed);

        // We can just iterate through each press without resetting the count because it will be reset by the code after the 
        //      injection.
        World world = client.world;
        PlayerEntity player = client.player;
        ItemStack item = player.getMainHandStack();
        
        for (int i = 0; i < timesPressed; i++) 
            LeftClickEvent.ON_LEFT_CLICK.invoker().onLeftClick(world, player, item);
    }
}
