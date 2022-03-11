package io.github.codeutilities.mod;

import io.github.codeutilities.mod.config.Config;
import io.github.codeutilities.sys.SimplePlot;
import io.github.codeutilities.sys.file.FileUtil;
import io.github.codeutilities.sys.networking.State;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;
import java.util.ArrayList;

public class PlotHistoryRecorder {
    private static final Path FILE_PATH = FabricLoader.getInstance().getGameDir().resolve("CodeUtilities/history.sff"); // sff means stupid file format

    private static String lastPlot = null;
    public static void record(State.Plot statePlot) {
        if(statePlot == null) return;
        if(!Config.getBoolean("plotHistory")) return;
        if(lastPlot != null && lastPlot.equals(statePlot.getId())) return;

        SimplePlot plot = SimplePlot.fromStatePlot(statePlot);
        lastPlot = plot.getId();

        String fileContents = FileUtil.readFirstLine(FILE_PATH);

        ArrayList<SimplePlot> plots = SimplePlot.toPlotList(fileContents);
        while(plots.size() > Config.getInteger("plotHistoryLength")) {
            plots.remove(0);
        }
        plots.add(0, plot);
        FileUtil.writeStringToFile(FILE_PATH, SimplePlot.serializePlots(plots));
    }

    public static ArrayList<SimplePlot> getHistory() {
        return SimplePlot.toPlotList(FileUtil.readFirstLine(FILE_PATH));
    }


}
