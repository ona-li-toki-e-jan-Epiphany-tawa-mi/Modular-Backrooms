package net.epiphany.mdlrbckrms.levels.level0;

import java.util.Set;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.epiphany.mdlrbckrms.blocks.exitdoor.ExitDoorEvents;
import net.epiphany.mdlrbckrms.blocks.exitdoor.ExitDoorEvents.PortalDestination;
import net.epiphany.mdlrbckrms.blocks.rift.RiftEvents;
import net.epiphany.mdlrbckrms.utilities.DimensionHelper;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

/**
 * Methods and values used throughout Level 0's code.
 */
public class Level0 {
    public static final RegistryKey<World> LEVEL_0 = DimensionHelper.wrapDimensionID(
        new Identifier(ModularBackrooms.MOD_ID, "level_0"));

    public static void register(Set<RegistryKey<World>> backroomsLevelsSet) {
        backroomsLevelsSet.add(LEVEL_0);

        // Makes exit doors in level 0 lead to the overworld.
        ExitDoorEvents.TRY_CREATE_PORTAL.register((world, position, player) -> {
            if (!DimensionHelper.isDimension(world, LEVEL_0))
                return null;

            World overworld = world.getServer().getOverworld();
            return new PortalDestination(overworld.getSpawnPos(), overworld.getRegistryKey(), -1);
        }); 

        // Makes rifts in level 0 lead to the overworld.
        RiftEvents.ON_ENTITY_ENTER_RIFT.register((world, position, entity) -> {
            if (DimensionHelper.isDimension(world, LEVEL_0)) 
                return world.getServer().getOverworld().getRegistryKey();

            return null;
        });
    }
}
