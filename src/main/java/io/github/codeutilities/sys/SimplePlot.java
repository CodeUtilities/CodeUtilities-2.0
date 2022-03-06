package io.github.codeutilities.sys;

import io.github.codeutilities.sys.networking.State;

import java.util.ArrayList;
import java.util.Arrays;

public class SimplePlot {
    private final String id;
    private final String name;
    private final String owner;

    public SimplePlot(String id, String name, String owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public static SimplePlot fromStatePlot(State.Plot plot) {
        return new SimplePlot(plot.getId(), plot.getName().replaceAll("[,;]", "?"), plot.getOwner());
    }

    @Override
    public String toString() {
        return id + ";" + name + ";" + owner;
    }

    public static ArrayList<SimplePlot> toPlotList(String serialized) {
        String[] serializedPlots = serialized.length() == 0 ? null : serialized.split(",");
        ArrayList<SimplePlot> plots = new ArrayList<>();
        if(serializedPlots == null)
            return plots;

        for(String serializedPlot : serializedPlots) {
            String[] props = serializedPlot.split(";");
            plots.add(new SimplePlot(props[0], props[1], props[2]));
        }
        return plots;
    }

    public static String serializePlots(ArrayList<SimplePlot> plots) {
        StringBuilder builder = new StringBuilder();
        for(SimplePlot plot : plots) {
            builder.append(",").append(plot.toString());
        }
        return builder.toString().replaceAll("^,", "");
    }
}
