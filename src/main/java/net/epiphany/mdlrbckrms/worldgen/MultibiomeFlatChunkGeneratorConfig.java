package net.epiphany.mdlrbckrms.worldgen;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorLayer;

/**
 * Flat world generation settings for {@link net.epiphany.mdlrbckrms.worldgen.MultibiomeFlatChunkGenerator}.
 */
public class MultibiomeFlatChunkGeneratorConfig {
    public static final Codec<MultibiomeFlatChunkGeneratorConfig> CODEC = RecordCodecBuilder.create(instance -> 
            instance.group( FlatChunkGeneratorLayer.CODEC.listOf()
                                                         .fieldOf("layers")
                                                         .forGetter(MultibiomeFlatChunkGeneratorConfig::getLayers)
                          , Codec.INT.fieldOf("minimum_y")
                                     .forGetter(MultibiomeFlatChunkGeneratorConfig::getMinumumY)            
                          , Codec.INT.fieldOf("sea_level")
                                     .forGetter(MultibiomeFlatChunkGeneratorConfig::getSeaLevel)          
            ).apply(instance, instance.stable(MultibiomeFlatChunkGeneratorConfig::new)));



    protected final List<FlatChunkGeneratorLayer> layers = Lists.newArrayList();
    protected final List<BlockState> layerBlocks = Lists.newArrayList();
    protected final int minumumY;
    protected final int seaLevel;

    /**
     * @attention This does not set a minumum height like with {@link net.minecraft.world.gen.chunk.FlatChunkGenerator}, make sure to
     *      put a large air layer if you want any airspace.
     * 
     * @param layers   The layers of the superflat world. Determines the world height.
     * @param minimumY The world's minimum y-level.
     * @param seaLevel The sea level, used by some features.
     */
    public MultibiomeFlatChunkGeneratorConfig(List<FlatChunkGeneratorLayer> layers, int minimumY, int seaLevel) {
        this.layers.addAll(layers);
        this.updateLayerBlocks();

        this.minumumY = minimumY;
        this.seaLevel = seaLevel;
    }
    
    public List<FlatChunkGeneratorLayer> getLayers() {
        return layers;
    }

    public List<BlockState> getLayerBlocks() {
        return layerBlocks;
    }

    public int getMinumumY() {
        return minumumY;
    }

    public int getSeaLevel() {
        return seaLevel;
    }



    public void updateLayerBlocks() {
        this.layerBlocks.clear();

        for (FlatChunkGeneratorLayer flatChunkGeneratorLayer : this.layers) 
            for (int i = 0; i < flatChunkGeneratorLayer.getThickness(); ++i) 
                this.layerBlocks.add(flatChunkGeneratorLayer.getBlockState());
    }
}
