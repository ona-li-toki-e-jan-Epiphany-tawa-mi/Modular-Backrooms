package net.epiphany.mdlrbckrms.worldgen.features.walleddoor;

import java.util.Optional;

import com.mojang.serialization.Codec;

import net.epiphany.mdlrbckrms.worldgen.features.MBFeatures;
import net.epiphany.mdlrbckrms.worldgen.features.MBFeatures.PillarCondition;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

/**
 * Used to generate random doors in the walls of the backrooms.
 */
public class WalledDoorFeature extends Feature<WalledDoorConfig> {
    public WalledDoorFeature(Codec<WalledDoorConfig> configCodec) {
        super(configCodec);
    }

    

    /**
     * The positions of the block neighboring the possible door on the x-axis.
     */
    private static final Vec3i[] X_AXIS_NEIGHBOR_POSITIONS = { new Vec3i(-1, 0, 0), new Vec3i(-1, 1, 0)
                                                             , new Vec3i(1, 0, 0), new Vec3i(1, 1, 0)};
    /**
     * The positions of the block neighboring the possible door on the z-axis.
     */
    private static final Vec3i[] Z_AXIS_NEIGHBOR_POSITIONS = { new Vec3i(0, 0, -1), new Vec3i(0, 1, -1)
                                                             , new Vec3i(0, 0, 1), new Vec3i(0, 1, 1)};

    @Override
    public boolean generate(FeatureContext<WalledDoorConfig> context) {
        WalledDoorConfig config = context.getConfig();

        Identifier doorBlockID = config.doorBlockID();
        BlockState defaultDoorState = Registries.BLOCK.get(doorBlockID).getDefaultState();
        // Checks to make sure the block ID is actually a door.
        if (defaultDoorState == null || !(defaultDoorState.getBlock() instanceof DoorBlock))
            throw new IllegalStateException(doorBlockID + " could not be parsed to a valid door block identifier!");

        float openChance = MathHelper.clamp(config.openChance(), 0.0f, 1.0f);

        boolean canPlaceDoubleDoors = config.canPlaceDoubleDoors();

            

        StructureWorldAccess world = context.getWorld();
        BlockPos doorOrigin = context.getOrigin();

        // Checks if door is being placed in already existing blocks (wall check part 1).
        if (!world.getBlockState(doorOrigin).isSolidBlock(world, doorOrigin) || !world.getBlockState(doorOrigin).isSolidBlock(world, doorOrigin))
            return false;


        // Checks if blocks surround the door on either the x or z axis, inidicating that is indeed in a wall.
        boolean walledInOnXAxis = true
              , walledInOnZAxis = true;

        for (Vec3i possibleWallBlock : X_AXIS_NEIGHBOR_POSITIONS) {
            BlockPos position = doorOrigin.add(possibleWallBlock);

            if (!world.getBlockState(position).isSolidBlock(world, position)) {
                walledInOnXAxis = false;
                break;
            }
        }
        for (Vec3i possibleWallBlock : Z_AXIS_NEIGHBOR_POSITIONS) {
            BlockPos position = doorOrigin.add(possibleWallBlock);

            if (!world.getBlockState(position).isSolidBlock(world, position)) {
                walledInOnZAxis = false;
                break;
            }
        }

        if ((walledInOnXAxis && walledInOnZAxis) || (!walledInOnXAxis && !walledInOnZAxis))
            return false;


        // Checks in what directions are avalible to place in (not walled off) and chooses a facing for the door.
        Random random = context.getRandom();
        Optional<Direction> possibleDoorFacing;

        if (walledInOnXAxis) {
            possibleDoorFacing = determineFacing(world, random, doorOrigin, Direction.NORTH);
        } else 
            possibleDoorFacing = determineFacing(world, random, doorOrigin, Direction.EAST);

        if (!possibleDoorFacing.isPresent())
            return false;


        Direction doorFacing = possibleDoorFacing.get();
        DoorHinge doorHinge = random.nextBoolean() ? DoorHinge.LEFT : DoorHinge.RIGHT;
        boolean open = random.nextFloat() < openChance;
        boolean doorToRight = world.getBlockState(doorOrigin.offset(doorFacing.rotateYClockwise())).getBlock() instanceof DoorBlock
              , doorToLeft  = world.getBlockState(doorOrigin.offset(doorFacing.rotateYCounterclockwise())).getBlock() instanceof DoorBlock;

        // Ensures that if the door generates next to another door it will match up or not generate. Also prevents any doors larger
        //   than double-doors.
        if (doorToRight && doorToLeft) {
            return false;
        
        } else if (doorToRight || doorToLeft) {
            if (!canPlaceDoubleDoors)
                return false;

            BlockPos otherDoorPosition = doorOrigin.offset(doorToRight ? doorFacing.rotateYClockwise() 
                                                                       : doorFacing.rotateYCounterclockwise());
            BlockState otherDoorState = world.getBlockState(otherDoorPosition);
            DoorHinge otherDoorHinge = otherDoorState.get(DoorBlock.HINGE);

            // Ensures both doors hinge correctly.
            if (doorToRight && !DoorHinge.RIGHT.equals(otherDoorHinge)) {
                return false;
            } else if (doorToLeft && !DoorHinge.LEFT.equals(otherDoorHinge)) {
                return false;
            } else 
                doorHinge = DoorHinge.LEFT.equals(otherDoorHinge) ? DoorHinge.RIGHT : DoorHinge.LEFT;

            doorFacing = otherDoorState.get(DoorBlock.FACING);
            open = otherDoorState.get(DoorBlock.OPEN);
        }


        // Acutally places the door.
        BlockState directedDoorState = defaultDoorState.with(DoorBlock.FACING, doorFacing)
                                                       .with(DoorBlock.HINGE,  doorHinge)
                                                       .with(DoorBlock.OPEN,   open);
        world.setBlockState(doorOrigin, directedDoorState.with(DoorBlock.HALF, DoubleBlockHalf.LOWER), 0);
        world.setBlockState(doorOrigin.up(), directedDoorState.with(DoorBlock.HALF, DoubleBlockHalf.UPPER), 0);

        return true;
    }

    /**
     * Determines whether a door can face in the given direction or it's opposite, or neither.
     * 
     * Should both directions be possible one will be chosen at random.
     * 
     * @param world      The world the door will be generated in.
     * @param random     Random number generator.
     * @param doorOrigin The position of the lower half of the door.
     * @param direction  The direction the door will face in, also checks and can return it's opposite.
     * @return Optionally, the direction the door can face.
     */
    private Optional<Direction> determineFacing(WorldAccess world, Random random, BlockPos doorOrigin, Direction direction) {
        Direction opposite = direction.getOpposite();

        // We need to check the opposite direction because the "facing" of the door is to the inside, but it needs to be coded with 
        //  thinking of it as to the outside for the placement checking to work..
        boolean canFaceDirection = MBFeatures.testPillar( world
                                                        , doorOrigin.offset(direction.getOpposite())
                                                        , 2
                                                        , PillarCondition.REPLACABLE)
              , canFaceOpposite  = MBFeatures.testPillar( world
                                                        , doorOrigin.offset(opposite.getOpposite())
                                                        , 2
                                                        , PillarCondition.REPLACABLE);
        
        if (!canFaceDirection && !canFaceOpposite)
            return Optional.empty();

        if (canFaceDirection && canFaceOpposite) {
            return Optional.of(random.nextBoolean() ? direction : opposite);
        } else if (canFaceDirection) {
            return Optional.of(direction);
        } else
            return Optional.of(opposite);
    }
}
