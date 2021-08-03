package io.github.codeutilities.mod.features.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.mod.features.commands.nbs.NBSToTemplate;
import io.github.codeutilities.mod.features.commands.nbs.SongData;
import io.github.codeutilities.mod.features.social.tab.CodeUtilitiesServer;
import io.github.codeutilities.sys.hypercube.templates.TemplateUtils;
import io.github.codeutilities.sys.renderer.IMenu;
import io.github.codeutilities.sys.util.ItemUtil;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabeledSlider;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WScrollPanel;
import io.github.cottonmc.cotton.gui.widget.WText;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;

public class NbsSearchMenu extends LightweightGuiDescription implements IMenu {

    public final String query;
    private final SoundEvent[] instrumentids = {
        SoundEvents.BLOCK_NOTE_BLOCK_HARP,
        SoundEvents.BLOCK_NOTE_BLOCK_BASS,
        SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM,
        SoundEvents.BLOCK_NOTE_BLOCK_SNARE,
        SoundEvents.BLOCK_NOTE_BLOCK_HAT,
        SoundEvents.BLOCK_NOTE_BLOCK_GUITAR,
        SoundEvents.BLOCK_NOTE_BLOCK_FLUTE,
        SoundEvents.BLOCK_NOTE_BLOCK_BELL,
        SoundEvents.BLOCK_NOTE_BLOCK_CHIME,
        SoundEvents.BLOCK_NOTE_BLOCK_XYLOPHONE,
        SoundEvents.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE,
        SoundEvents.BLOCK_NOTE_BLOCK_COW_BELL,
        SoundEvents.BLOCK_NOTE_BLOCK_DIDGERIDOO,
        SoundEvents.BLOCK_NOTE_BLOCK_BIT
    };
    int previewId = -1;

    public NbsSearchMenu(String query) {
        this.query = query;
    }

