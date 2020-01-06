package com.lolpop.templateutilities.schematic;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class SchematicData
{
    public static int[] getBlockFromBytes(Schematic schematic)
    {
        byte[] b = schematic.getBlocks();
        int[] blocks = new int[b.length];
        try
        {
            int i = 0;
            for(byte by : b)
            {
                Byte data = by;
                blocks[i] = data.intValue();
            }
            return blocks;
        }catch(Exception e)
        {
            return null;
        }
    }

    public static int getTreshold(Schematic schematic)
    {
        short width = schematic.getWidth();
        short height = schematic.getHeight();
        short length = schematic.getLength();

        int treshold = width*height*length;
        if(length <= 50||width <= 50)
        {
            treshold = 999999999;
        }
        return treshold;
    }
}
