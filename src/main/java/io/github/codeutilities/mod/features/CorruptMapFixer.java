package io.github.codeutilities.mod.features;

import io.github.codeutilities.sys.file.ILoader;
import net.minecraft.block.MapColor;

import java.util.Arrays;

public class CorruptMapFixer implements ILoader {

    @Override
    public void load() {
        /*
        Maps contain an array of bytes that each represent their own colors.

        Minecraft has a limited amount of colors, as a result they have the map color array
        sized to have the appropriate amount of colors.
        However, not every color is used yet. This means some objects stored in the array
        are NULL.

        Because these colors do not exist you should never run into issues.
        However, the issue plaguing DiamondFire right now which causes people to crash is
        due to these colors being sent to the client. Specifically, empty maps seem to all be
        -1... which when deserialized turns out to be ~63rd index in the MapColor array.

        Simply this will replace these null colors with fake colors. This should prevent the crash.
         */
        Arrays.fill(MapColor.COLORS, 62, 64, MapColor.BLUE);
    }
}
