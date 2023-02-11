package net.epiphany.features;

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
 * Used to generate arrays of light fixtures within the ceiling.
 */
public class CeilingLightArrayFeature extends Feature<CeilingLightArrayConfig> {
    public static final Identifier LIGHT_ARRAY_ID = new Identifier(ModularBackrooms.MOD_ID, "ceiling_light_array");
    public static final Feature<CeilingLightArrayConfig> LIGHT_ARRAY_FEATURE = new CeilingLightArrayFeature(CeilingLightArrayConfig.CODEC);

    public CeilingLightArrayFeature(Codec<CeilingLightArrayConfig> configCodec) {
        super(configCodec);
    }



    @Override
    public boolean generate(FeatureContext<CeilingLightArrayConfig> context) {
        StructureWorldAccess world = context.getWorld();

        CeilingLightArrayConfig config = context.getConfig();
        Identifier blockID = config.blockID();
        BlockState blockState = Registries.BLOCK.get(blockID).getDefaultState();
        int length = config.length();
        int columns = config.columns();
        int rows = config.rows();

        // Calculates how many blocks need to be in between each light in each row and column and how far from the wall they 
        //   should be.
        int columnSpacing = (16 - columns) / (columns + 1);
        int rowSpacing = (16 - rows) / (rows + 1);
        // Used to center lights.
        int lightCenterOffset = length / 2;
        // Offset by (1,0,1) to account for chunk walls.
        BlockPos columnPosition = context.getOrigin().add(1, 0, 1).south(columnSpacing);
        
        for (int column = 0; column < columns; column++) {
            BlockPos rowPosition = columnPosition.east(rowSpacing);

            for (int row = 0; row < rows; row++) {
                BlockPos lightPosition = rowPosition.north(lightCenterOffset);

                for (int x = 0; x < length; x++) {
                    world.setBlockState(lightPosition, blockState, 0x0);
                    lightPosition = lightPosition.south();
                }

                rowPosition = rowPosition.east(rowSpacing + 1);
            }

            columnPosition = columnPosition.south(columnSpacing + 1);
        }   

        return true;
    }
}
