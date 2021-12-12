package io.github.codeutilities.event.type;

public interface Cancellable {

    boolean isCancelled();

    void setCancelled(boolean cancel);

}
