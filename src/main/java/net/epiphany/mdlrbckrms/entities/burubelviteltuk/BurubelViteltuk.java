package net.epiphany.mdlrbckrms.entities.burubelviteltuk;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.epiphany.mdlrbckrms.items.ChickenItem;
import net.epiphany.mdlrbckrms.utilities.MBLootTables;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

/**
 * βγργβελ, νλελ διτελλελονβον ικκα'διτελτγκοραη βγργβελβγλ.
 */
public class BurubelViteltuk extends ExplosiveProjectileEntity {
    public static final int TICKBUL_BON_VITELAMORTI = 200;



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
     * @param type  βγργβελ.
     * @param world !εκ, μα'νλελ βγργβελον. 
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
        if (!this.world.isClient) {
            if (this.age > TICKBUL_BON_VITELAMORTI) {
                this.vitelamortitel();
                return;
            }

            Random PPR = this.world.getRandom();

            // :)))))
            if (PPR.nextInt(7) == 0) 
                ChickenItem.playChickenSound(this.world, this.getBlockPos(), SoundEvents.ENTITY_CHICKEN_HURT);

            // μ'οονδιτνι νβεβ διτελβεβ μ'ρεββεν ικκα'τερ, μ'ρετ διτελβεβςεμονβον.
            this.powerX += (PPR.nextDouble() - 0.5) * 0.005;
            this.powerY += (PPR.nextDouble() - 0.5) * 0.005;
            this.powerZ += (PPR.nextDouble() - 0.5) * 0.005;

        } else {
            if (this.age % 2 == 0) {
                Vec3d ikkaTerRetVitelbeb = this.getRotationVector().add(0.0, 0.5, 0.0).add(this.getPos());
                this.world.addParticle( ParticleTypes.FIREWORK
                                      , ikkaTerRetVitelbeb.getX(), ikkaTerRetVitelbeb.getY(), ikkaTerRetVitelbeb.getZ()    
                                      , 0.0, 0.0, 0.0);
            }
        }

        super.tick();
    }

    /**
     * πιδχολ αλ'ξεε!εκ βελςα'νιτνι.
     */
    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);

        if (!this.world.isClient) 
            this.vitelamortitel();
    }



    /**
     * διτελαμορτιτελβεβςεμ περ πγργηβεβ αλξεεβγλβεβ βγργβελ μα'!εκ.
     */
    public void vitelamortitel() {
        ServerWorld _ekServer = (ServerWorld) this.getWorld();

        _ekServer.createExplosion( this
                                 , this.getX(), this.getY(), this.getZ()
                                 , 2.0f, true
                                 , World.ExplosionSourceType.MOB);

        // πγργη αλ'ξεεβγλβεβ βγργβελ.
        LootTable lootbebTablebebBurubel = this.world.getServer().getLootManager().getTable(MBLootTables.CHICKEN_LOOT_TABLE);
        if (lootbebTablebebBurubel != LootTable.EMPTY) {
            ObjectArrayList<ItemStack> jeebul = lootbebTablebebBurubel.generateLoot(
                new LootContext.Builder(_ekServer).parameter(LootContextParameters.THIS_ENTITY, this)
                                                  .parameter(LootContextParameters.DAMAGE_SOURCE, this.getDamageSources().generic())
                                                  .parameter(LootContextParameters.ORIGIN, this.getPos())
                                                  .build(lootbebTablebebBurubel.getType()));
        
            for (ItemStack jee : jeebul) {
                ItemEntity jeePurug = new ItemEntity(world, this.getX(), this.getY(), this.getZ(), jee);
                // ξεε διτελοννοβ ξαβο)
                jeePurug.setVelocity(jeePurug.getVelocity().multiply(4.0));
                _ekServer.spawnEntity(jeePurug);
            }
        }

        ChickenItem.playChickenSound(_ekServer, this.getBlockPos(), SoundEvents.ENTITY_CHICKEN_DEATH);

        this.discard();
    }
}
