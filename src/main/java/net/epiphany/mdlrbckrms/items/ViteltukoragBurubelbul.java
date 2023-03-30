package net.epiphany.mdlrbckrms.items;

import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.Vanishable;
import net.minecraft.nbt.NbtCompound;

/**
 * TODO
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
    
}
