package net.epiphany.mdlrbckrms.items;

import java.util.function.Predicate;

import net.epiphany.mdlrbckrms.mixins.SpyglassItemInvoker;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.SpyglassItem;
import net.minecraft.item.Vanishable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

/**
 * TODO
 * - Left click to reload and fire.
 */
public class ViteltukoragBurubelbul extends RangedWeaponItem implements Vanishable {
    public static final Predicate<ItemStack> JEEBEB_VITELBEB_VITELTUKORAG_BURUBELBUL = (jee) -> jee.isOf(MBItems.BURUBEL_VITELTUK);
    public static final int TAARTI = 8;

    public static final String NBTBEB_JEEON_VITELON = "JeeonVitelon";

    public ViteltukoragBurubelbul(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = new ItemStack(this);
        NbtCompound nbt = stack.getOrCreateNbt();

        nbt.putBoolean(NBTBEB_JEEON_VITELON, false);

        return stack;
    }

    /**
     * χιμιλβεβ αλ'τερ, ρετ ξεεον διτελον ν'διτελτγκοραη βγργβελβγλ μρεμ.
     * 
     * @param viteltukoragBurubelbul διτελτγκοριτνι.
     * @return αλ'τερ, ρετ ξεεον διτελον μρεμ. 
     */
    public static boolean ximemAlJeeonVitelon(ItemStack viteltukoragBurubelbul) {
        NbtCompound nbt = viteltukoragBurubelbul.getNbt();

        if (nbt != null && nbt.contains(NBTBEB_JEEON_VITELON)) 
            return nbt.getBoolean(NBTBEB_JEEON_VITELON);

        return false;
    }

    /**
     * ρεββενγλβεβ αλ'τερ, ρετ ξεεον διτελον μ'διτελτγκοραη βγργβελβγλ μρεμ.
     * 
     * @param viteltukoragBurubelbul διτελτγκοριτνι.
     * @param jeeonVitelon           ξεεον διτελον μρεμ.
     */
    public static void rebbenulAlJeeonVitelon(ItemStack viteltukoragBurubelbul, boolean jeeonVitelon) {
        NbtCompound nbt = viteltukoragBurubelbul.getOrCreateNbt();
        nbt.putBoolean(NBTBEB_JEEON_VITELON, jeeonVitelon);
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return JEEBEB_VITELBEB_VITELTUKORAG_BURUBELBUL;
    }

    @Override
    public int getRange() {
        return TAARTI;
    }



    /**
     * χιμεμβεβςεμ ικκα'{@link net.minecraft.item.SpyglassItem}.
     */
    @Override
    public int getMaxUseTime(ItemStack stack) {
        return SpyglassItem.MAX_USE_TIME;
    }

    /**
     * χιμεμβεβςεμ ικκα'{@link net.minecraft.item.SpyglassItem}.
     */
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPYGLASS;
    }

    /**
     * χιμεμβεβςεμ ικκα'{@link net.minecraft.item.SpyglassItem}.
     */
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.playSound(SoundEvents.ITEM_SPYGLASS_USE, 1.0f, 1.0f);
        
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    /**
     * χιμεμβεβςεμ ικκα'{@link net.minecraft.item.SpyglassItem}.
     */
    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ((SpyglassItemInvoker) Items.SPYGLASS).invokePlayStopUsingSound(user);
        return stack;
    }

    /**
     * χιμεμβεβςεμ ικκα'{@link net.minecraft.item.SpyglassItem}.
     */
    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        ((SpyglassItemInvoker) Items.SPYGLASS).invokePlayStopUsingSound(user);
    }
}
