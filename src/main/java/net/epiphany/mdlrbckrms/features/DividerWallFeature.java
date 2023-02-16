package net.epiphany.mdlrbckrms.features;

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

        float faceSouthChance = config.faceSouthChance();
        if (faceSouthChance < 0.0f || faceSouthChance > 1.0f)
            throw new IllegalStateException( "Divider wall face south chance must be between 0 and 1! (recieved chance of " 
                                           + faceSouthChance + ")");
        
        

        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos wallPosition = context.getOrigin();
        int height = random.nextBetween(minimumHeight, maximumHeight);
        int length = random.nextBetween(minimumLength, maximumLength);
        boolean faceSouth = random.nextFloat() < faceSouthChance;

    HorizontalWalk:
        for (int x = 0; x <= length; x++) {
            for (int y = 0; y < height; y++)
                if (!world.getBlockState(wallPosition.up(y)).isAir())
                    break HorizontalWalk;

            for (int y = 0; y < height - 1; y++)
                world.setBlockState(wallPosition.up(y), mainBlockState, 0x0);
            world.setBlockState(wallPosition.up(height - 1), topBlockState, 0x0);

            wallPosition = faceSouth ? wallPosition.south() : wallPosition.east();
        }

        return true;
    }
}
