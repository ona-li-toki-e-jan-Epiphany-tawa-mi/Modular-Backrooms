package net.epiphany.mdlrbckrms.entities.burubelviteltuk;

import net.epiphany.mdlrbckrms.items.ChickenItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

/**
 * βγργβελ, νλελ διτελλελονβον ικκα'διτελτγκοραη βγργβελβγλ.
 */
public class BurubelViteltuk extends ExplosiveProjectileEntity {
    /**
     * @param type       βγργβελ.
     * @param owner      τεπ, νλελ διτελλελονβον αλ'βγργβελ.
     * @param directionX μ'τερ, ρετ βγργβελ διτελβεβοννοβ.
     * @param directionY μ'τερ, ρετ βγργβελ διτελβεβοννοβ.
     * @param directionZ μ'τερ, ρετ βγργβελ διτελβεβοννοβ.
     * @param world      !εκ, μα'νλελ βγργβελον. 
     */
    public BurubelViteltuk(EntityType<? extends BurubelViteltuk> type, LivingEntity owner, double directionX,
            double directionY, double directionZ, World world) {
        super(type, owner, directionX, directionY, directionZ, world);
    }

    /**
     * @param type       βγργβελ.
     * @param world      !εκ, μα'νλελ βγργβελον. 
     */
    public BurubelViteltuk(EntityType<? extends BurubelViteltuk> entityType, World world) {
        super(entityType, world);
    }

    /**
     * @param type       βγργβελ.
     * @param x          μ'τερ, ρετ βγργβελον μ'x-axis.
     * @param y          μ'τερ, ρετ βγργβελον μ'y-axis.
     * @param z          μ'τερ, ρετ βγργβελον μ'z-axis.
     * @param directionX μ'τερ, ρετ βγργβελ διτελβεβοννοβ μ'x-axis.
     * @param directionY μ'τερ, ρετ βγργβελ διτελβεβοννοβ μ'y-axis.
     * @param directionZ μ'τερ, ρετ βγργβελ διτελβεβοννοβ μ'z-axis.
     * @param world      !εκ, μα'νλελ βγργβελον. 
     */
    public BurubelViteltuk(EntityType<? extends BurubelViteltuk> type, double x, double y, double z,
            double directionX, double directionY, double directionZ, World world) {
        super(type, x, y, z, directionX, directionY, directionZ, world);
    }



    @Override
    public void tick() {
        Random PPR = this.world.getRandom();

        // :)))))
        if (this.world.isClient && PPR.nextInt(6) == 0) 
            ChickenItem.playChickenSound(this.world, this.getBlockPos(), SoundEvents.ENTITY_CHICKEN_HURT);

        // μ'οονδιτνι νβεβ διτελβεβ μ'ρεββεν ικκα'τερ, μ'ρετ διτελβεβςεμονβον.
        this.powerX += (PPR.nextDouble() - 0.5) * 0.005;
        this.powerY += (PPR.nextDouble() - 0.5) * 0.005;
        this.powerZ += (PPR.nextDouble() - 0.5) * 0.005;

        super.tick();
    }

    /**
     * πιδχολ αλ'ξεε!εκ βελςα'νιτνι.
     */
    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.world.isClient) {
            this.world.createExplosion((Entity)this, this.getX(), this.getY(), this.getZ(), 1.0f, false, World.ExplosionSourceType.MOB);
            this.discard();
        }
    }



    /** 
     * νιτνιον'αραμ μρεμ, βγργβελ δγμεδβεβ τοβαρλι.
     */
    @Override
    public boolean isOnFire() {
        return false;
    }
}
