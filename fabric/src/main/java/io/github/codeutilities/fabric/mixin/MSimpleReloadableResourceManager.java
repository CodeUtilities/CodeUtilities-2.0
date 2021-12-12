package io.github.codeutilities.fabric.mixin;

import java.io.InputStream;
import java.net.URL;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.SimpleReloadableResourceManager;
import net.minecraft.server.packs.resources.SimpleResource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SimpleReloadableResourceManager.class)
public class MSimpleReloadableResourceManager {

    @Inject(method = "getResource", at = @At("HEAD"), cancellable = true)
    public void getResource(ResourceLocation loc, CallbackInfoReturnable<Resource> cir) {
        if (loc.getNamespace().equals("codeutilities")) {
            try {
                cir.cancel();

                FabricLoader fl = FabricLoader.getInstance();

                URL url = getClass().getResource("/assets/codeutilities/" + loc.getPath());

                InputStream stream = url.openStream();

                SimpleResource resource = new SimpleResource(
                    loc.getPath(), loc,
                    stream,
                    null
                );
                cir.setReturnValue(resource);
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
    }

}
