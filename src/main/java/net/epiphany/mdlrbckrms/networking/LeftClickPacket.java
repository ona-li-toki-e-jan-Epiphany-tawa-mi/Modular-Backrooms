package net.epiphany.mdlrbckrms.networking;

import net.epiphany.mdlrbckrms.ModularBackrooms;
import net.epiphany.mdlrbckrms.utilities.LeftClickEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

/**
 * Handles sending left-clicks from the client to the server.
 * TODO Consider making this part of a library mod.
 */
public class LeftClickPacket {
    public static final Identifier LEFT_CLICK_PACKET_ID = new Identifier(ModularBackrooms.MOD_ID, "left_click");

    /**
     * Sends left-clicks to the server.
     * 
     * @param timesPressed The number of times that the left-click button was pressed prior to sending the packet.
     */
    @Environment(EnvType.CLIENT)
    public static void sendToServer(int timesPressed) {
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(timesPressed);

        ClientPlayNetworking.send(LEFT_CLICK_PACKET_ID, buffer);
    }

    /**
     * Registers the listener that handles this packet to run the left-click events server-side.
     */
    public static void registerServerListener() {
        ServerPlayNetworking.registerGlobalReceiver(LEFT_CLICK_PACKET_ID, (server, player, handler, buffer, responseSender) -> {
            int timesPressed = buffer.readInt();
            if (timesPressed <= 0)
                return;

            server.execute(() -> {
                World world = player.getWorld();
                ItemStack item = player.getMainHandStack();

                for (int i = 0; i < timesPressed; i++)
                    LeftClickEvent.ON_LEFT_CLICK.invoker().onLeftClick(world, player, item);
            });
        });
    }
}