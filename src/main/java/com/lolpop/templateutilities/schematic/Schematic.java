package com.lolpop.templateutilities.schematic;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.ListNBT;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Schematic
{
    private short width;
    private short height;
    private short length;

    private byte[] blocks;
    private byte[] data;

    private ListNBT entities;
    private ListNBT tileentities;

    public Schematic(File f) throws IOException
    {
        InputStream fis = new FileInputStream(f);
        CompoundNBT nbtdata = CompressedStreamTools.readCompressed(fis);


        this.width = nbtdata.getShort("Width");
        this.height = nbtdata.getShort("Height");
        this.length = nbtdata.getShort("Length");

        this.blocks = nbtdata.getByteArray("Blocks");
        this.data = nbtdata.getByteArray("Data");

        this.entities = nbtdata.getList("Entities", 8);
        this.tileentities = nbtdata.getList("TileEntities", 8);

        fis.close();
    }

    public short getWidth()
    {
        return width;
    }

    public short getHeight()
    {
        return height;
    }

    public short getLength()
    {
        return length;
    }

    public byte[] getBlocks()
    {
        return blocks;
    }

    public byte[] getData()
    {
        return data;
    }

    public List getEntities()
    {
        return entities;
    }

    public List getTileentities()
    {
        return tileentities;
    }
}
