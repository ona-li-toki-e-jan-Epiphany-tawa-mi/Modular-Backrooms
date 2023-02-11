package net.epiphany.features;

import com.mojang.serialization.Codec;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class Wall extends Feature<WallConfig> {
    public static final Identifier WALL_ID = new Identifier(ModularBackrooms.MOD_ID, "wall");
    public static Feature<WallConfig> WALL_FEATURE = new Wall(WallConfig.CODEC);

    public Wall(Codec<WallConfig> configCodec) {
        super(configCodec);
    }



    @Override
    public boolean generate(FeatureContext<WallConfig> context) {
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos origin = context.getOrigin();

        WallConfig config = context.getConfig();
        Identifier blockID = config.blockID();
        BlockState blockState = Registries.BLOCK.get(blockID).getDefaultState();

        BlockPos wallPosition = new BlockPos(origin).withY(-2);
        int wallLength = random.nextBetween(1, 10);
        boolean faceNorth = random.nextBoolean();

        if (world.getBlockState(wallPosition).isOf(Blocks.VOID_AIR)) {
            for (int i = 0; i < wallLength; i++) {
                BlockPos wallUpper = new BlockPos(wallPosition);

                for (int k = 0; k < 4; k++) {
                    world.setBlockState(wallUpper, blockState, 0x0);
                    wallUpper = wallUpper.up();
                }
 
                wallPosition = faceNorth ? wallPosition.north() : wallPosition.west();
            }

            return true;
        }

        return false;
    }
}
