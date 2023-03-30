package net.epiphany.mdlrbckrms.items.predicates;

import net.epiphany.mdlrbckrms.items.MBItems;
import net.epiphany.mdlrbckrms.items.ViteltukoragBurubelbul;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

/**
 * πγργηδγμ αλ'βγργβελ διτελτγκ μ'διτελτγκοραη βγργβελβγλ, νβεβον μρεμ.
 */
@Environment(EnvType.CLIENT)
public class PredicatebebViteloragBurubelbul {
    public static void register() {
        ModelPredicateProviderRegistry.register( MBItems.VITELTUKORAG_BURUBELBUL
                                               , new Identifier("jeeon_vitelon")
                                               , (jee, _ekbebTep, livingEntity, seed) -> {
            return ViteltukoragBurubelbul.ximemAlJeeonVitelon(jee) ? 1.0f : 0.0f;
        });
    }
}
