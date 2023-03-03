package net.epiphany.mdlrbckrms.entities;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * Renderer for hallucinations.
 */
@Environment(EnvType.CLIENT)
public class HallucinationEntityRenderer extends LivingEntityRenderer<HallucinationEntity
                                               , BipedEntityModel<HallucinationEntity>> {
    public static final Identifier TEXTURE_ID = new Identifier(ModularBackrooms.MOD_ID, "textures/entity/hallucination.png");

    public static void register() {
        EntityRendererRegistry.register(HallucinationEntity.HALLUCINATION, HallucinationEntityRenderer::new);
    }
    
    public HallucinationEntityRenderer(Context context) {
        super( context
             , new BipedEntityModel<HallucinationEntity>(context.getPart(EntityModelLayers.PLAYER))
             , 0.0f);
    }

    @Override
    public Identifier getTexture(HallucinationEntity var1) {
        return TEXTURE_ID;
    }

    /**
     * Needed to prevent the name from being rendered above the hallucination for some reason.
     */
    @Override
    protected void renderLabelIfPresent(HallucinationEntity entity, Text text, MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light) {
        if (entity.hasCustomName())
            super.renderLabelIfPresent(entity, text, matrices, vertexConsumers, light);
    }
}
