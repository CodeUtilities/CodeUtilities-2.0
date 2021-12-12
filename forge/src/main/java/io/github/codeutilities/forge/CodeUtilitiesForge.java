package io.github.codeutilities.forge;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.util.Platform;
import net.minecraftforge.fml.common.Mod;

@Mod("codeutilities")
public class CodeUtilitiesForge {
    public CodeUtilitiesForge() {
        CodeUtilities.platform = Platform.FORGE;
        CodeUtilities.init();
    }
}
