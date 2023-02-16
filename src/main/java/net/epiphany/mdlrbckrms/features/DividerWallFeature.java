package net.epiphany.mdlrbckrms.features;

import java.util.LinkedList;
import java.util.List;

import com.mojang.serialization.Codec;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

// TODO Possibly remove ability to generate in door ways (idk i kinda like it.)

/**
 * Used to generate smaller subwalls throught a level that split up rooms.
 */
public class DividerWallFeature extends Feature<DividerWallConfig> {
    public static final Identifier DIVIDER_WALL_ID = new Identifier(ModularBackrooms.MOD_ID, "divider_wall");
    public static final Feature<DividerWallConfig> DIVIDER_WALL_FEATURE = new DividerWallFeature(DividerWallConfig.CODEC);

    public DividerWallFeature(Codec<DividerWallConfig> configCodec) {
        super(configCodec);
    }



    @Override
    public boolean generate(FeatureContext<DividerWallConfig> context) {
        DividerWallConfig config = context.getConfig();

        Identifier mainBlockID = config.mainBlockID();
        BlockState mainBlockState = Registries.BLOCK.get(mainBlockID).getDefaultState();
        if (mainBlockState == null)
            throw new IllegalStateException(mainBlockID + " could not be parsed to a valid block identifier!");

        Identifier topBlockID = config.topBlockID();
        BlockState topBlockState = Registries.BLOCK.get(topBlockID).getDefaultState(); 
        if (topBlockState == null)
            throw new IllegalStateException(topBlockID + " could not be parsed to a valid block identifier!");

        int minimumHeight = config.minimumHeight()
          , maximumHeight = config.maximumHeight();
        if (minimumHeight < 1 || maximumHeight < 1)
            throw new IllegalStateException( "Divider wall minimum and maximum heights cannot be less than 1! (recieved a minimum"
                                           + " of " + minimumHeight + " and a maximum of " + maximumHeight + ")");
        if (minimumHeight > maximumHeight)
            throw new IllegalStateException( "Divider wall minimum height cannot be greater than the maximum! (recieved a minimum"
                                           + " of " + minimumHeight + " and a maximum of " + maximumHeight + ")");

        int minimumLength = config.minimumHeight()
          , maximumLength = config.maximumHeight();
        if (minimumLength < 1 || maximumLength < 1)
            throw new IllegalStateException( "Divider wall minimum and maximum lengths cannot be less than 1! (recieved a minimum"
                                           + " of " + minimumLength + " and a maximum of " + maximumLength + ")");
        if (minimumLength > maximumLength)
            throw new IllegalStateException( "Divider wall minimum length cannot be greater than the maximum! (recieved a minimum"
                                           + " of " + minimumLength + " and a maximum of " + maximumLength + ")");
        
        

        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos wallOrigin = context.getOrigin();
        int height = random.nextBetween(minimumHeight, maximumHeight);

        // Ensures that the divider wall builds out from an existing wall of at least the same height because it looks better.
        boolean startsInWall = testPillar(world, wallOrigin, height, true);
        if (!startsInWall)
            return false;
        

        // Finds open space to start building the divider wall into and ensures it isn't going to be butted up with another wall.
        List<Direction> validDirections = new LinkedList<>();

        for (Direction direction : Direction.Type.HORIZONTAL) {
            BlockPos possibleDirection = wallOrigin.offset(direction);

            if ( testPillar(world, possibleDirection, height, false)
                    && testPillar(world, possibleDirection.offset(direction.rotateYClockwise()), height, false)
                    && testPillar(world, possibleDirection.offset(direction.rotateYCounterclockwise()), height, false))
                validDirections.add(direction);
        }

        if (validDirections.isEmpty())
            return false;


        // Actually builds the wall. If a collision occurs building will just be stopped.
        Direction buildDirection = validDirections.get(random.nextInt(validDirections.size()));
        int length = random.nextBetween(minimumLength, maximumLength);
        wallOrigin = wallOrigin.offset(buildDirection);
        
        for (int x = 0; x <= length; x++) {
            if (!testPillar(world, wallOrigin, height, false))
                break;

            for (int y = 0; y < height - 1; y++)
                world.setBlockState(wallOrigin.up(y), mainBlockState, 0x0);
            world.setBlockState(wallOrigin.up(height - 1), topBlockState, 0x0);

            wallOrigin = wallOrigin.offset(buildDirection);
        }

        return true;
    }

    /**
     * Tests to see if a pillar of blocks meet the given condition.
     * 
     * @param world The world the pillar is in,.
     * @param pillarOrigin The position of the bottom block of the pillar.
     * @param height The height of the pillar.
     * @param solidOrAir If true, tests if the pillar is completely soild (non-air.) If false, tests if its completely air.
     */
    private static boolean testPillar(WorldAccess world, BlockPos pillarOrigin, int height, boolean solidOrAir) {
        for (int i = 0; i < height; i++) {
            if (world.getBlockState(pillarOrigin).isAir() == solidOrAir) 
                return false;

            pillarOrigin = pillarOrigin.up();
        }

        return true;
    }
}
