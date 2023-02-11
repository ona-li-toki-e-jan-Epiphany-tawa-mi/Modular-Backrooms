package net.epiphany.features;

import com.mojang.serialization.Codec;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

/**
 * Used to conditionally generate walls on the south and east side of all chunks to form walls throught a level.
 */
public class ChunkWallFeature extends Feature<ChunkWallConfig> {
    public static final Identifier WALL_ID = new Identifier(ModularBackrooms.MOD_ID, "chunk_wall");
    public static final Feature<ChunkWallConfig> WALL_FEATURE = new ChunkWallFeature(ChunkWallConfig.CODEC);

    public ChunkWallFeature(Codec<ChunkWallConfig> configCodec) {
        super(configCodec);
    }



    @Override
    public boolean generate(FeatureContext<ChunkWallConfig> context) {
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();

        ChunkWallConfig config = context.getConfig();
        Identifier blockID = config.blockID();
        BlockState blockState = Registries.BLOCK.get(blockID).getDefaultState();
        int height = config.height();
        float wallChance = config.wallChance();
        float doorChance = config.doorChance();

        BlockPos wallPosition = context.getOrigin();

        if (random.nextFloat() < wallChance)
            generateWall(world, blockState, wallPosition, height, true, random.nextFloat() < doorChance);
        if (random.nextFloat() < wallChance)
            generateWall(world, blockState, wallPosition, height, false, random.nextFloat() < doorChance);

        return true;
    }

    /**
     * Generates a 16 block long wall, optionally with a door, starting at the given block and moving either south or east.
     * 
     * @param world        The world to write the wall to.
     * @param block        The block to make the wall out of.
     * @param wallPosition The starting position of the wall.
     * @param height       How tall the wall will be.
     * @param faceSouth    Whether the wall will generate to the south or east of the starting position.
     * @param generateDoor Whether to generate a 4 wide opening in the middle of the wall from bottom to top.
     */
    protected void generateWall(StructureWorldAccess world, BlockState block, BlockPos wallPosition, int height, 
            boolean faceSouth, boolean generateDoor) {
        for (int x = 0; x <= 16; x++) {
            if (!(generateDoor && (x >= 6 && x <= 10)))
                for (int y = 0; y < height; y++) 
                    world.setBlockState(wallPosition.add(0, y, 0), block, 0x0);

            wallPosition = faceSouth ? wallPosition.south() : wallPosition.east();
        }
    }
}
