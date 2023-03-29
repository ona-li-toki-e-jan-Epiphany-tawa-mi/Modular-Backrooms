package net.epiphany.mdlrbckrms.worldgen.features.fluorescentlightarray;

import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

/**
 * Used to generate arrays of light fixtures.
 */
public class FluorescentLightArrayFeature extends Feature<FluorescentLightArrayConfig> {
    public FluorescentLightArrayFeature(Codec<FluorescentLightArrayConfig> configCodec) {
        super(configCodec);
    }

    

    @Override
    public boolean generate(FeatureContext<FluorescentLightArrayConfig> context) {
        FluorescentLightArrayConfig config = context.getConfig();

        Identifier lightBlockID = config.lightBlockID();
        BlockState lightBlockState = Registries.BLOCK.get(lightBlockID).getDefaultState();
        if (lightBlockState == null) 
            throw new IllegalStateException(lightBlockID + " could not be parsed to a valid block identifier!");

        int length = config.length();
        if (length < 1) 
            throw new IllegalStateException( "Fluorescent light array lights cannot be shorter than 1 block! (recieved length of " 
                                           + length + ")");

        int columns = config.columns();
        if (columns < 1) 
            throw new IllegalStateException( "Fluorescent light arrays must have at least 1 column! (recieved " + columns 
                                           + " columns)");

        int rows = config.rows();
        if (rows < 1) 
            throw new IllegalStateException( "Fluorescent light arrays must have at least 1 row! (recieved " + rows + " rows)");

        int xSpacing = config.xSpacing();
        if (xSpacing < 0) 
            throw new IllegalStateException( "Fluorescent light array lights must have a spacing on the x-axis that is greater than or"
                                           + " equal to 0! (recieved spacing of " + xSpacing + ")");

        int zSpacing = config.zSpacing();
        if (zSpacing < 0) 
            throw new IllegalStateException( "Fluorescent light array lights must have a spacing on the z-axis that is greater than or"
                                           + " equal to 0! (recieved spacing of " + zSpacing + ")");


                                           
        // How much to move the starting position back by to center the array on the origin.
        int xCenteringOffset = (columns       + (columns-1) * xSpacing) / 2;
        int zCenteringOffset = (rows * length + (rows-1) * zSpacing)    / 2;
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();

        BlockPos.Mutable workingPosition = new BlockPos.Mutable();

        for (int column = 0; column < columns; column++)
            for (int row = 0; row < rows; row++) {
                int x = column       + column * xSpacing + origin.getX() - xCenteringOffset;
                int z = row * length + row * zSpacing    + origin.getZ() - zCenteringOffset;

                for (int i = 0; i < length; i++) {
                    workingPosition.set(x, origin.getY(), z + i);
                    world.setBlockState(workingPosition, lightBlockState, 0x0);
                }
            }

        return true;
    }
}
