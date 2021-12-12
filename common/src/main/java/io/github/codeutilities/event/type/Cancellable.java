package io.github.codeutilities.event.type;

public class Cancellable {

    private boolean cancelled = false;

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public boolean isCancelled() {
        return cancelled;
    }

}
