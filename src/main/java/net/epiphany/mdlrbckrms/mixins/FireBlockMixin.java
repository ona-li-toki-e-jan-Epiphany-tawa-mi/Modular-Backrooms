package net.epiphany.mdlrbckrms.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.epiphany.mdlrbckrms.blocks.WallpaperBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

@Mixin(FireBlock.class)
public class FireBlockMixin {
    /**
     * Listens to when a block is replaced with fire.
     */
    @Inject( method = "Lnet/minecraft/block/FireBlock;trySpreadingFire(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;ILnet/minecraft/util/math/random/Random;I)V"
           , at = @At( value = "INVOKE_ASSIGN"
                     , target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z")
           , locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onReplaceBlock(World world, BlockPos position, int spreadFactor, Random random, int currentAge, CallbackInfo info
            , int spreadChance, BlockState originalState) {
        WallpaperBlock.onBlockDestoryedByFire(world, position, originalState);
    }

    /**
     * Listens to when a block is destoryed by fire.
     */
    @Inject( method = "Lnet/minecraft/block/FireBlock;trySpreadingFire(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;ILnet/minecraft/util/math/random/Random;I)V"
           , at = @At( value = "INVOKE_ASSIGN"
                     , target = "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z")
           , locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onRemoveBlock(World world, BlockPos position, int spreadFactor, Random random, int currentAge, CallbackInfo info
            , int spreadChance, BlockState originalState) {
        WallpaperBlock.onBlockDestoryedByFire(world, position, originalState);
    }
}
