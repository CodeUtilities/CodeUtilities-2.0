package io.github.codeutilities.sys.renderer.widgets;

import io.github.cottonmc.cotton.gui.client.Scissors;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

public class CMultilineTextField extends WTextField {

    private TextRenderer font;
    private int scroll = 3;

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

        String[] lines = getText().split("\n");

        int yoffset = 0;
        for (String line : lines) {
            font.draw(matrices, line, 3 + x, y + yoffset * font.fontHeight, 0xffffffff);
            yoffset++;
        }

        if (isFocused()) {
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
            ScreenDrawing.coloredRect(matrices, x + cursorpos + 3, y + cursorline * font.fontHeight, 1, font.fontHeight, 0xFFD0D0D0);
        }
        Scissors.pop();
        matrices.pop();
    }

    @Override
    public InputResult onClick(int x, int y, int button) {
        requestFocus();
        x -= 3;
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
                char c = getText().charAt(getCursor()-1);
                if (c == '\n') scroll += font.fontHeight;
            }
        }
        if (scroll < -getText().split("\n").length * font.fontHeight + height) {
            scroll = -getText().split("\n").length * font.fontHeight + height;
        }
        if (scroll > 3) {
            scroll = 3;
        }
        super.onKeyPressed(ch, key, modifiers);
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
