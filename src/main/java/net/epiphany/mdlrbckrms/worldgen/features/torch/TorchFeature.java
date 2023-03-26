package net.epiphany.mdlrbckrms.worldgen.features.torch;

import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

/**
 * Used to generate singular torches in the world.
 */
public class TorchFeature extends Feature<TorchConfig> {
    public TorchFeature(Codec<TorchConfig> configCodec) {
        super(configCodec);
    }



    @Override
    public boolean generate(FeatureContext<TorchConfig> context) {
        TorchConfig config = context.getConfig();

        Identifier torchID = config.torchBlockID();
        BlockState torchState = Registries.BLOCK.get(torchID).getDefaultState();
        if (torchState == null || !(torchState.getBlock() instanceof TorchBlock torchBlock)) 
            throw new IllegalStateException(torchID + " could not be parsed to a valid torch block identifier!");

        Identifier wallTorchID = config.wallTorchBlockID();
        BlockState wallTorchState = Registries.BLOCK.get(wallTorchID).getDefaultState();
        
        if (wallTorchState == null || !(wallTorchState.getBlock() instanceof WallTorchBlock wallTorchBlock)) 
            throw new IllegalStateException(wallTorchID + " could not be parsed to a valid wall torch block identifier!");



        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();

        if (!world.getBlockState(origin).isReplaceable())
            return false;

        if (torchBlock.canPlaceAt(torchState, world, origin)) {
            world.setBlockState(origin, torchState, 0);
            return true;

        } else 
            for (Direction direction : Direction.Type.HORIZONTAL) {
                BlockState possibleState = wallTorchState.with(WallTorchBlock.FACING, direction);

                if (wallTorchBlock.canPlaceAt(possibleState, world, origin)) {
                    world.setBlockState(origin, possibleState, 0);
                    return true;
                }
            }

        return false;
    }
}
