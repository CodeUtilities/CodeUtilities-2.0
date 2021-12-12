package io.github.codeutilities.feature;

import io.github.codeutilities.commands.CommandHandler;
import java.util.ArrayList;
import java.util.List;

public class FeatureList {

    public static List<Feature> getFeatures() {
        List<Feature> features = new ArrayList<>(List.of(
            new SimpleFeature("Scripting","Create simple scripts in a df like text language to automate tasks.")
        ));

        features.addAll(CommandHandler.getCommands());

        return features;
    }
}