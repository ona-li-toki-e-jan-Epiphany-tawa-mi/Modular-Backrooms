package net.epiphany.mdlrbckrms.entities.burubelviteltuk;

import net.epiphany.mdlrbckrms.mixins.TexturedModelDataAccessor;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.ChickenEntityModel;
import net.minecraft.client.util.math.MatrixStack;

/**
 * παλβεβ δγμτιβεβ βγργβελ διτελτγκ. πγργηορλγμβεβ αλ'παλβεβ βγργβελ.
 */
public class PalbebVumtibebBurubelViteltuk extends ChickenEntityModel<BurubelViteltuk> {
    public static final String TJUERTIBEB_PAL_VUM_NLELBEB_VITELTUKOR = "viteltukor";

    /**
     * πγργηβεβ αλ'παλ δγμτι περ τααρτιβεβ δγμτι.
     */
    public static TexturedModelData getTexturedModelData() {
        TexturedModelDataAccessor texturedModelDataAccessor = (TexturedModelDataAccessor) ChickenEntityModel.getTexturedModelData();
        ModelData modelData = texturedModelDataAccessor.getData();
        ModelPartData modelPartData = modelData.getRoot();

        modelPartData.addChild( TJUERTIBEB_PAL_VUM_NLELBEB_VITELTUKOR
                              , ModelPartBuilder.create()
                                                .uv(0, 23)
                                                .cuboid(-0.5f, -0.75f, 2.0f, 2.0f, 2.0f, 18.0f)
                              , ModelTransform.pivot(-0.5f, 16.0f, 2.0f));

        return TexturedModelData.of(modelData, 64, 64);
    }

    public static final TexturedModelData TEXTURED_MODEL_DATA = getTexturedModelData();



    protected final ModelPart viteltukor;

    public PalbebVumtibebBurubelViteltuk(ModelPart root) {
        super(root);
        this.child = false;

        this.viteltukor = root.getChild(TJUERTIBEB_PAL_VUM_NLELBEB_VITELTUKOR);
    }

    

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green,
            float blue, float alpha) {
        super.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        this.viteltukor.render(matrices, vertices, light, overlay);
    }
}
