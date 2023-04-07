package net.epiphany.mdlrbckrms.entities.hallucination;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * A visual hallucination. Entities that watch you from the corner of your eye but dissapear when you take a look.
 */
public class HallucinationEntity extends MobEntity {
    /**
     * The maximum range that a player can be stared at from and to test for players looking at it.
     */
    protected static final double TARGETING_RANGE = 90.0;

    /**
     * The minimum distance squared a hallucination can be from a player before it disappears. Prevents players from
     *  easily making out what the hallucination looks like.
     */
    protected static final double MINIMUM_PLAYER_DISTANCE_SQUARED = MathHelper.square(35.0);

    /**
     * The amount of time between when the hallucination should check for players looking at it.
     */
    protected static final int CHECK_SEEN_TICKS = 5;

    /**
     * The maximum age of a hallucination. Meant to make them rarer.
     */
    protected static final int MAXIMUM_AGE = 160;



    /**
     * The amount of time since the hallucination has checked to see if players are watching. Used to stagger the checks.
     */
    protected int ticksSinceSeenCheck = 0;

    public HallucinationEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }



    @Override
    public void tick() {
        super.tick();
        if (this.world.isClient)
            return;


        // Makes sure hallucinations disappear after a short time to reduce their numbers.
        if (this.age > MAXIMUM_AGE) {
            this.discard();
            return;
        }


        // Stares at the player.
        PlayerEntity closestPlayer = this.world.getClosestPlayer(this, TARGETING_RANGE);

        if (closestPlayer != null && !closestPlayer.isSpectator()) { 
            if (this.squaredDistanceTo(closestPlayer) < MINIMUM_PLAYER_DISTANCE_SQUARED) {
                this.discard();
                return;
            }

            this.lookAt(EntityAnchor.EYES, closestPlayer.getPos());
        }


        // Disappears if players look at it.
        if (this.ticksSinceSeenCheck > CHECK_SEEN_TICKS) {
            List<PlayerEntity> nearbyPlayers = this.world.getPlayers( TargetPredicate.createNonAttackable()
                                                                    , this
                                                                    , this.getBoundingBox().expand(TARGETING_RANGE));
            final double PLAYER_VIEW_EPSILON = -0.48; // Roughly matches player's normal fov.

            for (PlayerEntity player : nearbyPlayers)
                if (!player.isSpectator() && player.canSee(this) 
                        && player.getRotationVector().dotProduct(this.getRotationVector()) < PLAYER_VIEW_EPSILON) {
                    this.discard();
                    return;
                }
        }

        this.ticksSinceSeenCheck = this.ticksSinceSeenCheck > CHECK_SEEN_TICKS ? 0 : this.ticksSinceSeenCheck + 1;
    }

    

    /**
     * Prevents mobs from targeting hallucinations among other things.
     */
    @Override
    public boolean isPartOfGame() {
        return false;
    }

    /**
     * Ensures that hallucinations stay in place, unaffected by gravity.
     */
    @Override
    public void travel(Vec3d movementInput) {}

    /**
     * Prevents hallucinations from triggering sculk senors and the like.
     */
    @Override
    public boolean occludeVibrationSignals() {
        return true;
    }

    @Override
    public boolean canUsePortals() {
        return false;
    }
    


    /**
     * This and the following methods prevent collisions and the hallucination from being pushed around.
     */
    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public PistonBehavior getPistonBehavior() {
        return PistonBehavior.IGNORE;
    }

    @Override
    public boolean isPushedByFluids() {
        return false;
    }

    @Override
    protected void pushAway(Entity entity) {}


    
    /**
     * This and following methods prevent status effects, mainly to prevent potions of harming from damaging it, invisibility 
     *  being applied, and particles effects being created.
     */
    @Override
    public boolean isAffectedBySplashPotions() {
        return false;
    }

    @Override
    public boolean canHaveStatusEffect(StatusEffectInstance effect) {
        return false;
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    public boolean isGlowing() {
        return false;
    }


   
    /**
     * This and the following damage-related methods prevent the hallucination from taking damage as it isn't supposed to be 
     *  real.
     */
    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.world.isClient || this.isRemoved()) 
            return false;

        if (source.isOf(DamageTypes.OUT_OF_WORLD))
            this.kill();

        return false;
    }

    @Override
    protected void tickCramming() {}

    @Override
    public void onStruckByLightning(ServerWorld world, LightningEntity lightning) {}

    @Override
    public boolean isImmuneToExplosion() {
        return true;
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {}

    @Override
    public boolean canHit() {
        return false;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    public void takeKnockback(double strength, double x, double z) {}



    /**
     * This, amongst the following sound-related functions prevent the hallucination from making sound. It is visual, not
     *  auditory.
     */
    @Override
    @Nullable
    protected SoundEvent getHurtSound(DamageSource source) {
        return null;
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return null;
    }

    @Override
    protected void playBlockFallSound() {}

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {}

    @Override
    protected void playSwimSound(float volume) {}

    /**
     * In the event that the hallucination takes damage we want to make sure it isn't shown to keep the feel.
     */
    @Override
    public void animateDamage(float yaw) {
        return;
    }
}
