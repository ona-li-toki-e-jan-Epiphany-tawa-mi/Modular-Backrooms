package net.epiphany.mdlrbckrms.items;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

/**
 * A bottle of water gained from wringing out moist carpet and collecting the fluid. Best not to drink.
 * 
 * TODO Note: if nbt crafting is ever added as a dependency (not gonna do it for one item (this),) make this a normal potion.
 */
public class SuspicousWaterItem extends Item {
    /**
     * A potion type containing the effects given by the suspicous water.
     */
    public static final Potion SUSPICOUS_WATER_POTION = 
            new Potion( new StatusEffectInstance( StatusEffects.POISON
                                                , 100, 0
                                                , false, false, true)
                      , new StatusEffectInstance( StatusEffects.WITHER
                                                , 100, 0
                                                , false, false, true)
                      , new StatusEffectInstance( StatusEffects.BLINDNESS
                                                , 200, 0
                                                , false, false, true)
                      , new StatusEffectInstance( StatusEffects.NAUSEA
                                                , 400, 0
                                                , false, false, true)
                      , new StatusEffectInstance( StatusEffects.SLOWNESS
                                                , 400, 0
                                                , false, false, true)
                      , new StatusEffectInstance( StatusEffects.WEAKNESS
                                                , 400, 0
                                                , false, false, true)
                      , new StatusEffectInstance( StatusEffects.MINING_FATIGUE
                                                , 400, 0
                                                , false, false, true));

    public static final Identifier SUSPICOUS_WATER_ID = new Identifier(ModularBackrooms.MOD_ID, "suspicous_water");
    public static final SuspicousWaterItem SUSPICOUS_WATER = new SuspicousWaterItem(new FabricItemSettings());
    
    public static void register() {
        Registry.register(Registries.ITEM, SUSPICOUS_WATER_ID, SUSPICOUS_WATER);
    }

    public static void registerItemUnderGroup(FabricItemGroupEntries content) {
        content.add(SUSPICOUS_WATER);
    }

    
    
    public SuspicousWaterItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getRecipeRemainder(ItemStack stack) {
        return new ItemStack(Items.GLASS_BOTTLE);
    }



    /**
     * Gives the suspicous water effects to the drinker.
     * 
     * Basically a copy of {@link PotionItem#finishUsing(ItemStack, World, LivingEntity)}
     */
    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        user.emitGameEvent(GameEvent.DRINK);


        PlayerEntity player = user instanceof PlayerEntity ? (PlayerEntity) user : null;
        ItemStack result = stack;
        
        if (player != null) {
            player.incrementStat(Stats.USED.getOrCreateStat(this));

            if (!player.isCreative())
                stack.decrement(1);
        }

        if (player == null || !player.isCreative()) 
            if (result.isEmpty()) {
                result = new ItemStack(Items.GLASS_BOTTLE);

            } else if (player != null) {
                ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
                if (!player.giveItemStack(stack))
                    player.dropItem(bottle, false);
            }


        if (world.isClient)
            return result;


        for (StatusEffectInstance statusEffect : SUSPICOUS_WATER_POTION.getEffects())
            user.addStatusEffect(new StatusEffectInstance(statusEffect));

        if (player != null) 
            Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity) player, stack);


        return result;
    }

    /**
     * Copy of {@link PotionItem#getMaxUseTime(ItemStack)}
     */
    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 32;
    }

    /**
     * Copy of {@link PotionItem#getUseAction(ItemStack)}
     */
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    /**
     * Copy of {@link PotionItem#use(World, PlayerEntity, Hand)}
     */
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }
}
