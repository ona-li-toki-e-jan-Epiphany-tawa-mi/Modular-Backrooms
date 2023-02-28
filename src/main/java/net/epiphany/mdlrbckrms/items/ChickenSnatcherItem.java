package net.epiphany.mdlrbckrms.items;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * An item that lets you snatch up chickens as items.
 */
public class ChickenSnatcherItem extends ToolItem {
    public static final Identifier CHICKEN_SNATCHER_ID = new Identifier(ModularBackrooms.MOD_ID, "chicken_snatcher");
    public static final ChickenSnatcherItem CHICKEN_SNATCHER = new ChickenSnatcherItem(ToolMaterials.WOOD
                                                                     , new FabricItemSettings().maxDamage(64));

    public static void register() {
        Registry.register(Registries.ITEM, CHICKEN_SNATCHER_ID, CHICKEN_SNATCHER);
    }

    public static void registerItemUnderGroup(FabricItemGroupEntries content) {
        content.add(CHICKEN_SNATCHER);
    }



    public ChickenSnatcherItem(ToolMaterial material, Settings settings) {
        super(material, settings);
    }



    /**
     * Snatches up adult chickens and gives them to you as an item.
     */
    @Override
    public ActionResult useOnEntity(ItemStack item, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (!(entity instanceof ChickenEntity chicken) || chicken.isBaby())
            return ActionResult.PASS;
        
        World world = user.getWorld();
        if (world.isClient)
            return ActionResult.success(true);


        ServerWorld serverWorld = (ServerWorld) world;
        Vec3d lookPosition = user.getEyePos().add(user.getRotationVector());

        serverWorld.spawnParticles( ParticleTypes.SWEEP_ATTACK
                                  , lookPosition.getX(), lookPosition.getY(), lookPosition.getZ()
                                  , 1
                                  , 0.0, 0.0, 0.0
                                  , 0.0);
        serverWorld.playSound( null
                             , user.getBlockPos()
                             , SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS
                             , 1.0f, user.getPitch());
        ChickenItem.playChickenSound(serverWorld, chicken.getBlockPos(), SoundEvents.ENTITY_CHICKEN_HURT);


        chicken.remove(RemovalReason.DISCARDED);

        ItemStack chickenItem = ChickenItem.CHICKEN.getDefaultStack();
        // If chicken was named we can retain that on the item; if the chicken is placed they will have it still.
        if (chicken.hasCustomName())
            chickenItem.setCustomName(chicken.getCustomName());

        if (!user.giveItemStack(chickenItem))
            user.dropItem(chickenItem, false);


        item.damage(1, user, (userr) -> userr.sendToolBreakStatus(hand));
        return ActionResult.success(false);
    }
}
