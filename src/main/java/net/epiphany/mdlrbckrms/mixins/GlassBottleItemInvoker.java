package net.epiphany.mdlrbckrms.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.item.ItemStack;

@Mixin(GlassBottleItem.class)
public interface GlassBottleItemInvoker {
    @Invoker("fill")
    public ItemStack invokeFill(ItemStack stack, PlayerEntity player, ItemStack outputStack);
}
