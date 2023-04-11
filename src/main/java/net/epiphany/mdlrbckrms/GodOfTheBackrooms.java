package net.epiphany.mdlrbckrms;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

/**
 * The inner machinations of my mind are an enigma.
 */
public class GodOfTheBackrooms {
    /**
     * The one and only
     */
    private static final String HIM = "DarkLordDudeALTA";

    /**
     * A means of identification.
     * 
     * @param entity The entity to test.
     * @return The truth that may now be seen.
     */
    public static boolean isHim(Entity entity) {
        return entity instanceof PlayerEntity player && HIM.equals(player.getEntityName());
    }


    /*
    TODO shift all of this code to a custom item or something that anyone can use.
    private static final double ENTITY_TELEPORT_RANGE = 12.0;
    private static final double CREATE_RIFT_RANGE = 10.0;
    private static int activationTime = 0;

    public static void onPlayerTick(PlayerEntity player) {
        if (player.getWorld().isClient || !isHim(player))
            return;

        ServerWorld world = (ServerWorld) player.getWorld();


            // TODO find extra control for this
        if (player.isInPose(EntityPose.CROUCHING)) {
            activationTime++;

            if (activationTime % 10 == 0) {
                Vec3d lookPosition = player.getEyePos().add(player.getRotationVector());

                world.spawnParticles( ParticleTypes.SONIC_BOOM
                                    , lookPosition.getX(), lookPosition.getY(), lookPosition.getZ()
                                    , 1
                                    , 0.0, 0.0, 0.0
                                    , 1.0);
            }

        } else if (activationTime > 0)
            activationTime--;


        if (activationTime > 80) {
            final double EPSILON = 0.001;

            if (player.getPitch() > 90.0 - EPSILON) {
                shiftEntity(world, player);

            } else {
                Vec3d playerEyePosition = player.getEyePos();
                Vec3d raycastEndPoint = playerEyePosition.add(player.getRotationVector().multiply(ENTITY_TELEPORT_RANGE));
                EntityHitResult entityRaycastResult = ProjectileUtil.getEntityCollision( world
                                                                                       , player
                                                                                       , playerEyePosition, raycastEndPoint
                                                                                       , new Box(playerEyePosition, raycastEndPoint)
                                                                                                .expand(1.0)
                                                                                       , entity -> !entity.isSpectator()
                                                                                       , 0.0f);
                                                                        
                if (entityRaycastResult != null && entityRaycastResult.getType() == HitResult.Type.ENTITY 
                        && player.canSee(entityRaycastResult.getEntity())) {
                    shiftEntity(world, entityRaycastResult.getEntity());

                } else {
                    HitResult raycastResult = player.raycast(CREATE_RIFT_RANGE, 1.0f, true);
                    createOrDestoryRift(world, player, raycastResult);
                }
            }

            activationTime = 0;
        }
    }

    // TODO make this teleport to nearby valid location and remember (for him) last backrooms and non-backrooms dimension.
    private static void shiftEntity(ServerWorld world, Entity entity) {
        Random random = world.getRandom();

        world.playSound( null
                       , entity.getBlockPos()
                       , SoundEvents.ENTITY_ENDERMAN_TELEPORT, entity.getSoundCategory()
                       , 1.0f, -0.5f);
        world.emitGameEvent(entity, GameEvent.TELEPORT, entity.getPos());

        Vec3d eyePostion = entity.getEyePos();
        world.spawnParticles( ParticleTypes.SONIC_BOOM
                            , eyePostion.getX(), eyePostion.getY(), eyePostion.getZ()
                            , 1
                            , 0.0, 0.0, 0.0
                            , 1.0);

        if (!Levels.isBackrooms(world)) {
            DimensionHelper.teleportToDimension(entity, Level0.LEVEL_0, random);
        } else
            DimensionHelper.teleportToDimension(entity, world.getServer().getOverworld(), random);
    }

    private static void createOrDestoryRift(ServerWorld world, PlayerEntity player, HitResult raycastResult) {
        BlockPos riftPosition = null;
        boolean success = false;

        switch (raycastResult.getType()) {
            case BLOCK: {
                BlockHitResult blockRaycastResult = (BlockHitResult) raycastResult;
                riftPosition = blockRaycastResult.getBlockPos();
                BlockState state = world.getBlockState(riftPosition);

                if (state.isOf(MBBlocks.RIFT)) {
                    world.breakBlock(riftPosition, false, player);
                    
                } else {
                    if (!world.getBlockState(riftPosition).isReplaceable()) {
                        riftPosition = riftPosition.offset(blockRaycastResult.getSide());

                        if (!world.getBlockState(riftPosition).isReplaceable())
                            break;
                    }

                    Direction.Axis axis = player.getHorizontalFacing().rotateYClockwise().getAxis();
                    world.setBlockState(riftPosition, MBBlocks.RIFT.getDefaultState().with(NetherPortalBlock.AXIS, axis));
                }

                success = true;

                break;
            }

            case MISS: {
                riftPosition = new BlockPos( (int) raycastResult.getPos().getX()
                                           , (int) raycastResult.getPos().getY()
                                           , (int) raycastResult.getPos().getZ());

                if (world.getBlockState(riftPosition).isReplaceable()) {
                    Direction.Axis axis = player.getHorizontalFacing().rotateYClockwise().getAxis();
                    world.setBlockState(riftPosition, MBBlocks.RIFT.getDefaultState().with(NetherPortalBlock.AXIS, axis));

                    success = true;
                }

                break;
            }

            default:
                break;
        }

        if (success) {
            world.emitGameEvent(player, GameEvent.BLOCK_PLACE, riftPosition);
            world.playSound(null, riftPosition, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.0f, -0.5f);

            world.spawnParticles( ParticleTypes.SONIC_BOOM
                                , riftPosition.getX() + 0.5, riftPosition.getY() + 0.5, riftPosition.getZ() + 0.5
                                , 1
                                , 0.0, 0.0, 0.0
                                , 1.0);
        }
    }*/
}
