package allclients2.lightweight;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;

// TODO: Debug library or use Minecraft's built in debugger
public class Debug {

    private static final boolean prefix = true;

    public static void logMessage(String message) {
        if (prefix) {
            message = "\u00A72\u00A7l\u00A7o" + "testmod" + "\u00A7r" + message;
        }
        if (MinecraftClient.getInstance() != null && MinecraftClient.getInstance().player != null) {
            MinecraftClient.getInstance().player.sendMessage(Text.of(message), false);
        } else {
            System.out.println(message);
        }
    }

}
