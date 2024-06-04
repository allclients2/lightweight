package allclients2.lightweight.mixins;

import allclients2.lightweight.Debug;
import allclients2.lightweight.eventbus.EventBus;
import allclients2.lightweight.eventbus.events.TitleScreenEntryEvent;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class EntryMixin {

    @Unique
    private static boolean _initialized = false;

    @Inject(at = @At("HEAD"), method = "init()V")
    private void init(CallbackInfo info) {
        if (!_initialized) {
            _initialized = true;
            Debug.logMessage("Global Init");
            EventBus.publish(new TitleScreenEntryEvent());
        }
    }
}

