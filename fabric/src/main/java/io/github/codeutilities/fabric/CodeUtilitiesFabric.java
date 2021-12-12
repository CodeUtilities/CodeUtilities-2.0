package io.github.codeutilities.fabric;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.Platform;
import net.fabricmc.api.ModInitializer;

public class CodeUtilitiesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        CodeUtilities.platform = Platform.FABRIC;
        CodeUtilities.init();
    }
}
