package net.epiphany.mdlrbckrms.mixins;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.epiphany.mdlrbckrms.GodOfTheBackrooms;
import net.epiphany.mdlrbckrms.levels.Levels;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.random.Random;

@Mixin(DebugHud.class)
public class DebugHudMixin {
    /**
     * Obfuscates the text on the left of the F3 menu when the player is in the Backrooms to prevent them from knowing
     *  where they are.
     */
    @Inject(method = "Lnet/minecraft/client/gui/hud/DebugHud;getLeftText()Ljava/util/List;", at = @At("RETURN"))
    private void onGetLeftText(CallbackInfoReturnable<List<String>> info) {
        MinecraftClient client = ((DebugHudAccessor) this).client();
        ClientPlayerEntity player = client.player;
        
        if (player.isCreative() || player.isSpectator())
            return;

        if (!GodOfTheBackrooms.isHim(player) && Levels.isBackrooms(player.getWorld()))
            obfuscateText(info.getReturnValue(), player.getRandom());
        
        return;
    }

    /**
     * Obfuscates the text on the right of the F3 menu when the player is in the Backrooms to prevent them from knowing
     *  where they are.
     */
    @Inject(method = "Lnet/minecraft/client/gui/hud/DebugHud;getRightText()Ljava/util/List;", at = @At("RETURN"))
    private void onGetRightText(CallbackInfoReturnable<List<String>> info) {
        MinecraftClient client = ((DebugHudAccessor) this).client();
        ClientPlayerEntity player = client.player;

        if (player.isCreative() || player.isSpectator())
            return;

        if (!GodOfTheBackrooms.isHim(player) && Levels.isBackrooms(player.getWorld()))
            obfuscateText(info.getReturnValue(), player.getRandom());
            
        return;
    }



    /**
     * Characters of approximately the same display-length for creating random strings.
     */
    private static final String OBFUSCATION_CHARACTERS = "01234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM@"
                                                       + "#$%^&*<>?(){}\\/";

    /**
     * Takes in a list of strings and modifies it to have strings of the same length with randomized, approximately same-length
     *  characters.
     * 
     * @param text   The list of strings to obfuscate.
     * @param random Random number generator.
     * @return A reference to text for method chaining.
     */
    private static List<String> obfuscateText(List<String> text, Random random) {
        List<Integer> lineLengths = new ArrayList<>();
        for (String line : text) 
            lineLengths.add(line.length());

        text.clear();

        for (int lineLength : lineLengths) {
            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < lineLength; i++)
                stringBuilder.append(OBFUSCATION_CHARACTERS.charAt(
                        random.nextInt(OBFUSCATION_CHARACTERS.length())));

            text.add(stringBuilder.toString());
        }

        return text;
    }
}