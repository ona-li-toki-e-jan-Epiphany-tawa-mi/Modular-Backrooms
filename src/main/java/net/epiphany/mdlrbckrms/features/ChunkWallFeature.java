package net.epiphany.mdlrbckrms.features;

import com.mojang.serialization.Codec;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
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
    public static final Identifier CHUNK_WALL_ID = new Identifier(ModularBackrooms.MOD_ID, "chunk_wall");
    public static final Feature<ChunkWallConfig> CHUNK_WALL_FEATURE = new ChunkWallFeature(ChunkWallConfig.CODEC);

    public static void register() {
        Registry.register(Registries.FEATURE, ChunkWallFeature.CHUNK_WALL_ID, ChunkWallFeature.CHUNK_WALL_FEATURE);
    }

    public ChunkWallFeature(Codec<ChunkWallConfig> configCodec) {
        super(configCodec);
    }



    @Override
    public boolean generate(FeatureContext<ChunkWallConfig> context) {
        ChunkWallConfig config = context.getConfig();

        Identifier blockID = config.blockID();
        BlockState blockState = Registries.BLOCK.get(blockID).getDefaultState();
        if (blockState == null)
            throw new IllegalStateException(blockID + " could not be parsed to a valid block identifier!");

        int height = config.height();
        if (height < 1)
            throw new IllegalStateException("Chunk wall height cannot be less than 1! (recieved height of " + height + ")");

        float wallChance = config.wallChance();
        if (wallChance < 0.0f || wallChance > 1.0f)
            throw new IllegalStateException( "Chunk wall wall chance must be between 0 and 1! (recieved chance of " + wallChance 
                                           + ")");

        float openingChance = config.openingChance();
        if (openingChance < 0.0f || openingChance > 1.0f)
            throw new IllegalStateException( "Chunk wall opening chance must be between 0 and 1! (recieved chance of " 
                                           + openingChance + ")");



                                           
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos wallOrigin = context.getOrigin();

        if (random.nextFloat() < wallChance)
            generateWall(world, blockState, wallOrigin, height, true, random.nextFloat() < openingChance);
        if (random.nextFloat() < wallChance)
            generateWall(world, blockState, wallOrigin, height, false, random.nextFloat() < openingChance);

        return true;
    }

    /**
     * Generates a 16 block long wall, optionally with a door, starting at the given block and moving either south or east.
     * 
     * @param world           The world to write the wall to.
     * @param block           The block to make the wall out of.
     * @param wallPosition    The starting position of the wall.
     * @param height          How tall the wall will be.
     * @param faceSouth       Whether the wall will generate to the south or east of the starting position.
     * @param generateOpening Whether to generate a 4 wide opening in the middle of the wall from bottom to top.
     */
    protected void generateWall(StructureWorldAccess world, BlockState block, BlockPos wallPosition, int height, 
            boolean faceSouth, boolean generateOpening) {
        for (int x = 0; x <= 16; x++) {
            if (!(generateOpening && (x >= 6 && x <= 10)))
                for (int y = 0; y < height; y++) 
                    world.setBlockState(wallPosition.add(0, y, 0), block, 0x0);

            wallPosition = faceSouth ? wallPosition.south() : wallPosition.east();
        }
    }
}
