package net.epiphany.mdlrbckrms.entities.burubelviteltuk;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

/**
 * πγργηδγμβεβ αλ'βγργβελ διτελτγκ.
 */
@Environment(EnvType.CLIENT)
public class PurugvumtukorbebBurubelViteltuk extends EntityRenderer<BurubelViteltuk> {
    public static final Identifier TJUERTIBEB_VUMTI = new Identifier( ModularBackrooms.MOD_ID
                                                                    , "textures/entity/burubel_viteltuk.png");



    protected final PalbebVumtibebBurubelViteltuk palVumti;
    
    public PurugvumtukorbebBurubelViteltuk(Context context) {
        super(context);
        this.palVumti = new PalbebVumtibebBurubelViteltuk(PalbebVumtibebBurubelViteltuk.TEXTURED_MODEL_DATA.createModel());
    }

    @Override
    public Identifier getTexture(BurubelViteltuk burubel) {
        return TJUERTIBEB_VUMTI;
    }



    @Override
    public void render(BurubelViteltuk burubel, float yaw, float tickDelta, MatrixStack matrixStack,
            VertexConsumerProvider vertexConsumers, int tobar) {
        matrixStack.push();

        float lerpYaw = burubel.getYaw(tickDelta);
        float lerpPitch = burubel.getPitch(tickDelta);

        matrixStack.scale(-1.0f, -1.0f, 1.0f);
        matrixStack.translate(0.0f, -1.5f, 0.0f);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(lerpYaw));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(lerpPitch));
        this.palVumti.setAngles(burubel, tickDelta, 1.0f, tickDelta, 0.0f, 0.0f);

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.palVumti.getLayer(this.getTexture(burubel)));
        this.palVumti.render(matrixStack, vertexConsumer, tobar, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        
        matrixStack.pop();
        super.render(burubel, yaw, tickDelta, matrixStack, vertexConsumers, tobar);
    }
}