    @Override
    public void open(String... args) throws CommandSyntaxException {
        MinecraftClient mc = CodeUtilities.MC;
        WPlainPanel root = new WPlainPanel();
        root.setSize(310, 250);

        WText queryField = new WText(new LiteralText("§l§nSearch Results for: " + query));
        root.add(queryField, 5, 5, 300, 0);

        WPlainPanel ppanel = new WPlainPanel();

        WText loading = new WText(new LiteralText("§lLoading Results..."));

        ppanel.add(loading, 85, 50, 300, 0);

        WLabeledSlider previewProgress = new WLabeledSlider(0, 1, Axis.HORIZONTAL);

        WScrollPanel spanel = new WScrollPanel(ppanel);

        CodeUtilities.EXECUTOR.submit(() -> {
            String sresults = CodeUtilitiesServer.requestURL(
                "https://untitled-57qvszfgg28u.runkit.sh/search?query=" + URLEncoder
                    .encode(query, StandardCharsets.UTF_8));

            JsonArray results = CodeUtilities.JSON_PARSER.parse(sresults).getAsJsonArray();

            mc.execute(() -> {
                ppanel.remove(loading);

                int y = 5;

                for (JsonElement resulte : results) {
                    JsonObject e = resulte.getAsJsonObject();

                    int id = e.get("id").getAsInt();
                    WText title = new WText(
                        new LiteralText(e.get("title").getAsString().replaceAll("\n", "")));
                    WText description = new WText(
                        new LiteralText(
                            ("§8by§r " + e.get("composer").getAsString()).replaceAll(
                                "\n", "")));

                    ppanel.add(title, 0, y, 999, 10);
                    ppanel.add(description, 0, y + 10, 999, 10);

                    WButton download = new WButton(new LiteralText("§l↓"));

                    ppanel.add(download, 270, y, 20, 20);

                    download.setOnClick(() -> {
                        download.setLabel(new LiteralText("..."));
                        CodeUtilities.EXECUTOR
                            .submit(() -> {
                                String notes = CodeUtilitiesServer.requestURL(
                                    "https://untitled-57qvszfgg28u.runkit.sh/download?format=mcnbs&id="
                                        + id);
                                String[] notearr = notes.split("=");
                                int length = Integer
                                    .parseInt(notearr[notearr.length - 1].split(":")[0]);
                                SongData d = new SongData("Song " + id, "CodeUtilities", 20f,
                                    length, notes, "", "", 1, 0, 0);

                                String code = new NBSToTemplate(d).convert();

                                ItemStack stack = new ItemStack(Items.NOTE_BLOCK);
                                TemplateUtils
                                    .compressTemplateNBT(stack, d.getName(), d.getAuthor(),
                                        code);

                                stack.setCustomName(
                                    new LiteralText("§d" + e.get("title").getAsString()));

                                ItemUtil.giveCreativeItem(stack, true);
                                download.setLabel(new LiteralText("§l↓"));
                            });
                    });

                    WButton preview = new WButton(new LiteralText("▶"));

                    ppanel.add(preview, 250, y, 20, 20);

                    preview.setOnClick(() -> {
                        if (previewId != id) {
                            previewId = id;

                            preview.setLabel(new LiteralText("..."));
                            CodeUtilities.EXECUTOR
                                .submit(() -> {
                                    String snotes = CodeUtilitiesServer.requestURL(
                                        "https://untitled-57qvszfgg28u.runkit.sh/download?format=mcnbs&id="
                                            + id);
                                    List<String> notes = new ArrayList<>(
                                        Arrays.asList(snotes.split("=")));

                                    preview.setLabel(new LiteralText("■"));

                                    //Start previewing
                                    int durnum = Integer.parseInt(
                                        notes.get(notes.size() - 1).split(":")[0]);
                                    previewProgress.setMaxValue(durnum);
                                    previewProgress.setValue(0);
                                    previewProgress.setHost(this);

                                    if (previewProgress.getParent() == null) {
                                        root.add(previewProgress, 5, 245, 300, 0);
                                        root.setSize(310, 270);

                                    }

                                    int[] index = {0};

                                    ScheduledExecutorService scheduler = Executors
                                        .newScheduledThreadPool(1);
                                    scheduler.scheduleAtFixedRate(() -> {
                                        try {
                                            previewProgress.setLabel(new LiteralText(
                                                "Tick: " + previewProgress.getValue() + " of "
                                                    + durnum));

                                            if (previewId != id
                                                || mc.currentScreen == null) {

                                                if (previewProgress.getParent() != null) {
                                                    //Stop Previewing anything
                                                    root.remove(previewProgress);
                                                    root.setSize(310, 250);
                                                    previewProgress.setParent(null);
                                                }

                                                scheduler.shutdown();
                                                preview.setLabel(new LiteralText("▶"));
                                                return;
                                            }
                                            if (index[0] < notes.size()) {
                                                while (
                                                    index[0] < notes.size() - 1 && Integer.parseInt(
                                                        notes.get(index[0]).split(":")[0])
                                                        < previewProgress.getValue()) {
                                                    index[0]++;
                                                }
                                                while (index[0] > 1 &&
                                                    Integer.parseInt(
                                                        notes.get(index[0]).split(":")[0])
                                                        > previewProgress.getValue()) {
                                                    index[0]--;
                                                }

                                                String line = notes.get(index[0])
                                                    .split(":")[1];

                                                if (notes.get(index[0])
                                                    .startsWith(previewProgress.getValue() + "")) {
                                                    for (String n : line.split(";")) {
                                                        String[] d = n.split(",");
                                                        int instrumentid = Integer
                                                            .parseInt(d[0]);
                                                        float pitch =
                                                            (float) Integer.parseInt(d[1])
                                                                / 1000;
                                                        float panning =
                                                            (float) Integer.parseInt(d[2]) / 100;
                                                        mc.player
                                                            .playSound(
                                                                instrumentids[instrumentid - 1],
                                                                panning, pitch);


                                                    }
                                                }

                                                index[0]++;
                                            }
                                            previewProgress.setValue(
                                                previewProgress.getValue() + 1);
                                            if (previewProgress.getValue() > durnum - 1) {
                                                previewId = -1;
                                            }
                                        } catch (Exception err) {
                                            err.printStackTrace();
                                        }
                                    }, 0, 1000 / 20, TimeUnit.MILLISECONDS);
                                });
                        } else {
                            previewId = -1;
                        }
                    });

                    y += 25;
                }

                spanel.layout();
            });
        });

        spanel.setScrollingHorizontally(TriState.FALSE);
        spanel.setScrollingVertically(TriState.TRUE);
        root.add(spanel, 5, 15, 300, 230);

        setRootPanel(root);
        root.validate(this);
    }
}
