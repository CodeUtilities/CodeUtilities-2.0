package io.github.codeutilities.commands;

import com.mojang.brigadier.CommandDispatcher;
import io.github.codeutilities.commands.item.GiveCommand;
import io.github.codeutilities.commands.item.LoreCommand;
import io.github.codeutilities.commands.nbs.NBSCommand;
import io.github.codeutilities.gui.ExampleGui;
import io.github.cottonmc.clientcommands.ArgumentBuilders;
import io.github.cottonmc.clientcommands.ClientCommandPlugin;
import io.github.cottonmc.clientcommands.CottonClientCommandSource;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.minecraft.client.MinecraftClient;

public class Commands implements ClientCommandPlugin {

   @Override
   public void registerCommands(CommandDispatcher<CottonClientCommandSource> cd) {
      GiveCommand.register(cd);
      LoreCommand.register(cd);
      NBSCommand.register(cd);

      cd.register(ArgumentBuilders.literal("guitest").executes(ctx -> {
         MinecraftClient.getInstance().openScreen(new CottonClientScreen(new ExampleGui()));
         return 1;
      }));
   }
}
