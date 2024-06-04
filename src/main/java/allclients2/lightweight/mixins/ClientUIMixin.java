package allclients2.lightweight.mixins;

import allclients2.lightweight.eventbus.EventBus;
import allclients2.lightweight.eventbus.events.ClientRenderEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public final class ClientUIMixin {
    @Inject(
            method = "render",
            at = @At("TAIL")
    )
    private void clientRender(DrawContext context, float tickDelta, CallbackInfo ci) {
        EventBus.publish(new ClientRenderEvent(context.getMatrices(), tickDelta));
    }
}
