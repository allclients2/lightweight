package allclients2.lightweight.eventbus.events;

public class SendChatEvent {
    public String message;
    private boolean cancelled;

    public SendChatEvent(String message) {
        this.message = message;
    }

    public void cancel() {
        cancelled = true;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
