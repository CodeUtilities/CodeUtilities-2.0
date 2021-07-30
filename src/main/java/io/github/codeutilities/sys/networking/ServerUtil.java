package io.github.codeutilities.sys.networking;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.sys.player.DFInfo;

public class ServerUtil {

    /**
     * Checks if the Minecraft client is currently on DiamondFire.
     *
     * @return True if connected to DF, otherwise false.
     */
    public static boolean isOnDF() {
        if (CodeUtilities.MC.getCurrentServerEntry() == null) return false;
        return CodeUtilities.MC.getCurrentServerEntry().address.contains(DFInfo.IP);
    }

}
