package net.epiphany.mdlrbckrms.features;

import com.mojang.serialization.Codec;

import net.epiphany.mdlrbckrms.ModularBackrooms;
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
    public static final Identifier FLUORESCENT_LIGHT_ARRAY_ID = new Identifier( ModularBackrooms.MOD_ID
                                                                              , "fluorescent_light_array");
    public static final Feature<FluorescentLightArrayConfig> FLUORESCENT_LIGHT_ARRAY_FEATURE = 
            new FluorescentLightArrayFeature(FluorescentLightArrayConfig.CODEC);

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
        if (columns < 1 || columns > 15) 
            throw new IllegalStateException( "Fluorescent light arrays must have between 1 and 15 columns! (recieved " + columns 
                                           + " columns)");

        int rows = config.rows();
        if (rows < 1 || rows > 15) 
            throw new IllegalStateException( "Fluorescent light arrays must have between 1 and 15 rows! (recieved " + rows 
                                           + " rows)");

                                           

        // Calculates how many blocks need to be in between each light in each row and column and how far from the wall they 
        //   should be.
        int columnSpacing = (16 - columns) / (columns + 1);
        int rowSpacing = (16 - rows) / (rows + 1);
        // Used to center lights.
        int lightCenterOffset = length / 2;
        // Offset by (1,0,1) to account for chunk walls.
        BlockPos columnPosition = context.getOrigin().add(1, 0, 1).south(columnSpacing);
        StructureWorldAccess world = context.getWorld();
        
        for (int column = 0; column < columns; column++) {
            BlockPos rowPosition = columnPosition.east(rowSpacing);

            for (int row = 0; row < rows; row++) {
                BlockPos lightPosition = rowPosition.north(lightCenterOffset);

                for (int x = 0; x < length; x++) {
                    world.setBlockState(lightPosition, lightBlockState, 0x0);
                    lightPosition = lightPosition.south();
                }

                rowPosition = rowPosition.east(rowSpacing + 1);
            }

            columnPosition = columnPosition.south(columnSpacing + 1);
        }   

        return true;
    }
}
