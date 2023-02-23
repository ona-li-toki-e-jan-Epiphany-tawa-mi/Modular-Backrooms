package net.epiphany.mdlrbckrms.items;

import org.jetbrains.annotations.Nullable;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

/**
 * A item that looks and behaves like a chicken.
 */
public class ChickenItem extends Item {
    public static final Identifier CHICKEN_ID = new Identifier(ModularBackrooms.MOD_ID, "chicken");
    public static final ChickenItem CHICKEN = new ChickenItem((new FabricItemSettings()).maxCount(16));
   
    /**
     * Nbt tag that acts like the damage cooldown that mobs have.
     */
    public static final String DAMAGE_TIME = "DamageTime";

    public static void register() {
        Registry.register(Registries.ITEM, CHICKEN_ID, CHICKEN);
        CompostingChanceRegistry.INSTANCE.add(CHICKEN, 1.0f); // Compostable chickens ;)
    }

    public static void registerItemUnderGroup(FabricItemGroupEntries content) {
        content.add(CHICKEN);
    }



    public ChickenItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack jee = new ItemStack(this);
        addNbtData(jee);
        return jee;
    }

    /**
     * Updates a ChickenItem itemstack to have the needed nbt data. Call on creation and tick events to make sure 
     *      it's present
     * 
     * @param item The ChickenItem itemstack to add the nbt to.
     */
    private void addNbtData(ItemStack item) {
        NbtCompound nbt = item.getOrCreateNbt();
        nbt.putInt(DAMAGE_TIME, 0);
    }


    
    // TODO πγργηξε αλ'sound-βγλ ον'κελβεβ βγργβελτι.
    /**
     * Allows placing the chicken into the world.
     */
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (world.isClient)
            return ActionResult.success(true);


        ItemStack stack = context.getStack();
        BlockPos adjacentPosition = context.getBlockPos().offset(context.getSide());

        ChickenEntity chicken = new ChickenEntity(EntityType.CHICKEN, world);
        // If the chicken item is named, why not add that name to the resulting chicken.
        if (stack.hasCustomName())
            chicken.setCustomName(stack.getName());
        chicken.setPosition(adjacentPosition.getX() + 0.5, adjacentPosition.getY(), adjacentPosition.getZ() + 0.5);
        world.spawnEntity(chicken);

        chicken.playAmbientSound();


        stack.decrement(1);
        return ActionResult.success(false);
    }



    @Override
    public void inventoryTick(ItemStack item, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient)
            return;

        
        // Updates damage time and ensures that it is present on chicken items.
        NbtCompound nbt = item.getNbt();
        if (nbt != null) {
            int damageTime = nbt.getInt(DAMAGE_TIME);
            if (damageTime > 0)
                nbt.putInt(DAMAGE_TIME, damageTime - 1);
        } else
            addNbtData(item);

        
        Random random = world.getRandom();

        // Clucks whilst the chicken item is in the player's inventory.
        //TODO make work for item frames.
        for (int i = 0; i < item.getCount(); i++)
            if (random.nextInt(1000) < 3) 
                world.playSound( null
                             , entity.getBlockPos()
                             , SoundEvents.ENTITY_CHICKEN_AMBIENT
                             , SoundCategory.NEUTRAL
                             , 1.0f, getPitch(random));
    }

    private static final Identifier CHICKEN_LOOT_TABLE_ID = new Identifier("minecraft", "entities/chicken");
    /**
     * Plays chicken death sounds and drops loot when a chicken item entity is destroyed.
     */
    @Override
    public void onItemEntityDestroyed(ItemEntity entity) {
        ServerWorld world = (ServerWorld) entity.getWorld();
        Random random = world.getRandom();
        BlockPos position = entity.getBlockPos();
        int chickenCount = entity.getStack().getCount();

        
        // Generates chicken drops.
        LootTable chickenLootTable = world.getServer().getLootManager().getTable(CHICKEN_LOOT_TABLE_ID);
        if (chickenLootTable != LootTable.EMPTY) 
            for (int i = 0; i < chickenCount; i++) {
                ObjectArrayList<ItemStack> drops = chickenLootTable.generateLoot(
                    new LootContext.Builder(world).parameter(LootContextParameters.THIS_ENTITY, entity)
                                                  .parameter(LootContextParameters.DAMAGE_SOURCE, DamageSource.GENERIC)
                                                  .parameter(LootContextParameters.ORIGIN, entity.getPos())
                                                  .build(chickenLootTable.getType()));
            
                for (ItemStack drop : drops) {
                    ItemEntity dropItemEntity = new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), drop);
                    world.spawnEntity(dropItemEntity);
                }
            }

        for (int i = 0; i < chickenCount; i++)
            world.playSound( null
                           , position
                           , SoundEvents.ENTITY_CHICKEN_DEATH
                           , SoundCategory.NEUTRAL
                           , 1.0f, getPitch(random));
    }

    /**
     * "Damages" chicken item on hitting an entity.
     */
    @Override
    public boolean postHit(ItemStack item, LivingEntity attacked, LivingEntity attacker) {
        return damageChicken(item, attacker.getWorld(), attacker.getBlockPos(), attacker);
    }

    /**
     * "Damages" chicken item when used to mine a block.
     */
    @Override
    public boolean postMine(ItemStack item, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        return damageChicken(item, world, pos, miner);
    }



    /**
     * "Damages" the chicken item, causing it to turn red and the chicken damamage sound to play.
     * 
     * @param item     The chicken item.
     * @param world    The world with the chicken item.
     * @param position The position of the chicken item or it's wielder.
     * @param wielder  The wielder of the item. Can be {@code null}.
     * @return Whether the chicken was "damaged."
     */
    private static boolean damageChicken(ItemStack item, World world, BlockPos position, @Nullable LivingEntity wielder) {
        NbtCompound nbt = item.getNbt();
        // "Damage" cannot occur until cooldown is up.
        if (nbt != null && nbt.getInt(DAMAGE_TIME) > 0)
            return false;


        Random random = world.getRandom();
        world.playSound( null
                       , position
                       , SoundEvents.ENTITY_CHICKEN_HURT
                       , SoundCategory.NEUTRAL
                       , 1.0f, getPitch(random));


        int cooldown = 10 - item.getCount() / 2; // More chickens, more hits.
        if (wielder instanceof PlayerEntity player)
            player.getItemCooldownManager().set(CHICKEN, cooldown);
        if (nbt != null) 
            nbt.putInt(DAMAGE_TIME, cooldown);

        return true;
    }

    /**
     * Just a copy of the pitch generators used in {@link ChickenEntity}
     * 
     * @param random Random number generator.
     * @return A random pitch to use for the sounds of an adult animal.
     */
    private static float getPitch(Random random) {
        return (random.nextFloat() -random.nextFloat()) * 0.2f + 1.0f;
    }
}
