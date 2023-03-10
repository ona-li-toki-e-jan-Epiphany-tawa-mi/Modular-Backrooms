package net.epiphany.mdlrbckrms.blocks.exitdoor;

import org.jetbrains.annotations.Nullable;

import net.epiphany.mdlrbckrms.blocks.MBBlocks;
import net.epiphany.mdlrbckrms.utilities.DimensionHelper;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A block entity that keeps track of data for the portals within exit doors.
 */
public class ExitDoorBlockEntity extends BlockEntity {
    public static final BlockEntityType<ExitDoorBlockEntity> EXIT_DOOR_ENTITY =
            FabricBlockEntityTypeBuilder.create(ExitDoorBlockEntity::new, MBBlocks.INTERDIMENSIONAL_EXIT_DOOR).build();

    public static void register() {
        MBBlocks.registerBlockEntityType("exit_door_entity", EXIT_DOOR_ENTITY);
    }
            


    public static final String PORTAL_LIFESPAN_NBT = "PortalLifespan";
    public static final String DESTINATION_NBT = "Destination";
    public static final String DESTINATION_DIMENSION_NBT = "DestinationDimension";

    /**
     * The time in ticks before the portal in the exit door will attempt to close itself.
     */
    private int portalLifespan = -1;
    /**
     * The destination where any entities entering the exit door will be transported to, or null, if there is none.
     */
    @Nullable
    private BlockPos destination = null; 
    /**
     * The world the destination lies in.
     */
    @Nullable
    private RegistryKey<World> destinationDimension = null;

    public ExitDoorBlockEntity(BlockPos position, BlockState state) {
        super(EXIT_DOOR_ENTITY, position, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        nbt.putInt(PORTAL_LIFESPAN_NBT, this.portalLifespan);
        if (this.destination != null)
            nbt.putIntArray(DESTINATION_NBT, new int[] { this.destination.getX()
                                                       , this.destination.getY()
                                                       , this.destination.getZ()});
        if (this.destinationDimension != null) 
            nbt.putString(DESTINATION_DIMENSION_NBT, destinationDimension.getValue().toString());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        this.portalLifespan = nbt.getInt(PORTAL_LIFESPAN_NBT);

        int[] possibleDestination = nbt.getIntArray(DESTINATION_NBT);
        this.destination = possibleDestination.length == 3 ? new BlockPos( possibleDestination[0]
                                                                         , possibleDestination[1]
                                                                         , possibleDestination[2])
                                                           : null;

        String possibleDestinationDimension = nbt.getString(DESTINATION_DIMENSION_NBT);
        this.destinationDimension = !possibleDestinationDimension.isEmpty() 
                ? DimensionHelper.wrapDimensionID(new Identifier(possibleDestinationDimension)) : null;
    }



    public static void tick(World world, BlockPos position, BlockState state, ExitDoorBlockEntity blockEntity) {
        if (blockEntity.portalLifespan == 0) {
            blockEntity.removePortal();
            MBBlocks.INTERDIMENSIONAL_EXIT_DOOR.setOpen( null
                                                       , world
                                                       , state.with(ExitDoorBlock.PORTAL, false)
                                                       , position
                                                       , false);

        } else if (blockEntity.portalLifespan > 0) 
            blockEntity.portalLifespan--; 
    }



    /**
     * Attempts to send the entity through the exit door's portal.
     * 
     * @param entity The entity to send.
     * @return {@code null} if the portal does not exist. See {@link DimensionHelper#teleportToDimension(Entity, RegistryKey, BlockPos, boolean)} for more return values.
     */
    @Nullable
    public Entity tryTeleportEntity(Entity entity) {
        if (!this.hasPortal())
            return null;

        return DimensionHelper.teleportToDimension( entity
                                                  , this.destinationDimension
                                                  , this.getDestination().toCenterPos()
                                                  , true);
    }



    /**
     * @return Whether the exit door portal exists, as-in it has a destination.
     */
    public boolean hasPortal() {
        return this.destination != null;
    }

    /**
     * @return The destination of the exit door portal, or null, if it doesn't exist.
     */
    @Nullable
    public BlockPos getDestination() {
        return this.destination;
    }

    /**
     * @return The dimension of the destination, or null, if it doesn't exist.
     */
    @Nullable
    public RegistryKey<World> getDestinationDimension() {
        return this.destinationDimension;
    }

    /**
     * @return The lifespan of the portal in ticks. 
     * 
     * If there is a portal and the lifespan is -1, the portal will never close on it's own.
     */
    public int getPortalLifespan() {
        return portalLifespan;
    }

    /**
     * Creates a permanent portal in the exit door leading to the destination.
     * 
     * @param destination The destination of the portal.
     * @param dimension   The dimension of the destination.
     */
    public void createPortal(BlockPos destination, RegistryKey<World> dimension) {
        this.createPortal(destination, dimension, -1);
    }

    /**
     * Creates a temporary portal in the exit door leading to the destination.
     * 
     * @param destination    The destination of the portal.
     * @param dimension      The dimension of the destination.
     * @param portalLifespan The time, in ticks, that the portal will exist for.
     */
    public void createPortal(BlockPos destination, RegistryKey<World> dimension, int portalLifespan) {
        this.destination = destination;
        this.destinationDimension = dimension;
        this.portalLifespan = portalLifespan;
    }

    /**
     * Clears the portal state of the exit door.
     */
    public void removePortal() {
        this.destination = null;
        this.destinationDimension = null;
        this.portalLifespan = -1;
    }
}
