package net.epiphany.mdlrbckrms.blocks;

import net.minecraft.block.BlockSetType;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A version of {@link DoorBlock} that will let players open them on right-click even if the material is set to metal.
 */
public class OpenableMetalDoorBlock extends DoorBlock {
    public OpenableMetalDoorBlock(Settings settings, BlockSetType blockSetType) {
        super(settings, blockSetType);
    }
    
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hit) {
        this.setOpen(player, world, state, pos, !state.get(OPEN));
        return ActionResult.success(world.isClient);
    }
}
