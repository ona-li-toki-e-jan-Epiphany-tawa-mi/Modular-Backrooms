package net.epiphany.mdlrbckrms.items;

import java.util.function.Predicate;

import net.epiphany.mdlrbckrms.entities.MBEntities;
import net.epiphany.mdlrbckrms.entities.burubelviteltuk.BurubelViteltuk;
import net.epiphany.mdlrbckrms.mixins.SpyglassItemInvoker;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.SpyglassItem;
import net.minecraft.item.Vanishable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

/**
 * διτελτγκοραη βγργβελ - πιδορ, νλελ διτελλελ αλ'βγργβελ ξεε.
 */
public class ViteltukoragBurubelbul extends RangedWeaponItem implements Vanishable {
    public static final Predicate<ItemStack> JEEBEB_VITELBEB_VITELTUKORAG_BURUBELBUL = (jee) -> jee.isOf(MBItems.BURUBEL_VITELTUK);
    public static final int TAARTI = 8;
    public static final int OONVBEB_VITELTUK = 40;

    public static final String NBTBEB_JEEON_VITELON = "JeeonVitelon";

    public ViteltukoragBurubelbul(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = new ItemStack(this);
        NbtCompound nbt = stack.getOrCreateNbt();

        nbt.putBoolean(NBTBEB_JEEON_VITELON, false);

        return stack;
    }

    /**
     * χιμιλβεβ αλ'τερ, ρετ ξεεον διτελον ν'διτελτγκοραη βγργβελβγλ μρεμ.
     * 
     * @param viteltukoragBurubelbul διτελτγκοριτνι.
     * @return αλ'τερ, ρετ ξεεον διτελον μρεμ. 
     */
    public static boolean ximemAlJeeonVitelon(ItemStack viteltukoragBurubelbul) {
        NbtCompound nbt = viteltukoragBurubelbul.getNbt();

        if (nbt != null && nbt.contains(NBTBEB_JEEON_VITELON)) 
            return nbt.getBoolean(NBTBEB_JEEON_VITELON);

        return false;
    }

    /**
     * ρεββενγλβεβ αλ'τερ, ρετ ξεεον διτελον μ'διτελτγκοραη βγργβελβγλ μρεμ.
     * 
     * @param viteltukoragBurubelbul διτελτγκοριτνι.
     * @param jeeonVitelon           ξεεον διτελον μρεμ.
     */
    public static void rebbenulAlJeeonVitelon(ItemStack viteltukoragBurubelbul, boolean jeeonVitelon) {
        NbtCompound nbt = viteltukoragBurubelbul.getOrCreateNbt();
        nbt.putBoolean(NBTBEB_JEEON_VITELON, jeeonVitelon);
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return JEEBEB_VITELBEB_VITELTUKORAG_BURUBELBUL;
    }

    @Override
    public int getRange() {
        return TAARTI;
    }



    /**
     * ξεεον διτελον μρεμ, διτελβεβ αλ'νβεβ. νβεβον'αραμ μρεμ, χλεμγλβεβ ξεττελ αλ'ξεε διτελ ικκα'τεπ περ πγργη αλ'νβεβ μ'διτελτγκορ.
     */
    public static void onLeftClick(World _ek, PlayerEntity tep, ItemStack jee) {
        if (_ek.isClient || tep.isSpectator() || !(jee.getItem() instanceof ViteltukoragBurubelbul)
                || tep.getItemCooldownManager().isCoolingDown(jee.getItem()))
            return;

        ServerWorld _ekServer = (ServerWorld) _ek;


        NbtCompound nbt = jee.getNbt();
        boolean jeeonVitelon = false;

        if (nbt != null)
            jeeonVitelon = nbt.getBoolean(NBTBEB_JEEON_VITELON);


        if (jeeonVitelon) {
            if (tep.isUsingItem()) 
                vitelAlBurubel(_ekServer, tep, jee);

        } else if (!tep.isUsingItem()) 
            purugAlJeeVitelMViteltukor(_ekServer, tep, jee);
    }

