package com.lolpop.templateutilities.commands.env2df;

import com.lolpop.templateutilities.clientcommands.LolpopClientCommandSource;
import com.lolpop.templateutilities.schematic.Schematic;
import com.lolpop.templateutilities.schematic.SchematicData;
import com.lolpop.templateutilities.util.GzFormat;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;

public class EnvLoadCommand
{
    private static String fname;
    private static String text = "";

    public static int execute(Minecraft mc, CommandContext<LolpopClientCommandSource> ctx, boolean hasOffset) throws Exception {
        File f;
        fname = StringArgumentType.getString(ctx, "location");
        if(fname.endsWith(".litematic")||fname.endsWith(".schematic"))
        {
            f = new File("schemFiles/" + StringArgumentType.getString(ctx, "location"));
        }
        else
        {
            f = new File("schemFiles/" + StringArgumentType.getString(ctx, "location") + ".schematic");
        }

        if(f.exists())
        {
            Schematic schematic = new Schematic(f);
            int[] blocks = SchematicData.getBlockFromBytes(schematic);

            int treshold = SchematicData.getTreshold(schematic);
            if(blocks.length < treshold)
            {
                mc.player.sendMessage(new StringTextComponent(ChatFormatting.RED + "The Schematic is too small/ not outside of the plot"));
                return 1;
            }
            short width = schematic.getWidth();
            short height = schematic.getHeight();
            short length = schematic.getLength();

            int xoff = 0;
            int yoff = 50;
            int zoff = 0;

            if(hasOffset)
            {
                xoff = IntegerArgumentType.getInteger(ctx, "xoff");
                yoff = IntegerArgumentType.getInteger(ctx, "yoff")+50;
                zoff = IntegerArgumentType.getInteger(ctx, "zoff");
            }

            for(int y = yoff; y < height+yoff; y++)
            {
                for(int z = zoff; z < length+zoff; z++)
                {
                    for(int x = xoff; x < width+xoff; x++)
                    {
                        int id = blocks[x];
                        if(id>=1)
                        {
                            System.out.println(id);
                        }
                        if((!(((x <= 50) && (x >= 0 )) || ((z <= 50) && (z >= 0)))))
                        {
                            text += x + "," + y + "," + z + "," + id + ";";
                        }
                    }
                }
            }
            String code = convert();

            byte[] b64 = GzFormat.encryptBase64(GzFormat.compress(code.getBytes()));
            String exported = new String(b64);
            final CompoundNBT nbt = new CompoundNBT();

            nbt.putString("author", mc.player.getName().getString());
            nbt.putString("name", fname);
            nbt.putInt("version", 1);
            nbt.putString("code", exported);

            final CompoundNBT itemNbt = new CompoundNBT();
            final CompoundNBT publicBukkitNbt = new CompoundNBT();

            publicBukkitNbt.putString("hypercube:codetemplatedata", nbt.toString());
            itemNbt.put("PublicBukkitValues", publicBukkitNbt);

            ItemStack stack = new ItemStack(Items.SPRUCE_LOG);
            stack.setTag(itemNbt);

            stack.setDisplayName(new StringTextComponent(ChatFormatting.DARK_PURPLE + "ENVIRONMENT" + ChatFormatting.GRAY + " - " + ChatFormatting.WHITE + " " + fname));

            mc.playerController.sendPacketDropItem(stack);
        }
        return 1;
    }

    public static String convert()
    {
        String codeFormat = "{\"id\":\"block\",\"block\":\"func\",\"args\":{\"items\":[{\"item\":{\"id\":\"bl_tag\",\"data\":{\"option\":\"False\",\"tag\":\"Is Hidden\",\"action\":\"dynamic\",\"block\":\"func\"}},\"slot\":26}]},\"data\":\"%s\"}";
        String code = "";
        String[] texts = text.split(";");

        String createListFormat = "{\"id\":\"block\",\"block\":\"set_var\",\"args\":{\"items\":[{\"item\":{\"id\":\"var\",\"data\":{\"name\":\"data\",\"scope\":\"local\"}},\"slot\":0}%s]},\"action\":\"CreateList\"}";
        String appendValueFormat = "{\"id\":\"block\",\"block\":\"set_var\",\"args\":{\"items\":[{\"item\":{\"id\":\"var\",\"data\":{\"name\":\"data\",\"scope\":\"local\"}},\"slot\":0}%s]},\"action\":\"AppendValue\"}";
        String textFormat = ", {\"item\":{\"id\":\"txt\",\"data\":{\"name\":\"%s\"}},\"slot\":%d}";

        String currentBlock = "";

        String currentNotes = "";

        if(fname.length() > 12)
        {
            code = String.format(codeFormat, fname.substring(0, 12));
        }
        else
        {
            code = String.format(codeFormat, fname);
        }

        int slot = 1;
        boolean createList = true;
        for(String s: texts)
        {
            if(slot == 26)
            {


                if(createList)
                {
                    code = code + ", " + String.format(createListFormat, currentBlock);
                    createList = false;
                }
                else
                {
                    code = code + ", " + String.format(appendValueFormat, currentBlock);
                }

                currentBlock = "";
                slot = 1;
            }



            if(currentNotes.length() < 1900)
            {

            }
            else
            {
                currentBlock = currentBlock + String.format(textFormat, currentNotes, slot);
                currentNotes = "";
                slot++;
            }


            currentNotes = currentNotes + "=" + s;
        }

        if(slot != 26)
        {
            currentBlock = currentBlock + String.format(textFormat, currentNotes, slot);
            currentNotes = "";
            slot++;
        }
        else
        {
            currentBlock = currentBlock + String.format(textFormat, currentNotes, 1);
            currentNotes = "";
        }

        if(createList)
        {
            code = code + ", " + String.format(createListFormat, currentBlock);
            createList = false;
        }
        else
        {
            code = code + ", " + String.format(appendValueFormat, currentBlock);
        }

        return "{\"blocks\": [" + code + "]}";
    }
}
