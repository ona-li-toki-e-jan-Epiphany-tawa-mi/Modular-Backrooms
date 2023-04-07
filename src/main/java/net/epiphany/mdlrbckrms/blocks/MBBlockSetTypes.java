package net.epiphany.mdlrbckrms.blocks;

import net.epiphany.mdlrbckrms.utilities.MBSounds;
import net.minecraft.block.BlockSetType;
import net.minecraft.sound.BlockSoundGroup;

/**
 * A set of custom {@link BlockSetType} for Modular Backrooms.
 */
public class MBBlockSetTypes {
    public static final BlockSetType CREAKY_WOOD = new BlockSetType( "creaky_wood", BlockSoundGroup.WOOD
                                                                   , MBSounds.DOOR_CREAKS, MBSounds.DOOR_CREAKS
                                                                   , MBSounds.DOOR_CREAKS, MBSounds.DOOR_CREAKS
                                                                   , MBSounds.DOOR_CREAKS, MBSounds.DOOR_CREAKS
                                                                   , MBSounds.DOOR_CREAKS, MBSounds.DOOR_CREAKS);
}