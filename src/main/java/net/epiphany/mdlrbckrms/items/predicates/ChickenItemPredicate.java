package net.epiphany.mdlrbckrms.items.predicates;

import net.epiphany.mdlrbckrms.items.ChickenItem;
import net.epiphany.mdlrbckrms.items.MBItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

/**
 * Used to conditionally render the chicken item as red when it is "damaged."
 */
@Environment(EnvType.CLIENT)
public class ChickenItemPredicate {
    public static void register() {
        ModelPredicateProviderRegistry.register( MBItems.CHICKEN
                                               , new Identifier("damaged")
                                               , (item, clientWorld, livinEntity, seed) -> {
            NbtCompound nbt = item.getNbt();
            if (nbt == null)                                     
                return 0.0f;
                
            return nbt.getInt(ChickenItem.DAMAGE_TIME_NBT) > 0 ? 1.0f : 0.0f;
        });
    }
}