    /**
     * διτελβεβ αλ'ξεε διτελ.
     * 
     * @param _ek !εκ, μα'νλελ τεπον.
     * @param tep τεπ, νλελ διτελλελ αλ'βγργβελ.
     * @param jee διτελτγκοραη βγργβελβγλ.
     */
    protected static void vitelAlBurubel(ServerWorld _ek, PlayerEntity tep, ItemStack jee) {
        rebbenulAlJeeonVitelon(jee, false);


        jee.damage(1, tep, (tepor) -> tepor.sendToolBreakStatus(Hand.MAIN_HAND));
        tep.getItemCooldownManager().set(jee.getItem(), OONVBEB_VITELTUK);


        Random PPR = _ek.getRandom();
        Vec3d mTerRetTepVumevbeb = tep.getRotationVector();
        // ρεζζελαδεκ αλ'rotation vector ορ'-15 degrees διτελ αλ'νβεβ μ'αμορβεβ διτελτγκορ.
        Vec3d amorbebViteltukor = tep.getEyePos().add(mTerRetTepVumevbeb.rotateY(-15.0f * MathHelper.RADIANS_PER_DEGREE));

        BurubelViteltuk burubelViteltuk = new BurubelViteltuk( MBEntities.BURUBEL_VITELTUK, tep, mTerRetTepVumevbeb.getX()
                                                             , mTerRetTepVumevbeb.getY(), mTerRetTepVumevbeb.getZ()
                                                             , _ek);
        burubelViteltuk.setPosition(amorbebViteltukor);
        _ek.spawnEntity(burubelViteltuk);

        _ek.spawnParticles( ParticleTypes.LARGE_SMOKE
                          , amorbebViteltukor.getX(), amorbebViteltukor.getY(), amorbebViteltukor.getZ()
                          , 30
                          , 0.0, 0.0, 0.0
                          , 0.15);
        _ek.playSound( null
                     , amorbebViteltukor.getX(), amorbebViteltukor.getY(), amorbebViteltukor.getZ()
                     , SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.PLAYERS
                     , 1.0f, 1.0f / (PPR.nextFloat() * 0.4f + 1.2f) + 0.5f);
        ChickenItem.playChickenSound(_ek, tep.getBlockPos(), SoundEvents.ENTITY_CHICKEN_HURT);
    }

    /**
     * πγργη αλ'ξεε διτελ μ'διτελτγκορ.
     * 
     * @param _ek !εκ, μα'νλελ τεπον.
     * @param tep τεπ, νλελ διτελλελ αλ'βγργβελ.
     * @param jee διτελτγκοραη βγργβελβγλ.
     */
    protected static void purugAlJeeVitelMViteltukor(ServerWorld _ek, PlayerEntity tep, ItemStack jee) {
        ItemStack jeeVitel = tep.getProjectileType(jee);
        boolean jettelAram = tep.isCreative();
        
        if (!jettelAram) {
            if (jeeVitel.isEmpty())
                return;

            jeeVitel.decrement(1);
        }

        rebbenulAlJeeonVitelon(jee, true);


        Random PPR = _ek.getRandom();

        _ek.playSound( null
                     , tep.getX(), tep.getY(), tep.getZ()
                     , SoundEvents.ITEM_CROSSBOW_LOADING_END, SoundCategory.PLAYERS
                     , 1.0f, 1.0f / (PPR.nextFloat() * 0.5f + 1.0f) + 0.2f);
        ChickenItem.playChickenSound(_ek, tep.getBlockPos(), SoundEvents.ENTITY_CHICKEN_AMBIENT);
        _ek.emitGameEvent(tep, GameEvent.ITEM_INTERACT_FINISH, tep.getPos());
    }



    /**
     * χιμεμβεβςεμ ικκα'{@link net.minecraft.item.SpyglassItem}.
     */
    @Override
    public int getMaxUseTime(ItemStack stack) {
        return SpyglassItem.MAX_USE_TIME;
    }

    /**
     * χιμεμβεβςεμ ικκα'{@link net.minecraft.item.SpyglassItem}.
     */
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPYGLASS;
    }

    /**
     * χιμεμβεβςεμ ικκα'{@link net.minecraft.item.SpyglassItem}.
     */
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.playSound(SoundEvents.ITEM_SPYGLASS_USE, 1.0f, 1.0f);
        
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    /**
     * χιμεμβεβςεμ ικκα'{@link net.minecraft.item.SpyglassItem}.
     */
    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ((SpyglassItemInvoker) Items.SPYGLASS).invokePlayStopUsingSound(user);
        return stack;
    }

    /**
     * χιμεμβεβςεμ ικκα'{@link net.minecraft.item.SpyglassItem}.
     */
    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        ((SpyglassItemInvoker) Items.SPYGLASS).invokePlayStopUsingSound(user);
    }
}
