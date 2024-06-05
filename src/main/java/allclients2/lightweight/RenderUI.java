package allclients2.lightweight;

import allclients2.lightweight.eventbus.EventBus;
import allclients2.lightweight.eventbus.events.ClientRenderEvent;
import allclients2.lightweight.eventbus.events.InputEvent;
import allclients2.lightweight.input.InputListener;
import allclients2.lightweight.measure.TickRateMeasurer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class RenderUI {
    private boolean isRenderActivated = true;

    public RenderUI() {
        System.out.println("Subscribe Render!");
        EventBus.subscribe(ClientRenderEvent.class, evt -> { if (isRenderActivated) { renderStep(evt.stack); } });
        InputListener.addPressSubscription(GLFW.GLFW_KEY_F4);
        EventBus.subscribe(InputEvent.class, evt -> {
            if (evt.keyCode == GLFW.GLFW_KEY_F4) {
                isRenderActivated = !isRenderActivated;
                TickRateMeasurer.toggleUse(isRenderActivated);
            }
        });
    }

    private static void renderStep(MatrixStack matrixStack) {
        final ClientPlayerEntity player = LightWeightClient.getPlayer();
        if (player != null) { // Only render is player is present.
            textDrawer.update(matrixStack);
            textDrawer.drawShadedText("FPS: " + MinecraftClient.getInstance().getCurrentFps(), 13, 7, new Color(166,166,166).getRGB());
            textDrawer.drawShadedText("CTPS: " + formatNum(TickRateMeasurer.getAverageTickRate()), 13, 17, new Color(166,166,166).getRGB());
            textDrawer.drawShadedText("POS: " + formatPos(player.getPos()), 13, 27, new Color(166,166,166).getRGB());
        }
    }

    private static String formatPos(Vec3d position) {
        return formatNum(position.x) + ", " + formatNum(position.y) + ", " + formatNum(position.z);
    }

    private static String formatNum(double num) {
        return String.format("%.2f", num);
    }

    private static String formatNum(float num) {
        return String.format("%.2f", num);
    }

    private static class textDrawer {
        private static TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        private static VertexConsumerProvider vertexConsumerProvider = MinecraftClient.getInstance().getBufferBuilders().getOutlineVertexConsumers();
        private static Matrix4f matrix;
        public static void drawShadedText(String text, float x, float y, int shade) {
            textRenderer.draw(text, x, y, shade, true, matrix, vertexConsumerProvider, TextRenderer.TextLayerType.SEE_THROUGH, 0, 255);
        }

        public static void update(MatrixStack matrixStack) {
            matrixStack.push();
            matrix = matrixStack.peek().getPositionMatrix();
            matrixStack.pop();
            textRenderer = MinecraftClient.getInstance().textRenderer;
            vertexConsumerProvider = MinecraftClient.getInstance().getBufferBuilders().getOutlineVertexConsumers();
        }
    }
}
