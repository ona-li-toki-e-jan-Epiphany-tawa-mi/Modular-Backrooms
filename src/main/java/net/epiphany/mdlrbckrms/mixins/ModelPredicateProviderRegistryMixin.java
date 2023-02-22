package net.epiphany.mdlrbckrms.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.epiphany.mdlrbckrms.levels.Levels;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CompassItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.GlobalPos;

/**
 * Modified the handling of data for the display of dynamically rendered models, currently used for disrupting compasses.
 */
@Mixin(ModelPredicateProviderRegistry.class)
public class ModelPredicateProviderRegistryMixin {
    /**
     * Prevents lodestone compasses from working properly in the backrooms.
     */
    @Inject( method = "Lnet/minecraft/client/item/ModelPredicateProviderRegistry;method_43220(Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/math/GlobalPos;"
           , at = @At("HEAD")
           , cancellable = true)
    private static void onCompassTryPointToLodestone(ClientWorld world, ItemStack itemStack, Entity entity
            , CallbackInfoReturnable<GlobalPos> info) {
        if (entity instanceof PlayerEntity player && player.isCreative())
            return;

        if (Levels.isBackrooms(world))
            info.setReturnValue(CompassItem.createSpawnPos(world));
    }

    /**
     * Prevents recovery compasses from working properly in the backrooms.
     */
    @Inject( method = "Lnet/minecraft/client/item/ModelPredicateProviderRegistry;method_43219(Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/math/GlobalPos;"
           , at = @At("HEAD")
           , cancellable = true)
    private static void onRecoveryCompassTryPoint(ClientWorld world, ItemStack itemStack, Entity entity
            , CallbackInfoReturnable<GlobalPos> info) {
        if (entity instanceof PlayerEntity player && player.isCreative())
            return;

        if (Levels.isBackrooms(world))
            info.setReturnValue(null);
    }
}
