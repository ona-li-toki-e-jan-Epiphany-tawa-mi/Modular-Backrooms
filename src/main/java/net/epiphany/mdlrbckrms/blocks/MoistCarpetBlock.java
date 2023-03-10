package net.epiphany.mdlrbckrms.blocks;

import net.epiphany.mdlrbckrms.items.MBItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

/**
 * The moist carpeting that appears in Level 0.
 */
public class MoistCarpetBlock extends Block {
    public MoistCarpetBlock(Settings settings) {
        super(settings);
    }

    /**
     * Allows players to fill glass bottles with the "water" in the carpet.
     */
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos position, PlayerEntity player, Hand hand,
            BlockHitResult hit) {
        ItemStack item = hand == Hand.MAIN_HAND ? player.getMainHandStack() : player.getOffHandStack();
        
        if (!Items.GLASS_BOTTLE.equals(item.getItem()))
            return ActionResult.PASS;
        

        if (world.isClient)
            return ActionResult.success(true);


        // Gross sound effect like wringing out moist carpet ;).
        world.playSound( null, player.getX(), player.getY(), player.getZ()
                       , BlockSoundGroup.MOSS_BLOCK.getStepSound(), SoundCategory.NEUTRAL
                       , 1.0f, 1.0f);

        // Fills bottle with "water."
        if (world.getRandom().nextInt(7) == 0) {
            ItemStack sussyWater = MBItems.SUSPICOUS_WATER.getDefaultStack(); // ;)
            player.setStackInHand(hand, ItemUsage.exchangeStack(item, player, sussyWater)); 
            
            world.playSound( null, player.getX(), player.getY(), player.getZ()
                        , SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL
                        , 1.0f, 1.0f);
            world.emitGameEvent(player, GameEvent.FLUID_PICKUP, position);

            player.incrementStat(Stats.USED.getOrCreateStat(Items.GLASS_BOTTLE));
        }

        return ActionResult.success(false);
    }
}
