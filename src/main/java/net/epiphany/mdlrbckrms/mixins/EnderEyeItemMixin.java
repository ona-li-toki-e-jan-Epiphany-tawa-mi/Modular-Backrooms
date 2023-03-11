package net.epiphany.mdlrbckrms.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.epiphany.mdlrbckrms.levels.Levels;
import net.minecraft.block.BlockState;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(EnderEyeItem.class)
public class EnderEyeItemMixin {
    /**
     * Prevents portals to the End from being formed in the backrooms.
     */
    @Inject( method = "Lnet/minecraft/item/EnderEyeItem;useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;"
           , at = @At( value = "INVOKE_ASSIGN"
                     , target = "Lnet/minecraft/block/pattern/BlockPattern;searchAround(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/pattern/BlockPattern$Result;")
           , locals = LocalCapture.CAPTURE_FAILSOFT
           , cancellable = true)
    private void onTryCreateEndPortal(ItemUsageContext context, CallbackInfoReturnable<ActionResult> info, World world
            , BlockPos blockPos, BlockState blockState, BlockState blockState2, BlockPattern.Result result) {
        // If end portal cannot be created anyways we can just return.
        if (result == null)
            return;

        if (Levels.isBackrooms(world))
            info.setReturnValue(ActionResult.CONSUME); // Cancels portal creation but allows eye to be placed in frame.
    }
}
