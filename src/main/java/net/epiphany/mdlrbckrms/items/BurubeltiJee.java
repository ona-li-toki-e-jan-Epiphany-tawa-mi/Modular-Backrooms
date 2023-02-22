package net.epiphany.mdlrbckrms.items;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * α?, βγργβελτιον ξεε λα? νιτνιον νιζεζ λα?
 */
public class BurubeltiJee extends Item {
    public static final Identifier TJUERTMIKBEB_BURUBELTI = new Identifier(ModularBackrooms.MOD_ID, "burubelti");
    public static final BurubeltiJee BURUBELTI = new BurubeltiJee((new FabricItemSettings()).maxCount(16));

    public static void register() {
        Registry.register(Registries.ITEM, TJUERTMIKBEB_BURUBELTI, BURUBELTI);
        CompostingChanceRegistry.INSTANCE.add(BURUBELTI, 1.0f);
    }

    public static void registerItemUnderGroup(FabricItemGroupEntries content) {
        content.add(BURUBELTI);
    }

    public BurubeltiJee(Settings settings) {
        super(settings);
    }
    
    

    // TODO πγργηαπαξε αλ'model
    // TODO πγργηξε αλ'sound-βγλ ον'κελβεβ βγργβελτι.
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World _ek = context.getWorld(); // _ek-ον ερττγκ "!εκ".
        if (_ek.isClient)
            return ActionResult.success(true);

        ItemStack jeeBurubelti = context.getStack();
        BlockPos mtukPiztum = context.getBlockPos().offset(context.getSide());

        ChickenEntity burubelti = new ChickenEntity(EntityType.CHICKEN, _ek);
        // πιζτγμ'ξεε βγργβελτι τξγερττγκον, μρεμ χλεμγλαδεκ τξερττελ αλ'βγργβελτι ορ'τξγερττγκιτνι.
        if (jeeBurubelti.hasCustomName())
            burubelti.setCustomName(jeeBurubelti.getName());
        burubelti.setPosition(mtukPiztum.getX() + 0.5, mtukPiztum.getY(), mtukPiztum.getZ() + 0.5);
        _ek.spawnEntity(burubelti);

        jeeBurubelti.decrement(1);
        burubelti.playAmbientSound();
        return ActionResult.success(false);
    }
}
