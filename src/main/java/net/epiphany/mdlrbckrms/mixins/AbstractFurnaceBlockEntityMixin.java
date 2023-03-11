package net.epiphany.mdlrbckrms.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.epiphany.mdlrbckrms.items.ChickenItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityMixin {
    /**
     * Inserts a listener before the recipe is validated and actually crafts (only possible location to get ingredients,) but the 
     *  recipe is also validated shortly before as well, so it should operate fine(?).
     */
    @Inject( method = "Lnet/minecraft/block/entity/AbstractFurnaceBlockEntity;tick(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/entity/AbstractFurnaceBlockEntity;)V"
           , at = @At( value = "INVOKE"
                     , target = "Lnet/minecraft/block/entity/AbstractFurnaceBlockEntity;getCookTime(Lnet/minecraft/world/World;Lnet/minecraft/block/entity/AbstractFurnaceBlockEntity;)I"))
    private static void onTryCraftRecipe(World world, BlockPos position, BlockState state
            , AbstractFurnaceBlockEntity blockEntity, CallbackInfo info) {
        ChickenItem.onFurnaceCraftRecipe(world, position, blockEntity);
    }
}
