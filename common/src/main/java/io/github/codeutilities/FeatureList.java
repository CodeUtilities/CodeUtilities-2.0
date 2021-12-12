package io.github.codeutilities;

import io.github.codeutilities.commands.CommandHandler;
import java.util.ArrayList;
import java.util.List;

public class FeatureList {

    public static List<Feature> getFeatures() {
        List<Feature> features = new ArrayList<>(List.of(
            new FeatureImpl("Scripting","Create simple scripts in a df like text language to automate tasks.")
        ));

        features.addAll(CommandHandler.getCommands());

        return features;
    }

    public interface Feature {
        String getName();
        String getDescription();
    }

    private record FeatureImpl(String name, String description) implements Feature {

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getDescription() {
            return description;
        }
    }
}