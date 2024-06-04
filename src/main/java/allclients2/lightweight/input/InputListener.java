package allclients2.lightweight.input;

import allclients2.lightweight.eventbus.EventBus;
import allclients2.lightweight.eventbus.Subscription;
import allclients2.lightweight.eventbus.events.ClientTickEvent;
import allclients2.lightweight.eventbus.events.InputEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;

import java.util.HashSet;
import java.util.Set;

public class InputListener {

    private Subscription<ClientTickEvent> clientTickSubscription;
    private static final Set<Integer> requestedKeyCodes = new HashSet<>();
    private static final Set<Integer> currentlyPressedKeys = new HashSet<>();

    public InputListener() {
        if (clientTickSubscription == null) {
            clientTickSubscription = EventBus.subscribe(ClientTickEvent.class, evt -> {
                checkInputs();
            });
        }
    }

    public static void addPressSubscription(int keyCode) {
        requestedKeyCodes.add(keyCode);
    }

    private static boolean isKeyPressed(int code) {
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), code);
    }

    private void checkInputs() {
        for (int keyCode : requestedKeyCodes) {
            boolean currentlyPressed = isKeyPressed(keyCode);

            if (currentlyPressed && !currentlyPressedKeys.contains(keyCode)) {
                // Key is pressed and was not previously pressed, fire event
                EventBus.publish(new InputEvent(keyCode));
                currentlyPressedKeys.add(keyCode);
            } else if (!currentlyPressed) {
                // Key is not pressed, remove from the set of currently pressed keys
                currentlyPressedKeys.remove(keyCode);
            }
        }
    }
}
