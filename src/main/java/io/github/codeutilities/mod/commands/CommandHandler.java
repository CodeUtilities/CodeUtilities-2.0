package io.github.codeutilities.mod.commands;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.mod.commands.impl.image.ImageHologramCommand;
import io.github.codeutilities.mod.commands.impl.image.ImageParticleCommand;
import io.github.codeutilities.mod.commands.impl.item.BreakableCommand;
import io.github.codeutilities.mod.commands.impl.item.EditItemCommand;
import io.github.codeutilities.mod.commands.impl.item.GiveCommand;
import io.github.codeutilities.mod.commands.impl.item.ItemdataCommand;
import io.github.codeutilities.mod.commands.impl.item.RelativeLocCommand;
import io.github.codeutilities.mod.commands.impl.item.TemplatesCommand;
import io.github.codeutilities.mod.commands.impl.item.UnpackCommand;
import io.github.codeutilities.mod.commands.impl.item.template.SendTemplateCommand;
import io.github.codeutilities.mod.commands.impl.item.template.WebviewCommand;
import io.github.codeutilities.mod.commands.impl.other.*;
import io.github.codeutilities.mod.commands.impl.text.ColorCommand;
import io.github.codeutilities.mod.commands.impl.text.ColorsCommand;
import io.github.codeutilities.mod.commands.impl.text.CopyTextCommand;
import io.github.codeutilities.mod.commands.impl.text.GradientCommand;
import io.github.codeutilities.mod.commands.impl.text.UuidCommand;
import io.github.codeutilities.mod.config.Config;
import io.github.codeutilities.sys.file.ILoader;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;

public class CommandHandler implements ILoader {

    @Override
    public void load() {
        register(
                new CodeUtilitiesCommand(),
                new BreakableCommand(),
                new UnpackCommand(),
                new ItemdataCommand(),
                new UuidCommand(),
                new HeadsCommand(),
                new ColorsCommand(),
                new ColorCommand(),
                new EditItemCommand(),
                new CopyTextCommand(),
                new GradientCommand(),
                new ConfigCommand(),
                new SearchCommand(),
                new DebugCommand(),
                new QueueCommand()
        );

        if (Config.getBoolean("dfCommands")) {
            register(
                    new GiveCommand(),
                    new NodeCommand(),
                    new TemplatesCommand(),
                    new WebviewCommand(),
                    new NBSCommand(),
                    new ImageHologramCommand(),
                    new ImageParticleCommand(),
                    new SendTemplateCommand(),
                    new PJoinCommand(),
                    new ImageHologramCommand(),
                    new ImageParticleCommand(),
                    new SchemCommand(),
                    new RelativeLocCommand(),
                    new PlotsCommand()
            );
        }
    }

    public void register(Command cmd) {
        cmd.register(CodeUtilities.MC, ClientCommandManager.DISPATCHER);
    }

    public void register(Command... cmds) {
        for (Command cmd : cmds) {
            this.register(cmd);
        }
    }
}
