package io.github.codeutilities.sys.renderer.widgets;

import io.github.codeutilities.mod.features.scripting.engine.ScriptAction;
import io.github.codeutilities.mod.features.scripting.engine.ScriptAction.Category;
import io.github.codeutilities.mod.features.scripting.engine.ScriptContext;
import io.github.codeutilities.mod.features.scripting.engine.ScriptEvent;
import io.github.codeutilities.mod.features.scripting.engine.ScriptParser;
import io.github.codeutilities.mod.features.scripting.engine.ScriptParserException;
import io.github.codeutilities.mod.features.scripting.engine.ScriptPart;
import io.github.cottonmc.cotton.gui.client.Scissors;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

public class CCodeTextField extends WTextField {

    private TextRenderer font;
    private int scroll = 3;
    private int errorLine = -1;
    private final List<ScriptPart> autoCompletions = new ArrayList<>();

    public CCodeTextField(WButton saveBtn) {
        setChangedListener((text) -> {
            try {
                ScriptParser.parse(text, new ScriptContext());
                errorLine = -1;
                if (saveBtn != null) {
                    saveBtn.setEnabled(true);
                }
            } catch (ScriptParserException err) {
                errorLine = err.line - 1;
                if (saveBtn != null) {
                    saveBtn.setEnabled(false);
                }
            }

            getCompletions();
        });
    }

    private void getCompletions() {
        autoCompletions.clear();

        int cursorline = 0;
        int cursorleft = getCursor();
        int cursorpos = 0;
        for (char c : getText().toCharArray()) {
            if (--cursorleft < 0) {
                break;
            }
            if (c == '\n') {
                cursorline++;
                cursorpos = 0;
            } else {
                cursorpos++;
            }
        }

        String line = getText().split("\n", -1)[cursorline];
        line = line.substring(0, Math.min(cursorpos, line.length())).toLowerCase().trim();

        for (ScriptAction action : ScriptAction.values()) {
            if (action.category == Category.SPECIAL) continue;
            if ((action.category.label.toLowerCase() + "#" + action.name.toLowerCase()).startsWith(line)) {
                autoCompletions.add(action);
            }
        }
        for (ScriptEvent event : ScriptEvent.values()) {
            if (("event#" + event.codeName.toLowerCase()).startsWith(line)) {
                autoCompletions.add(event);
            }
        }
    }

    @Override
    public void setSize(int x, int y) {
        width = x;
        height = y;
    }

    @Override
    protected void renderTextField(MatrixStack matrices, int x, int y) {
        if (this.font == null) {
            this.font = MinecraftClient.getInstance().textRenderer;
        }

        int borderColor = (this.isFocused()) ? 0xFF_FFFFA0 : 0xFF_A0A0A0;
        ScreenDrawing.coloredRect(matrices, x - 1, y - 1, width + 2, height + 2, borderColor);
        ScreenDrawing.coloredRect(matrices, x, y, width, height, 0xFF000000);

        Scissors.push(x, y, width, height);

        matrices.push();
        matrices.translate(0, scroll, 0);

        String[] lines = getText().split("\n", -1);

        int yoffset = 0;
        for (String line : lines) {
            if (yoffset == errorLine) {
                line = "§c" + line;
            }
            font.draw(matrices, line, 15 + x, y + yoffset * font.fontHeight, 0xffffffff);
            font.draw(matrices, "§e" + (yoffset + 1) + ".", 13 + x - font.getWidth((yoffset + 1) + "."), y + yoffset * font.fontHeight, 0xffffffff);
            yoffset++;
        }

        int cursorline = 0;
        int cursorpos = 0;
        if (isFocused()) {
            int cursorleft = getCursor();
            for (char c : getText().toCharArray()) {
                if (--cursorleft < 0) {
                    break;
                }
                if (c == '\n') {
                    cursorline++;
                    cursorpos = 0;
                } else {
                    cursorpos += font.getWidth(c + "");
                }
            }
            ScreenDrawing.coloredRect(matrices, x + cursorpos + 15, y + cursorline * font.fontHeight, 1, font.fontHeight, 0xFFD0D0D0);
        }
        Scissors.pop();
        if (isFocused()) {
            int line = cursorline + 1;
            for (ScriptPart autoCompletion : autoCompletions) {
                String text = "";
                if (autoCompletion instanceof ScriptEvent evn) {
                    text = "Event#" + evn.codeName + " - " + evn.description;
                } else if (autoCompletion instanceof ScriptAction act) {
                    text = act.category.label + "#" + act.name + " - " + act.description;
                }
                ScreenDrawing.coloredRect(matrices, x + cursorpos + 15, y + line * font.fontHeight, font.getWidth(text), font.fontHeight, 0xdd000000);
                font.draw(matrices, text, x + cursorpos + 15, y + line * font.fontHeight, 0xff73fdff);
                line++;
            }
        }
        matrices.pop();
    }

