package allclients2.lightweight;

import allclients2.lightweight.eventbus.EventBus;
import allclients2.lightweight.eventbus.events.TitleScreenEntryEvent;
import allclients2.lightweight.input.InputListener;
import allclients2.lightweight.measure.TickRateMeasurer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

public class LightWeightClient implements ClientModInitializer {
	private static LightWeightClient INSTANCE;

	// Instances
	public static RenderUI renderUI;
	public static TickRateMeasurer tickRateMeasurer;
	public static InputListener inputListener;

	@Override
	public void onInitializeClient() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// As such, nothing will be loaded here but basic initialization.
		System.out.println("Client load!");
		EventBus.subscribe(TitleScreenEntryEvent.class, evt -> onInitializeLoad());
		INSTANCE = this;
	}

	public void onInitializeLoad() {
		System.out.println("Initialize load!");
		renderUI = new RenderUI();
		tickRateMeasurer = new TickRateMeasurer();
		inputListener = new InputListener();
	}

	public static ClientPlayerEntity getPlayer() {
		return MinecraftClient.getInstance().player;
	}
}