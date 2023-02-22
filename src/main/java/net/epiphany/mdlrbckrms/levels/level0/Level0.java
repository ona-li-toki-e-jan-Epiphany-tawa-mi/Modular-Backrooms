package net.epiphany.mdlrbckrms.levels.level0;

import java.util.Set;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.epiphany.mdlrbckrms.utilities.DimensionHelper;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

/**
 * Methods and values used throughout Level 0's code.
 */
public class Level0 {
    /**
     * General identifier path for things related to Level 0.
     */
    public static final String LEVEL_0_ID = "level_0";

    public static final RegistryKey<World> LEVEL_0_DIMENSION_KEY = DimensionHelper.wrapDimensionID(
        new Identifier(ModularBackrooms.MOD_ID, LEVEL_0_ID));

    public static void register(Set<RegistryKey<World>> backroomsLevelsSet) {
        backroomsLevelsSet.add(LEVEL_0_DIMENSION_KEY);
    }
}
