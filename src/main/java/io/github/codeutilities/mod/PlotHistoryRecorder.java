package io.github.codeutilities.mod;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.mod.config.Config;
import io.github.codeutilities.sys.SimplePlot;
import io.github.codeutilities.sys.networking.State;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.util.ArrayList;

public class PlotHistoryRecorder {
    private static final String FILE_PATH = "CodeUtilities/history.sff"; // sff means stupid file format

    private static String lastPlot = null;
    public static void record(State.Plot statePlot) {
        if(statePlot == null) return;
        if(!Config.getBoolean("plotHistory")) return;
        if(lastPlot != null && lastPlot.equals(statePlot.getId())) return;

        SimplePlot plot = SimplePlot.fromStatePlot(statePlot);
        lastPlot = plot.getId();

        String leFile = readLeFile();

        ArrayList<SimplePlot> plots = SimplePlot.toPlotList(leFile);
        while(plots.size() > Config.getInteger("plotHistoryLength")) {
            plots.remove(0);
        }
        plots.add(plot);
        writeLeFile(SimplePlot.serializePlots(plots));
    }

    public static ArrayList<SimplePlot> getHistory() {
        return SimplePlot.toPlotList(readLeFile());
    }

    private static String readLeFile() {
        File file = new File(FILE_PATH);

        try {
            if(!file.exists()) {
                boolean result = file.createNewFile();
                CodeUtilities.log(Level.INFO, FILE_PATH + " does not exist. creating it.");
                if(!result)
                    CodeUtilities.log(Level.INFO, "failed creating " + FILE_PATH);

            }

            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
            String line = reader.readLine();
            reader.close();
            return line == null ? "" : line;
        } catch(IOException exception) {
            CodeUtilities.log(Level.WARN, "Failed to read " + FILE_PATH);
            exception.printStackTrace();
            return "";
        }
    }

    private static void writeLeFile(String content) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            CodeUtilities.log(Level.WARN, "Failed writing to " + FILE_PATH);
            e.printStackTrace();
        }
    }

}
