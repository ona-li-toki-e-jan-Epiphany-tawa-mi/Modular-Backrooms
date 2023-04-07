package net.epiphany.mdlrbckrms.items;

import org.jetbrains.annotations.Nullable;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.epiphany.mdlrbckrms.utilities.MBLootTables;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

/**
 * A item that looks and behaves like a chicken.
 */
public class ChickenItem extends Item {
    /**
     * Nbt tag that acts like the damage cooldown that mobs have.
     */
    public static final String DAMAGE_TIME_NBT = "DamageTime";
    public static final int MAX_DAMAGE_TIME = 10;

    public ChickenItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = new ItemStack(this);
        NbtCompound nbt = stack.getOrCreateNbt();

        nbt.putInt(DAMAGE_TIME_NBT, 0);

        return stack;
    }


    
    /**
     * Allows placing the chicken into the world.
     */
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (world.isClient)
            return ActionResult.success(true);


        ItemStack stack = context.getStack();
        BlockPos position = context.getBlockPos();
        BlockPos adjacentPosition = position.offset(context.getSide());

        ChickenEntity chicken = new ChickenEntity(EntityType.CHICKEN, world);
        // If the chicken item is named, why not add that name to the resulting chicken.
        if (stack.hasCustomName())
            chicken.setCustomName(stack.getName());
        chicken.setPosition(adjacentPosition.getX() + 0.5, adjacentPosition.getY(), adjacentPosition.getZ() + 0.5);
        world.spawnEntity(chicken);

        chicken.playAmbientSound();

        PlayerEntity player = context.getPlayer();
        world.emitGameEvent(player, GameEvent.ENTITY_PLACE, position);


        stack.decrement(1);
        return ActionResult.success(false);
    }



    @Override
    public void inventoryTick(ItemStack item, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient)
            return;

        
        // Updates damage time.
        NbtCompound nbt = item.getNbt();

        if (nbt != null) {
            int damageTime = nbt.getInt(DAMAGE_TIME_NBT);
            if (damageTime > 0)
                nbt.putInt(DAMAGE_TIME_NBT, damageTime - 1);
        }

        
        Random random = world.getRandom();
        BlockPos position = entity.getBlockPos();
        
        // Clucks whilst the chicken item is in the player's inventory.
        if (random.nextInt(1000) < 3) 
            playChickenSound(world, position, SoundEvents.ENTITY_CHICKEN_AMBIENT);


        // Randomly gives eggs to players holding chicken items.
        if (entity instanceof PlayerEntity player)
            if (shouldLayEgg(random)) {
                ItemStack egg = new ItemStack(Items.EGG);
                if (!player.giveItemStack(egg))
                    player.dropItem(egg, false);
                
                playChickenSound(world, position, SoundEvents.ENTITY_CHICKEN_EGG);
            }
    }

    /**
     * Clucks and lays eggs whilst in an item frame.
     */
    public static void onItemFrameTick(ItemFrameEntity itemFrame) {
        World world = itemFrame.getWorld();
        if (world.isClient) 
            return;

        if (itemFrame.getHeldItemStack().isOf(MBItems.CHICKEN)) {
            Random random = world.getRandom();
            BlockPos position = itemFrame.getBlockPos();
                
            if (shouldPlayAmbientNoise(random)) 
                playChickenSound(world, position, SoundEvents.ENTITY_CHICKEN_AMBIENT);

            if (shouldLayEgg(random)) {
                // Causes the egg to shoot out from the item frame.
                Vec3d outwardVector = itemFrame.getRotationVector().multiply(-1, 1, -1); // Points from the item frame 
                                                                                           // outwards away from the wall.
                Vec3d eggPosition = itemFrame.getPos().add(outwardVector.multiply(0.25)); // Nudges egg outside wall to 
                                                                                                //      prevent collison.
                double horizontalVelocity = world.random.nextDouble() * 0.2 - 0.1; // Copy from ItemEntity constructor.
                Vec3d eggVelocity = outwardVector.multiply(horizontalVelocity, 0.2, horizontalVelocity);
                
                ItemEntity egg = new ItemEntity( world, eggPosition.getX(), eggPosition.getY(), eggPosition.getZ()
                                               , new ItemStack(Items.EGG)
                                               , eggVelocity.getX(), eggVelocity.getY(), eggVelocity.getZ());
                world.spawnEntity(egg);

                playChickenSound(world, position, SoundEvents.ENTITY_CHICKEN_EGG);
            }
        }
    }

    /**
     * Clucks and lays eggs whilst being an item entity.
     */
    public static void onItemEntityTick(ItemEntity itemEntity) {
        World world = itemEntity.getWorld();
        if (world.isClient) 
            return;

        if (itemEntity.getStack().isOf(MBItems.CHICKEN)) {
            Random random = world.getRandom();
            BlockPos position = itemEntity.getBlockPos();

            if (shouldPlayAmbientNoise(random)) 
                playChickenSound(world, position, SoundEvents.ENTITY_CHICKEN_AMBIENT);

            if (shouldLayEgg(random)) {
                ItemEntity egg = new ItemEntity( world, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ()
                                                , new ItemStack(Items.EGG));
                world.spawnEntity(egg);

                playChickenSound(world, position, SoundEvents.ENTITY_CHICKEN_EGG);
            }
        }
    }
    
    /**
     * Plays chicken death sounds and drops loot when a chicken item entity is destroyed.
     */
    @Override
    public void onItemEntityDestroyed(ItemEntity entity) {
        ServerWorld world = (ServerWorld) entity.getWorld();
        BlockPos position = entity.getBlockPos();
        
        // Generates chicken drops.
        LootTable chickenLootTable = world.getServer().getLootManager().getTable(MBLootTables.CHICKEN_LOOT_TABLE);
        if (chickenLootTable != LootTable.EMPTY) {
            ObjectArrayList<ItemStack> drops = chickenLootTable.generateLoot(
                new LootContext.Builder(world).parameter(LootContextParameters.THIS_ENTITY, entity)
                                              .parameter(LootContextParameters.DAMAGE_SOURCE, entity.getDamageSources().generic())
                                              .parameter(LootContextParameters.ORIGIN, entity.getPos())
                                              .build(chickenLootTable.getType()));
        
            for (ItemStack drop : drops) {
                ItemEntity dropItemEntity = new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), drop);
                world.spawnEntity(dropItemEntity);
            }
        }
    
        playChickenSound(world, position, SoundEvents.ENTITY_CHICKEN_DEATH);
    }

    private static final int FURNACE_INGREDIENT_SLOT = 0;
    /**
     * Plays a death sound when chicken items are cooked in furnaces and similar blocks.
     */
    public static void onFurnaceCraftRecipe(World world, BlockPos position, AbstractFurnaceBlockEntity furnaceBlockEntity) {
        ItemStack ingredient = furnaceBlockEntity.getStack(FURNACE_INGREDIENT_SLOT);
        
        if (ingredient.isOf(MBItems.CHICKEN))
            playChickenSound(world, position, SoundEvents.ENTITY_CHICKEN_DEATH);
    }

    /**
     * Plays a death sound when chicken items are cooked with a campfire and similar blocks.
     */
    public static void onCampfireCookItem(World world, BlockPos position, ItemStack ingredient) {
        if (ingredient.isOf(MBItems.CHICKEN))
            playChickenSound(world, position, SoundEvents.ENTITY_CHICKEN_HURT);
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
     * Tells whether the chicken item should lay an egg. Approximately the same as chicken egg drop timing.
     * 
     * @param random Random number generator.
     * @return Whether the chicken should lay an egg.
     */
    private static boolean shouldLayEgg(Random random) {
         return random.nextInt(24000) < random.nextBetween(2, 4);
    }

    /**
     * Tells whether the chicken item should play an ambient noise.
     * 
     * @param random Random number generator.
     * @return Whether the chicken should play an ambient noise.
     */
    private static boolean shouldPlayAmbientNoise(Random random) {
        return random.nextInt(1000) < 3;
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
        if (nbt != null && nbt.getInt(DAMAGE_TIME_NBT) > 0)
            return false;


        playChickenSound(world, position, SoundEvents.ENTITY_CHICKEN_HURT);


        int cooldown = MAX_DAMAGE_TIME;
        if (wielder instanceof PlayerEntity player)
            player.getItemCooldownManager().set(MBItems.CHICKEN, cooldown);

        nbt = item.getOrCreateNbt();
        nbt.putInt(DAMAGE_TIME_NBT, cooldown);


        return true;
    }

    /**
     * Plays a chicken sound as it would be with a normal chicken.
     * 
     * @param world        The world to play the sound in.
     * @param position     Where to play the sound.
     * @param chickenSound The sound to play.
     */
    public static void playChickenSound(World world, BlockPos position, SoundEvent chickenSound) {
        Random random = world.getRandom();
        float pitch = (random.nextFloat() -random.nextFloat()) * 0.2f + 1.0f; // Clone of adult MobEntity pitch calculator.

        world.playSound( null
                       , position
                       , chickenSound
                       , SoundCategory.NEUTRAL
                       , 1.0f, pitch);
    }
}
