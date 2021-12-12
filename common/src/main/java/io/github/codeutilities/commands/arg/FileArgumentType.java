package io.github.codeutilities.commands.arg;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.io.File;
import java.util.concurrent.CompletableFuture;

public class FileArgumentType implements ArgumentType<String> {

    File folder;

    public FileArgumentType(File folder) {
        this.folder = folder;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (File file : folder.listFiles()) {
            if (file.isFile()) {
                String name = file.getName();
                if (name.toLowerCase().startsWith(builder.getRemaining().toLowerCase())) {
                    builder.suggest(name);
                }
            }
        }
        return builder.buildFuture();
    }

    @Override
    public String parse(StringReader reader) {
        String text = reader.getRemaining();
        reader.setCursor(reader.getTotalLength());
        return text;
    }
}
