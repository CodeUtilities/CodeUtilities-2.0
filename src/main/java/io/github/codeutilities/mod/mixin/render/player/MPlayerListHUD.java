package io.github.codeutilities.mod.mixin.render.player;

import io.github.codeutilities.mod.config.Config;
import io.github.codeutilities.mod.features.social.tab.CodeUtilitiesServer;
import io.github.codeutilities.mod.features.social.tab.User;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(PlayerListHud.class)
public abstract class MPlayerListHUD {
    private static Text SPACE = Text.of(" ");

    @Shadow protected abstract Text applyGameModeFormatting(PlayerListEntry entry, MutableText name);

    @Inject(method = "getPlayerName", at = @At("RETURN"), cancellable = true)
    public void getPlayerName(PlayerListEntry entry, CallbackInfoReturnable<Text> cir) {
        if(!Config.getBoolean("loadTabStars")) return;
        if(CodeUtilitiesServer.getUserAmount() == 0) return;

        UUID id = entry.getProfile().getId();

        Text name = entry.getDisplayName() != null
                ? this.applyGameModeFormatting(entry, entry.getDisplayName().shallowCopy())
                : this.applyGameModeFormatting(entry, Team.decorateName(entry.getScoreboardTeam(), new LiteralText(entry.getProfile().getName())));
        User user = CodeUtilitiesServer.getUser(id.toString());

        if (user != null) {
            LiteralText star = new LiteralText(user.getStar().strip());
            if (Config.getBoolean("relocateTabStars")) {
                name = name.shallowCopy().append(SPACE).append(star);
            } else {
                name = star.append(SPACE).append(name);
            }
        }

        cir.setReturnValue(name);
    }
}
