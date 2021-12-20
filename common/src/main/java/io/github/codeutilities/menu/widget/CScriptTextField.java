package io.github.codeutilities.menu.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.scripts.action.ScriptActionCategory;
import io.github.codeutilities.scripts.action.ScriptActionType;
import io.github.codeutilities.scripts.event.ScriptEventType;
import io.github.codeutilities.util.RenderUtil;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;

public class CScriptTextField extends CTextField {

    String tabCompletion = "";
    boolean showCompletions = false;

    public CScriptTextField(String text, int x, int y, int width, int height, boolean editable) {
        super(text, x, y, width, height, editable);
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float tickDelta) {
        String oldText = text;
        Pattern eventP = Pattern.compile("^( *)Event\\.(\\w+):$");
        Pattern actionP = Pattern.compile("^( *)(\\w+)\\.(\\w+)\\((.*)\\)( ?\\{?)$");

        if (textColor == 0xFFFFFFFF) {
            text = Arrays.stream(text.split("\n")).map(s -> {
                if (s.trim().startsWith("#")) {
                    return "§7" + s;
                } else {
                    Matcher m = eventP.matcher(s);

                    if (m.find()) {
                        return m.group(1) + "§fEvent§b.§f" + m.group(2) + ":";
                    } else {
                        m = actionP.matcher(s);
                        if (m.find()) {
                            return m.group(1) + "§f" + m.group(2) + "§b.§f" + m.group(3) + "§6(§e" + m.group(4) + "§6)§e" + m.group(5);
                        } else {
                            return "§e" + s;
                        }
                    }
                }
            }).collect(Collectors.joining("\n"));

        }

        stack.pushPose();
        stack.translate(x, y, 0);

        GuiComponent.fill(stack, 0, 0, width, height, 0xFF888888);
        GuiComponent.fill(stack, 1, 1, width - 1, height - 1, 0xFF000000);

        FloatBuffer buff = FloatBuffer.allocate(16);

        stack.last().pose().store(buff);

        RenderUtil.setScissor(
            (int) ((x+buff.get(12))*buff.get(0)),
            (int) ((y+buff.get(13))*buff.get(5)),
            (int) ((width-2)*buff.get(0)*2),
            (int) ((height-2)*buff.get(5)*2)
        );

        stack.translate(2, 2+scroll, 0);
        stack.scale(0.5f, 0.5f, 0);

        Font f = CodeUtilities.MC.font;
        String[] lines = text.split("\n");
        String[] oldLines = oldText.split("\n");

            stack.pushPose();
        int selectionStart = Math.min(selectionPos, cursorPos);
        int selectionEnd = Math.max(selectionPos, cursorPos);

        int index = 0;
        for (String line : lines) {
            String oldLine = oldLines[index];
            if (hasSelection) {
                int lineSelectionStart = Math.max(0, Math.min(selectionStart, oldLine.length()));
                int lineSelectionEnd = Math.max(0, Math.min(selectionEnd, oldLine.length()));

                stack.pushPose();

                stack.translate(f.width(oldLine.substring(0, lineSelectionStart)), 0, 0);
                GuiComponent.fill(stack, 0, 0, f.width(oldLine.substring(lineSelectionStart, lineSelectionEnd)), f.lineHeight, 0xFF5555FF);

                stack.popPose();
            }
            f.draw(stack, line, 0, 0, textColor);

            selectionStart -= oldLine.length() + 1;
            selectionEnd -= oldLine.length() + 1;

            stack.translate(0, f.lineHeight, 0);
            index++;
        }
        stack.popPose();

        text = oldText;

        if (editable) {
            int cursorLine = getCursorLineIndex();
            int cursorLinePos = getIndexInCursorLine();

            int cursorX = f.width(getCursorLine().substring(0, cursorLinePos));
            int cursorY = f.lineHeight * cursorLine;

            f.draw(stack, "|", cursorX, cursorY, 0x99FFFFFF);
        }

        stack.popPose();
        RenderUtil.clearScissor();

        int cursorLine = getCursorLineIndex();
        int cursorLinePos = getIndexInCursorLine();

        String lineText = getCursorLine().substring(0, cursorLinePos);

        int completionsX = f.width(lineText);
        int completionsY = f.lineHeight * (cursorLine+1);

        lineText = lineText.trim();

        tabCompletion = "";
        List<String> suggestions = new ArrayList<>();
        if (lineText.contains(".")) {
            String text = lineText.split("\\.", -1)[0].trim();
            completionsX -= f.width(text);

            ScriptActionCategory cat = ScriptActionCategory.get(text);
            if (cat != null || text.equals("Event")) {
                String t2 = lineText.substring(lineText.indexOf(".") + 1).trim().toLowerCase();
                if (cat != null) {
                    for (ScriptActionType t : ScriptActionType.values()) {
                        if (t.getCategory() == cat) {
                            if (t.getName().toLowerCase().startsWith(t2)) {
                                suggestions.add(cat.getName() + "." + t.getName() + "(" + t.getArguments() + ")");
                                if (tabCompletion.isEmpty()) {
                                    tabCompletion = cat.getName() + "." + t.getName()+"()";
                                }
                            }
                        }
                    }
                } else {
                    for (ScriptEventType t : ScriptEventType.values()) {
                        if (t.getName().toLowerCase().startsWith(t2)) {
                            suggestions.add("Event." + t.getName());
                            if (tabCompletion.isEmpty()) {
                                tabCompletion = "Event." + t.getName() + ":";
                            }
                        }
                    }
                }
            }
        } else {
            String text = lineText.toLowerCase();
            completionsX -= f.width(text);
            if ("event".startsWith(text)) suggestions.add("Event");
            for (ScriptActionCategory cat : ScriptActionCategory.values()) {
                if (cat.getName().toLowerCase().startsWith(text)) suggestions.add(cat.getName());
            }
            if (!suggestions.isEmpty()) {
                tabCompletion = suggestions.get(0)+".";
            }
        }

        if (suggestions.size() > 0 && showCompletions) {
            if (suggestions.size() > 5) {
                suggestions = suggestions.subList(0, 5);
            }
            int maxWidth = -1;
            for (String s : suggestions) {
                int width = f.width(s);
                if (width > maxWidth) maxWidth = width;
            }
            stack.pushPose();
            stack.translate(x+2, y+2+scroll, 0);
            stack.scale(0.5f, 0.5f, 0);
            stack.translate(completionsX,completionsY,0);
            GuiComponent.fill(stack, 0,0, maxWidth, f.lineHeight * suggestions.size(), 0xcc000000);

            for (String suggestion : suggestions) {
                f.draw(stack, suggestion, 0, 0, 0xFFdddddd);
                stack.translate(0, f.lineHeight, 0);
            }

            stack.popPose();
        }

        if (editable) {
            String name = "", description = "";
            Matcher m = eventP.matcher(getCursorLine());
            if (m.find()) {
                String mName = m.group(2);
                for (ScriptEventType t : ScriptEventType.values()) {
                    if (t.getName().equals(mName)) {
                        name = t.getName();
                        description = t.getDescription();
                    }
                }
            } else {
                m = actionP.matcher(getCursorLine());
                if (m.find()) {
                    String type = m.group(2);
                    String mName = m.group(3);

                    ScriptActionCategory cat = ScriptActionCategory.get(type);
                    if (cat != null) {
                        ScriptActionType t = ScriptActionType.get(cat, mName);
                        if (t != null) {
                            name = t.getName() + "(" + t.getArguments() + ")";
                            description = t.getDescription();
                        }
                    }
                }
            }
            if (!name.isEmpty()) {
                stack.pushPose();
                stack.translate(x+2+width+5,y+5,0);
                stack.scale(0.5f, 0.5f, 0);

                f.draw(stack, name, 0, 0, 0xFFFFFF55);
                stack.translate(0, f.lineHeight, 0);

                while (!description.isEmpty()) {
                    String line = f.plainSubstrByWidth(description, 100);
                    line = Arrays.stream(description.split(" ")).limit(line.split(" ").length).collect(Collectors.joining(" "));
                    description = description.substring(line.length());
                    f.draw(stack, line.trim(), 0, 0, 0xFFdddddd);
                    stack.translate(0, f.lineHeight, 0);
                }
            }
        }
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (editable) {
            showCompletions = true;
            if (keyCode == 258) { //tab
                if (!tabCompletion.isEmpty()) {
                    Pattern spaceP = Pattern.compile("^\\s+");
                    Matcher m = spaceP.matcher(getCursorLine());

                    int spaces = 0;
                    if (m.find()) {
                        spaces = m.group().length();
                    }

                    setLine(getCursorLineIndex(), " ".repeat(spaces) + tabCompletion + getCursorLine().substring(getIndexInCursorLine()));
                    cursorPos += tabCompletion.length() - getCursorLine().substring(0, getIndexInCursorLine()).length() + spaces;
                    if (tabCompletion.endsWith(")")) {
                        cursorPos--;
                    }
                }
                if (changedListener != null) {
                    changedListener.run();
                }
                return;
            }
            if (keyCode == 257) { //enter
                if (hasSelection) {
                    int selectionStart = Math.min(selectionPos, cursorPos);
                    int selectionEnd = Math.max(selectionPos, cursorPos);

                    deleteSelection(selectionStart, selectionEnd);
                }

                Pattern spaceP = Pattern.compile("^\\s+");
                Matcher m = spaceP.matcher(getCursorLine());

                int spaces = 0;
                if (m.find()) {
                    spaces = m.group().length();
                }

                text = text.substring(0, cursorPos) + "\n" + " ".repeat(spaces) + text.substring(cursorPos);
                cursorPos += spaces + 1;

                if (changedListener != null) {
                    changedListener.run();
                }
                return;
            }
        }
        super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void mouseClicked(double x, double y, int button) {
        showCompletions = false;
        super.mouseClicked(x, y, button);
    }
}
