package net.epiphany.mdlrbckrms.entities.burubelviteltuk;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.ChickenEntityModel;

// TODO πγργηξε αλ'rocket body μ'παλιτνι δγμτι.
// TODO πγργηαπαξε αλ'ρεζζελτγκβεβ παλιτνι.

/**
 * παλβεβ δγμτιβεβ βγργβελ διτελτγκ. πγργηορλγμβεβ αλ'παλβεβ βγργβελ.
 */
public class PalbebVumtibebBurubelViteltuk extends ChickenEntityModel<BurubelViteltuk> {
    public PalbebVumtibebBurubelViteltuk(ModelPart root) {
        super(root);
        this.child = false;
    }
    
    @Override
    public void setAngles(BurubelViteltuk entity, float limbAngle, float limbDistance, float animationProgress,
            float headYaw, float headPitch) {
        super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
        //ChickenEntityModelAccessor accessor = (ChickenEntityModelAccessor) this;

        //accessor.getBody().yaw = accessor.getHead().yaw;
    }
}
