package net.epiphany.mdlrbckrms.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.epiphany.mdlrbckrms.items.ChickenItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(CampfireBlockEntity.class)
public class CampfireBlockEntityMixin {
    /**
     * Listener.
     */
    @Inject( method = "Lnet/minecraft/block/entity/CampfireBlockEntity;litServerTick(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/entity/CampfireBlockEntity;)V"
           , at = @At( value = "INVOKE"
                     , target = "Lnet/minecraft/util/ItemScatterer;spawn(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V")
           , locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void onCookItem(World world, BlockPos pos, BlockState state, CampfireBlockEntity campfire
            , CallbackInfo info, boolean stateChanged, int ingredientIndex, ItemStack ingredient) {
        ChickenItem.onCampfireCookItem(world, pos, ingredient);
    }
}
