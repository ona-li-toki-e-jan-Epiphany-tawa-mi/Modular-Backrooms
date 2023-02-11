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
