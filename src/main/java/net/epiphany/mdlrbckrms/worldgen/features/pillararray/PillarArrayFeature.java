package net.epiphany.mdlrbckrms.worldgen.features.pillararray;

import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

/**
 * Used to generate arrays of pillars.
 */
public class PillarArrayFeature extends Feature<PillarArrayConfig> {
    public PillarArrayFeature(Codec<PillarArrayConfig> configCodec) {
        super(configCodec);
    }

    

    @Override
    public boolean generate(FeatureContext<PillarArrayConfig> context) {
        PillarArrayConfig config = context.getConfig();

        Identifier blockID = config.blockID();
        BlockState blockState = Registries.BLOCK.get(blockID).getDefaultState();
        if (blockState == null) 
            throw new IllegalStateException(blockID + " could not be parsed to a valid block identifier!");

        int length = config.length();
        if (length < 1) 
            throw new IllegalStateException( "Pillar arrays' length cannot be less than 1 block! (recieved length of " + length + ")");

        int height = config.height();
        if (height < 1) 
            throw new IllegalStateException( "Pillar arrays cannot be shorter than 1 block! (recieved height of " + length + ")");

        int columns = config.columns();
        if (columns < 1 || columns > 15) 
            throw new IllegalStateException( "Pillar arrays must have between 1 and 15 columns! (recieved " + columns + " columns)");

        int rows = config.rows();
        if (rows < 1 || rows > 15) 
            throw new IllegalStateException( "Pillar arrays must have between 1 and 15 rows! (recieved " + rows + " rows)");

        int xSpacing = config.xSpacing();
        if (xSpacing < 0) 
            throw new IllegalStateException( "Pillar arrays must have a spacing on the x-axis that is greater than or equal to 0! "
                                           + "(recieved spacing of " + xSpacing + ")");

        int zSpacing = config.zSpacing();
        if (zSpacing < 0) 
            throw new IllegalStateException( "Pillar arrays must have a spacing on the z-axis that is greater than or equal to 0! "
                                           + "(recieved spacing of " + zSpacing + ")");



        // How much to move the starting position back by to center the array on the origin.
        int xCenteringOffset = (columns * length + (columns-1) * xSpacing) / 2;
        int zCenteringOffset = (rows * length    + (rows-1) * zSpacing)    / 2;
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();

        BlockPos.Mutable workingPosition = new BlockPos.Mutable();

        for (int column = 0; column < columns; column++)
            for (int row = 0; row < rows; row++) {
                int startX = column * length + column * xSpacing + origin.getX() - xCenteringOffset;
                int startZ = row * length    + row * zSpacing    + origin.getZ() - zCenteringOffset;

                for (int x = startX; x < startX + length; x++) 
                    for (int z = startZ; z < startZ + length; z++)
                        for (int y = origin.getY(); y < origin.getY() + height; y++) {
                            workingPosition.set(x, y, z);
                            world.setBlockState(workingPosition, blockState, 0x0);
                        }
            }

        return true;
    }
}