    @Override
    public InputResult onClick(int x, int y, int button) {
        requestFocus();
        x -= 15;
        y -= scroll;
        if (x > 0 && y > 0) {
            int line = y / font.fontHeight;
            String[] text = getText().split("\n");
            if (line >= text.length) {
                line = text.length - 1;
            }
            text[line] = font.trimToWidth(text[line], x);

            int newcursor = -1;
            for (int i = 0; i <= line; i++) {
                newcursor += text[i].length() + 1;
            }
            setCursorPos(newcursor);
        }
        getCompletions();
        return InputResult.PROCESSED;
    }

    @Override
    public void onKeyPressed(int ch, int key, int modifiers) {
        if (!isEditable()) {
            return;
        }
        if (ch == GLFW.GLFW_KEY_ENTER) {
            String before = getText().substring(0, getCursor());
            String after = getText().substring(getCursor());
            setText(before + "\n" + after);
            setCursorPos(getCursor() + 1);
            scroll -= font.fontHeight;
        } else if (ch == GLFW.GLFW_KEY_DOWN || ch == GLFW.GLFW_KEY_UP) {
            int cursorline = 0;
            int cursorpos = 0;
            int cursorleft = getCursor();
            for (char c : getText().toCharArray()) {
                if (--cursorleft < 0) {
                    break;
                }
                if (c == '\n') {
                    cursorline++;
                    cursorpos = 0;
                } else {
                    cursorpos += font.getWidth(c + "");
                }
            }
            if (ch == GLFW.GLFW_KEY_UP) {
                cursorline--;
            } else if (ch == GLFW.GLFW_KEY_DOWN) {
                cursorline++;
            }
            if (cursorline == -1) {
                setCursorPos(0);
                return;
            }
            int newcursor = 0;
            int linewidth = 0;
            for (char c : getText().toCharArray()) {
                if (c == '\n') {
                    if (cursorline == 0) {
                        setCursorPos(newcursor);
                        return;
                    }
                    cursorline--;
                } else if (cursorline == 0) {
                    linewidth += font.getWidth(c + "");
                    if (linewidth > cursorpos) {
                        setCursorPos(newcursor);
                        return;
                    }
                }
                newcursor++;
            }
            setCursorPos(newcursor);
        } else if (ch == GLFW.GLFW_KEY_BACKSPACE || ch == GLFW.GLFW_KEY_DELETE) {
            if (getCursor() != 0) {
                char c = getText().charAt(getCursor() - 1);
                if (c == '\n') {
                    scroll += font.fontHeight;
                }
            }
        }
        if (scroll < -getText().split("\n").length * font.fontHeight + height) {
            scroll = -getText().split("\n").length * font.fontHeight + height;
        }
        if (scroll > 3) {
            scroll = 3;
        }
        super.onKeyPressed(ch, key, modifiers);
        getCompletions();
    }

    @Override
    public InputResult onMouseScroll(int x, int y, double amount) {
        scroll += y * amount / 5;
        if (scroll < -getText().split("\n").length * font.fontHeight + height) {
            scroll = -getText().split("\n").length * font.fontHeight + height;
        }
        if (scroll > 3) {
            scroll = 3;
        }
        return super.onMouseScroll(x, y, amount);
    }

}
