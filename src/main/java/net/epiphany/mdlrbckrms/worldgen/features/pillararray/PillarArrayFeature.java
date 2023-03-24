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
 * Used to generate arrays of pillars. TODO
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

                                           

        // Calculates how many blocks need to be in between pillar in each row and column and how far from the wall they 
        //   should be. Note: does not account for pillar thickness.
        int columnSpacing = (16 - columns) / (columns + 1);
        int rowSpacing = (16 - rows) / (rows + 1);
        int pillarCenterOffset = length / 2;
        // Offset by (1,0,1) to account for chunk walls.
        BlockPos columnPosition = context.getOrigin().add(1, 0, 1).south(columnSpacing);
        StructureWorldAccess world = context.getWorld();
        
        // TODO make not ugly.
        for (int column = 0; column < columns; column++) {
            BlockPos rowPosition = columnPosition.east(rowSpacing);

            for (int row = 0; row < rows; row++) {
                BlockPos pillarXPosition = rowPosition.add(-pillarCenterOffset, 0, -pillarCenterOffset);

                for (int x = 0; x < length; x++) {
                    BlockPos pillarZPosition = pillarXPosition;

                    for (int z = 0; z < length; z++) {
                        BlockPos blockPosition = pillarZPosition;

                        for (int y = 0; y < height; y++) {
                            world.setBlockState(blockPosition, blockState, 0);
                            blockPosition = blockPosition.up();
                        }

                        pillarZPosition = pillarZPosition.east();
                    }

                    pillarXPosition = pillarXPosition.south();
                }

                rowPosition = rowPosition.east(rowSpacing + 1);
            }

            columnPosition = columnPosition.south(columnSpacing + 1);
        }   

        return true;
    }
}
