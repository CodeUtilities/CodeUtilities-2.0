package io.github.codeutilities.menus.sys;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.util.RenderUtil;
import java.nio.FloatBuffer;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;

public class CTextField implements CWidget {

    final int x, y, width, height;
    final boolean editable;
    String text;
    Runnable changedListener;
    int cursorPos = 0;
    boolean hasSelection = false;
    int selectionPos = 0;
    public int textColor = 0xFFFFFFFF;
    int scroll = 0;

    public CTextField(String text, int x, int y, int width, int height, boolean editable) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.editable = editable;
    }


    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float tickDelta) {
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

        stack.pushPose();
        int selectionStart = Math.min(selectionPos, cursorPos);
        int selectionEnd = Math.max(selectionPos, cursorPos);

        for (String line : lines) {
            if (hasSelection) {
                int lineSelectionStart = Math.max(0, Math.min(selectionStart, line.length()));
                int lineSelectionEnd = Math.max(0, Math.min(selectionEnd, line.length()));

                stack.pushPose();

                stack.translate(f.width(line.substring(0, lineSelectionStart)), 0, 0);
                GuiComponent.fill(stack, 0, 0, f.width(line.substring(lineSelectionStart, lineSelectionEnd)), f.lineHeight, 0xFF5555FF);

                stack.popPose();
            }
            f.draw(stack, line, 0, 0, textColor);

            selectionStart -= line.length() + 1;
            selectionEnd -= line.length() + 1;

            stack.translate(0, f.lineHeight, 0);
        }
        stack.popPose();

        if (editable) {
            int cursorLine = getCursorLineIndex();
            int cursorLinePos = getIndexInCursorLine();

            int cursorX = f.width(getCursorLine().substring(0, cursorLinePos));
            int cursorY = f.lineHeight * cursorLine;

            f.draw(stack, "|", cursorX, cursorY, 0x99FFFFFF);
        }

        stack.popPose();
        RenderUtil.clearScissor();
    }

    @Override
    public void charTyped(char ch, int keyCode) {
        if (ch == '§') return;

        if (editable) {
            if (hasSelection) {
                int selectionStart = Math.min(selectionPos, cursorPos);
                int selectionEnd = Math.max(selectionPos, cursorPos);
                deleteSelection(selectionStart, selectionEnd);
                cursorPos = selectionStart;
            }
            text = text.substring(0, cursorPos) + ch + text.substring(cursorPos);
            cursorPos++;
            if (changedListener != null) {
                changedListener.run();
            }
        }
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (editable) {
            String lastText = text;
            Font f = CodeUtilities.MC.font;
            boolean createSelection = modifiers != 0;
            if (createSelection && !hasSelection) {
                hasSelection = true;
                selectionPos = cursorPos;
            }
            int selectionStart = Math.min(selectionPos, cursorPos);
            int selectionEnd = Math.max(selectionPos, cursorPos);

            switch (keyCode) {
                case 259: //backspace
                    if (hasSelection) {
                        deleteSelection(selectionStart, selectionEnd);
                    } else if (cursorPos > 0) {
                        text = text.substring(0, cursorPos - 1) + text.substring(cursorPos);
                        cursorPos--;
                    }
                    break;
                case 257: //enter
                    if (hasSelection) {
                        deleteSelection(selectionStart, selectionEnd);
                    }
                    text = text.substring(0, cursorPos) + "\n" + text.substring(cursorPos);
                    cursorPos++;
                    break;
                case 263: //left
                    if (cursorPos > 0) {
                        cursorPos--;
                    }
                    break;
                case 262: //right
                    if (cursorPos < text.length()) {
                        cursorPos++;
                    }
                    break;
                case 265: //up
                    if (getCursorLineIndex() > 0) {
                        int x = f.width(getCursorLine().substring(0, getIndexInCursorLine()));
                        int charPos = f.plainSubstrByWidth(getLine(getCursorLineIndex() - 1), x, true).length();
                        setCursor(getCursorLineIndex() - 1, charPos);
                    } else {
                        cursorPos = 0;
                    }
                    break;
                case 264: //down
                    if (getCursorLineIndex() < getLines().length - 1) {
                        int x = f.width(getCursorLine().substring(0, getIndexInCursorLine()));
                        int charPos = f.plainSubstrByWidth(getLine(getCursorLineIndex() + 1), x, true).length();
                        setCursor(getCursorLineIndex() + 1, charPos);
                    } else {
                        cursorPos = text.length();
                    }
                    break;
                case 261: //delete
                    if (hasSelection) {
                        deleteSelection(selectionStart, selectionEnd);
                    } else if (cursorPos < text.length()) {
                        text = text.substring(0, cursorPos) + text.substring(cursorPos + 1);
                    }
                    break;
                case 65: //a
                    if (modifiers == 2) {
                        selectionPos = 0;
                        cursorPos = text.length();
                        hasSelection = true;
                    }
                    break;
                case 67: //c
                    if (modifiers == 2) {
                        if (hasSelection) {
                            CodeUtilities.MC.keyboardHandler.setClipboard(text.substring(selectionStart, selectionEnd));
                        }
                    }
                    break;
                case 86: //v
                    if (modifiers == 2) {
                        if (hasSelection) {
                            deleteSelection(selectionStart, selectionEnd);
                        }
                        String clipboard = CodeUtilities.MC.keyboardHandler.getClipboard();
                        text = text.substring(0, cursorPos) + clipboard + text.substring(cursorPos);
                        cursorPos += clipboard.length();
                    }
                    break;
                case 88: //x
                    if (modifiers == 2) {
                        if (hasSelection) {
                            CodeUtilities.MC.keyboardHandler.setClipboard(text.substring(selectionStart, selectionEnd));
                            deleteSelection(selectionStart, selectionEnd);
                        }
                    }
            }

            if (selectionPos == cursorPos) {
                hasSelection = false;
            }

            if (!lastText.equals(text)) {
                if (changedListener != null) {
                    changedListener.run();
                }
            }
        }
    }

    public void deleteSelection(int selectionStart, int selectionEnd) {
        text = text.substring(0, selectionStart) + text.substring(selectionEnd);
        cursorPos = selectionStart;
        hasSelection = false;
    }

    private void setCursor(int line, int pos) {
        List<String> lines = List.of(getLines());
        for (int i = 0; i < lines.size(); i++) {
            if (i == line) {
                break;
            }
            pos += lines.get(i).length() + 1;
        }
        cursorPos = Math.max(0, Math.min(pos, text.length()));
    }


    @Override
    public void mouseClicked(double x, double y, int button) {
        if (editable) {
            if (button == 0) {
                Font f = CodeUtilities.MC.font;

                x -= 1 + this.x;
                y -= 1 + this.y+scroll;

                x *= 2;
                y *= 2;

                int line = (int) (y / f.lineHeight);
                int pixelX = (int) (x);
                line = Math.max(0, Math.min(line, getLines().length - 1));
                int lineIndex = f.plainSubstrByWidth(getLine(line), pixelX, true).length();
                setCursor(line, lineIndex);
            }
        }
    }

    @Override
    public void mouseScrolled(double mouseX, double mouseY, double amount) {
        if (editable) {
            scroll += amount * 5;
            Font f = CodeUtilities.MC.font;
            scroll = Math.min(0, Math.max(scroll, -(getLines().length+1) * f.lineHeight/2+height-2));
        }
    }

    public int getCursorLineIndex() {
        return text.substring(0, cursorPos).split("\n", -1).length - 1;
    }

    public String getCursorLine() {
        return getLines()[getCursorLineIndex()];
    }

    public int getIndexInCursorLine() {
        String[] lines = text.substring(0, cursorPos).split("\n", -1);
        return lines[lines.length - 1].length();
    }

    private String[] getLines() {
        return text.split("\n", -1);
    }

    public void setLine(int line, String text) {
        String[] lines = getLines();
        lines[line] = text;
        this.text = String.join("\n", lines);
    }

    private String getLine(int line) {
        return getLines()[line];
    }

    public void setChangedListener(Runnable r) {
        changedListener = r;
    }

    public String getText() {
        return text;
    }
}
