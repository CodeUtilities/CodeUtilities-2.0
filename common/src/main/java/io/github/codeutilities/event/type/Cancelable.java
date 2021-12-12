package io.github.codeutilities.event.type;

public interface Cancelable {

    void setCancelled(boolean cancel);

    boolean isCancelled();

}
