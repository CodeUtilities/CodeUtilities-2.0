package com.lolpop.templateutilities;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.launch.MixinBootstrap;

import java.io.File;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("templateutilities")
@Mod.EventBusSubscriber()
public class Templateutilities
{
    @SubscribeEvent
    public void onInitialize(FMLClientSetupEvent event)
    {
        MixinBootstrap.init();
        File folder = new File("nbsFiles");
        if(!folder.exists())
        {
            folder.mkdir();
        }
        folder = new File("schemFiles");
        if(!folder.exists())
        {
            folder.mkdir();
        }
    }
}
