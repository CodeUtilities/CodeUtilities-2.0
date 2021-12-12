package io.github.codeutilities.feature;

public class SimpleFeature implements Feature {

    private final String name, description;

    public SimpleFeature(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }
}