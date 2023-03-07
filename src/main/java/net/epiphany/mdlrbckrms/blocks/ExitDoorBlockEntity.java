package net.epiphany.mdlrbckrms.blocks;

import org.jetbrains.annotations.Nullable;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ExitDoorBlockEntity extends BlockEntity {
    public static final Identifier EXIT_DOOR_BLOCK_ENTITY_ID = new Identifier(ModularBackrooms.MOD_ID, "exit_door_entity");
    public static BlockEntityType<ExitDoorBlockEntity> EXIT_DOOR_BLOCK_ENTITY = null;
            

    public static void register() {
        EXIT_DOOR_BLOCK_ENTITY = Registry.register( Registries.BLOCK_ENTITY_TYPE
                                                  , EXIT_DOOR_BLOCK_ENTITY_ID
                                                  , FabricBlockEntityTypeBuilder.create( ExitDoorBlockEntity::new
                                                                                       , ExitDoorBlock.EXIT_DOOR)
                                                                                .build());
    }



    public static final String PORTAL_LIFESPAN_NBT = "PortalLifespan";
    public static final String DESTINATION_NBT = "Destination";

    /**
     * The time in ticks before the portal in the exit door will attempt to close itself.
     */
    private int portalLifespan = 0;
    /**
     * The destination where any entities entering the exit door will be transported to, or null, if there is none.
     */
    @Nullable
    private BlockPos destination = null; 

    public ExitDoorBlockEntity(BlockPos position, BlockState state) {
        super(EXIT_DOOR_BLOCK_ENTITY, position, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        nbt.putInt(PORTAL_LIFESPAN_NBT, this.portalLifespan);
        if (this.destination != null)
            nbt.putIntArray(DESTINATION_NBT, new int[] { this.destination.getX()
                                                       , this.destination.getY()
                                                       , this.destination.getZ()});
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
    }



    public boolean hasPortal() {
        return this.destination != null;
    }

    @Nullable
    public BlockPos getDestination() {
        return this.destination;
    }

    public void createPortal(BlockPos destination) {
        this.createPortal(destination, -1);
    }

    public void createPortal(BlockPos destination, int portalLifespan) {
        this.destination = destination;
        this.portalLifespan = portalLifespan;
    }

    public void removePortal() {
        this.destination = null;
        this.portalLifespan = 0;
    }
}
