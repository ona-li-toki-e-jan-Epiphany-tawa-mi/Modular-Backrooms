package net.epiphany.mdlrbckrms.entities;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * A visual hallucination. Entities that watch you from the corner of your eye but dissapear when you take a look.
 */
public class HallucinationEntity extends LivingEntity {
    public static final Identifier HALLUCINATION_ID = new Identifier(ModularBackrooms.MOD_ID, "hallucination");
    public static final EntityType<HallucinationEntity> HALLUCINATION = 
            Registry.register( Registries.ENTITY_TYPE, HALLUCINATION_ID
                             , FabricEntityTypeBuilder.create(SpawnGroup.AMBIENT, HallucinationEntity::new)
                    .disableSaving()
                    .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
                    .build());

    public static void register() {
        FabricDefaultAttributeRegistry.register(HALLUCINATION, LivingEntity.createLivingAttributes());
    }

    

    /**
     * The amount of time that a hallucination has been visible to a player.
     */
    private int seenTicks = 0;

    public HallucinationEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }


    
    private static final double PLAYER_VIEW_EPSILON = -0.48;
    private static final double TARGETING_RANGE = 60.0;

    @Override
    public void tick() {
        super.tick();
        if (this.world.isClient)
            return;


        if (seenTicks > 5) {
            this.discard();
            return;
        }


        PlayerEntity closestPlayer = this.world.getClosestPlayer(this, TARGETING_RANGE);
        if (closestPlayer != null && !closestPlayer.isSpectator()) 
            this.lookAt(EntityAnchor.EYES, closestPlayer.getEyePos());


        // TODO improve this, or atleast increase the range.
        boolean isSeen = false;
        List<PlayerEntity> nearbyPlayers = this.world.getPlayers( TargetPredicate.createNonAttackable()
                                                                , this
                                                                , this.getBoundingBox().expand(TARGETING_RANGE * 2.0));

        for (PlayerEntity player : nearbyPlayers)
            if (!player.isSpectator() && player.canSee(this) 
                    && player.getRotationVector().dotProduct(this.getRotationVector()) < PLAYER_VIEW_EPSILON) {
                isSeen = true;
                break;
            }

        if (isSeen) {
            seenTicks++;
        } else if (seenTicks > 0)
            seenTicks--;
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

        if (DamageSource.OUT_OF_WORLD.equals(source)) 
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
    protected void playEquipSound(ItemStack stack) {}

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
    public void animateDamage() {
        return;
    }



    /**
     * These methods contribute nothing but are required by LivingEntity and have been set to resonable defaults.
     */
    @Override
    public Iterable<ItemStack> getArmorItems() {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot var1) {
        return ItemStack.EMPTY;
    }

    @Override
    public void equipStack(EquipmentSlot var1, ItemStack var2) {}

    @Override
    public Arm getMainArm() {
        return Arm.RIGHT;
    }
}
