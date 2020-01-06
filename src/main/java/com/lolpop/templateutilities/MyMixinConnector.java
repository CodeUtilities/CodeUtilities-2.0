package com.lolpop.templateutilities;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class MyMixinConnector implements IMixinConnector
{
    @Override
    public void connect()
    {
        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            Mixins.addConfiguration(
                    "assets/templateutilities/templateutilities.mixins_client.json"
            );
        }
    }
}
