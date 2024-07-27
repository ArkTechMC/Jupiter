package com.iafenvoy.jupiter.malilib.gui.widgets;

import com.iafenvoy.jupiter.malilib.util.StringUtils;
import com.iafenvoy.jupiter.malilib.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WidgetHoverInfo extends WidgetBase {
    protected final List<String> lines = new ArrayList<>();

    public WidgetHoverInfo(int x, int y, int width, int height, String key, Object... args) {
        super(x, y, width, height);

        this.setInfoLines(key, args);
    }

    protected void setInfoLines(String key, Object... args) {
        String[] split = StringUtils.translate(key, args).split("\\n");

        Collections.addAll(this.lines, split);
    }

    /**
     * Adds the provided lines to the list.
     * The strings will be split into separate lines from any "\n" sequences.
     *
     * @param lines
     */
    public void addLines(String... lines) {
        for (String line : lines) {
            line = StringUtils.translate(line);
            String[] split = line.split("\\n");

            Collections.addAll(this.lines, split);
        }
    }

    public List<String> getLines() {
        return this.lines;
    }

    @Override
    public void render(int mouseX, int mouseY, boolean selected, DrawContext drawContext) {
    }

    @Override
    public void postRenderHovered(int mouseX, int mouseY, boolean selected, DrawContext drawContext) {
        if (!this.lines.isEmpty())
            RenderUtils.drawHoverText(mouseX, mouseY, this.lines, drawContext);
    }
}
