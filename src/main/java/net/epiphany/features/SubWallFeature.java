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
public class SubWallFeature extends Feature<SubWallConfig> {
    public static final Identifier SUBWALL_ID = new Identifier(ModularBackrooms.MOD_ID, "subwall");
    public static final Feature<SubWallConfig> SUBWALL_FEATURE = new SubWallFeature(SubWallConfig.CODEC);

    public SubWallFeature(Codec<SubWallConfig> configCodec) {
        super(configCodec);
    }



    @Override
    public boolean generate(FeatureContext<SubWallConfig> context) {
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();

        SubWallConfig config = context.getConfig();
        BlockState blockState = Registries.BLOCK.get(config.blockID()).getDefaultState();
        BlockState topBlockState = Registries.BLOCK.get(config.topBlockID()).getDefaultState(); 
        int height = random.nextBetween(config.minimumHeight(), config.maximumHeight());
        int length = random.nextBetween(config.minimumLength(), config.maximumLength());
        boolean faceSouth = config.faceSouthChance() > random.nextFloat();

        BlockPos wallPosition = context.getOrigin();

    HorizontalWalk:
        for (int x = 0; x <= length; x++) {
            for (int y = 0; y < height; y++)
                if (!world.getBlockState(wallPosition.up(y)).isAir())
                    break HorizontalWalk;

            for (int y = 0; y < height - 1; y++)
                world.setBlockState(wallPosition.up(y), blockState, 0x0);
            world.setBlockState(wallPosition.up(height - 1), topBlockState, 0x0);

            wallPosition = faceSouth ? wallPosition.south() : wallPosition.east();
        }

        return true;
    }
}
