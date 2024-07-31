package com.iafenvoy.jupiter.malilib.gui;

import com.iafenvoy.jupiter.malilib.config.IConfigBase;
import com.iafenvoy.jupiter.malilib.gui.button.ButtonBase;
import com.iafenvoy.jupiter.malilib.gui.button.IButtonActionListener;
import com.iafenvoy.jupiter.malilib.gui.interfaces.IConfigGui;
import com.iafenvoy.jupiter.malilib.gui.interfaces.IMessageConsumer;
import com.iafenvoy.jupiter.malilib.gui.interfaces.ITextFieldListener;
import com.iafenvoy.jupiter.malilib.gui.widgets.WidgetBase;
import com.iafenvoy.jupiter.malilib.gui.widgets.WidgetLabel;
import com.iafenvoy.jupiter.malilib.gui.wrappers.TextFieldWrapper;
import com.iafenvoy.jupiter.malilib.interfaces.IStringConsumer;
import com.iafenvoy.jupiter.malilib.render.MessageRenderer;
import com.iafenvoy.jupiter.malilib.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class GuiBase extends Screen implements IMessageConsumer, IStringConsumer {
    public static final String TXT_AQUA = Formatting.AQUA.toString();
    public static final String TXT_BLACK = Formatting.BLACK.toString();
    public static final String TXT_BLUE = Formatting.BLUE.toString();
    public static final String TXT_GOLD = Formatting.GOLD.toString();
    public static final String TXT_GRAY = Formatting.GRAY.toString();
    public static final String TXT_GREEN = Formatting.GREEN.toString();
    public static final String TXT_RED = Formatting.RED.toString();
    public static final String TXT_WHITE = Formatting.WHITE.toString();
    public static final String TXT_YELLOW = Formatting.YELLOW.toString();

    public static final String TXT_BOLD = Formatting.BOLD.toString();
    public static final String TXT_ITALIC = Formatting.ITALIC.toString();
    public static final String TXT_RST = Formatting.RESET.toString();
    public static final String TXT_STRIKETHROUGH = Formatting.STRIKETHROUGH.toString();
    public static final String TXT_UNDERLINE = Formatting.UNDERLINE.toString();

    public static final String TXT_DARK_AQUA = Formatting.DARK_AQUA.toString();
    public static final String TXT_DARK_BLUE = Formatting.DARK_BLUE.toString();
    public static final String TXT_DARK_GRAY = Formatting.DARK_GRAY.toString();
    public static final String TXT_DARK_GREEN = Formatting.DARK_GREEN.toString();
    public static final String TXT_DARK_PURPLE = Formatting.DARK_PURPLE.toString();
    public static final String TXT_DARK_RED = Formatting.DARK_RED.toString();

    public static final String TXT_LIGHT_PURPLE = Formatting.LIGHT_PURPLE.toString();
    public static final int COLOR_WHITE = 0xFFFFFFFF;
    public static final int TOOLTIP_BACKGROUND = 0xB0000000;
    public static final int COLOR_HORIZONTAL_BAR = 0xFF999999;
    protected static final String BUTTON_LABEL_ADD = TXT_DARK_GREEN + "+" + TXT_RST;
    protected static final String BUTTON_LABEL_REMOVE = TXT_DARK_RED + "-" + TXT_RST;
    protected static final int LEFT = 20;
    protected static final int TOP = 10;
    public final MinecraftClient mc = MinecraftClient.getInstance();
    public final TextRenderer textRenderer = this.mc.textRenderer;
    public final int fontHeight = this.textRenderer.fontHeight;
    private final List<ButtonBase> buttons = new ArrayList<>();
    private final List<WidgetBase> widgets = new ArrayList<>();
    private final List<TextFieldWrapper<? extends GuiTextFieldGeneric>> textFields = new ArrayList<>();
    private final MessageRenderer messageRenderer = new MessageRenderer(0xDD000000, COLOR_HORIZONTAL_BAR);
    protected WidgetBase hoveredWidget = null;
    protected String title = "";
    protected boolean useTitleHierarchy = true;
    private long openTime;
    private int keyInputCount;
    @Nullable
    private Screen parent;

    protected GuiBase() {
        super(ScreenTexts.EMPTY);
    }

    public static boolean isMouseOver(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    public static void openGui(Screen gui) {
        MinecraftClient.getInstance().setScreen(gui);
    }

    public static boolean isShiftDown() {
        return hasShiftDown();
    }

    public static boolean isCtrlDown() {
        return hasControlDown();
    }

    public static boolean isAltDown() {
        return hasAltDown();
    }

    @Nullable
    public Screen getParent() {
        return this.parent;
    }

    public GuiBase setParent(@Nullable Screen parent) {
        // Don't allow nesting the GUI with itself...
        if (parent == null || parent.getClass() != this.getClass()) {
            this.parent = parent;
        }

        return this;
    }

    public String getTitleString() {
        return (this.useTitleHierarchy && this.parent instanceof GuiBase) ? (((GuiBase) this.parent).getTitleString() + " => " + this.title) : this.title;
    }

    @Override
    public Text getTitle() {
        return Text.of(this.getTitleString());
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean shouldPause() {
        return true;
    }

    @Override
    public void resize(MinecraftClient mc, int width, int height) {
        if (this.getParent() != null) {
            this.getParent().resize(mc, width, height);
        }

        super.resize(mc, width, height);
    }

    @Override
    public void init() {
        super.init();

        this.initGui();
        this.openTime = System.nanoTime();
    }

    public void initGui() {
        this.clearElements();
    }

    protected void closeGui(boolean showParent) {
        if (showParent) {
            this.mc.setScreen(this.parent);
        } else {
            this.close();
        }
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
        // Use a custom DrawContext that doesn't always disable depth test when drawing...
        drawContext = new MalilibDrawContext(this.client, drawContext.getVertexConsumers());

        this.drawScreenBackground(mouseX, mouseY);
        this.drawTitle(drawContext, mouseX, mouseY, partialTicks);

        // Draw base widgets
        this.drawWidgets(mouseX, mouseY, drawContext);
        this.drawTextFields(mouseX, mouseY, drawContext);
        this.drawButtons(mouseX, mouseY, partialTicks, drawContext);

        this.drawContents(drawContext, mouseX, mouseY, partialTicks);

        this.drawButtonHoverTexts(mouseX, mouseY, partialTicks, drawContext);
        this.drawHoveredWidget(mouseX, mouseY, drawContext);
        this.drawGuiMessages(drawContext);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (!this.onMouseClicked((int) mouseX, (int) mouseY, mouseButton)) {
            return super.mouseClicked(mouseX, mouseY, mouseButton);
        }

        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        if (!this.onMouseReleased((int) mouseX, (int) mouseY, mouseButton)) {
            return super.mouseReleased(mouseX, mouseY, mouseButton);
        }

        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.keyInputCount++;

        if (this.onKeyTyped(keyCode, scanCode, modifiers)) {
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char charIn, int modifiers) {
        // This is an ugly fix for the issue that the key press from the hotkey that
        // opens a GUI would then also get into any text fields or search bars, as the
        // charTyped() event always fires after the keyPressed() event in any case >_>
        // The 100ms timeout is to not indefinitely block the first character,
        // as otherwise IME methods wouldn't work at all, as they don't trigger a key press.
        if (this.keyInputCount <= 0 && System.nanoTime() - this.openTime <= 100000000) {
            this.keyInputCount++;
            return true;
        }

        if (this.onCharTyped(charIn, modifiers)) {
            return true;
        }

        return super.charTyped(charIn, modifiers);
    }

    public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (ButtonBase button : this.buttons) {
            if (button.onMouseClicked(mouseX, mouseY, mouseButton)) {
                // Don't call super if the button press got handled
                return true;
            }
        }

        boolean handled = false;

        for (TextFieldWrapper<?> entry : this.textFields) {
            if (entry.mouseClicked(mouseX, mouseY, mouseButton)) {
                // Don't call super if the button press got handled
                handled = true;
            }
        }

        if (!handled) {
            for (WidgetBase widget : this.widgets) {
                if (widget.isMouseOver(mouseX, mouseY) && widget.onMouseClicked(mouseX, mouseY, mouseButton)) {
                    // Don't call super if the button press got handled
                    handled = true;
                    break;
                }
            }
        }

        return handled;
    }

    public boolean onMouseReleased(int mouseX, int mouseY, int mouseButton) {
        for (WidgetBase widget : this.widgets) {
            widget.onMouseReleased(mouseX, mouseY, mouseButton);
        }

        return false;
    }

    public boolean onMouseScrolled(int mouseX, int mouseY, double horizontalAmount, double verticalAmount) {
        for (ButtonBase button : this.buttons) {
            if (button.onMouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)) {
                // Don't call super if the button press got handled
                return true;
            }
        }

        for (WidgetBase widget : this.widgets) {
            if (widget.onMouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)) {
                // Don't call super if the action got handled
                return true;
            }
        }

        return false;
    }

    public boolean onKeyTyped(int keyCode, int scanCode, int modifiers) {
        boolean handled = false;
        int selected = -1;

        for (int i = 0; i < this.textFields.size(); ++i) {
            TextFieldWrapper<?> entry = this.textFields.get(i);

            if (entry.isFocused()) {
                if (keyCode == GLFW.GLFW_KEY_TAB) {
                    entry.setFocused(false);
                    selected = i;
                } else {
                    entry.onKeyTyped(keyCode, scanCode, modifiers);
                }

                handled = keyCode != GLFW.GLFW_KEY_ESCAPE;
                break;
            }
        }

        if (!handled) {
            for (WidgetBase widget : this.widgets) {
                if (widget.onKeyTyped(keyCode, scanCode, modifiers)) {
                    // Don't call super if the button press got handled
                    handled = true;
                    break;
                }
            }
        }

        if (!handled) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                this.closeGui(!isShiftDown());

                return true;
            }
        }

        if (selected >= 0) {
            if (isShiftDown()) {
                selected = selected > 0 ? selected - 1 : this.textFields.size() - 1;
            } else {
                selected = (selected + 1) % this.textFields.size();
            }

            this.textFields.get(selected).setFocused(true);
        }

        return handled;
    }

    public boolean onCharTyped(char charIn, int modifiers) {
        boolean handled = false;

        for (TextFieldWrapper<?> entry : this.textFields) {
            if (entry.onCharTyped(charIn, modifiers)) {
                handled = true;
                break;
            }
        }

        if (!handled) {
            for (WidgetBase widget : this.widgets) {
                if (widget.onCharTyped(charIn, modifiers)) {
                    // Don't call super if the button press got handled
                    handled = true;
                    break;
                }
            }
        }

        return handled;
    }

    @Override
    public void setString(String string) {
        this.messageRenderer.addMessage(3000, string);
    }

    @Override
    public void addMessage(Message.MessageType type, String messageKey, Object... args) {
        this.addGuiMessage(type, 5000, messageKey, args);
    }

    @Override
    public void addMessage(Message.MessageType type, int lifeTime, String messageKey, Object... args) {
        this.addGuiMessage(type, lifeTime, messageKey, args);
    }

    public void addGuiMessage(Message.MessageType type, int displayTimeMs, String messageKey, Object... args) {
        this.messageRenderer.addMessage(type, displayTimeMs, messageKey, args);
    }

    public void setNextMessageType(Message.MessageType type) {
        this.messageRenderer.setNextMessageType(type);
    }

    protected void drawGuiMessages(DrawContext drawContext) {
        this.messageRenderer.drawMessages(this.width / 2, this.height / 2, drawContext);
    }

    public void bindTexture(Identifier texture) {
        RenderUtils.bindTexture(texture);
    }

    public <T extends ButtonBase> T addButton(T button, IButtonActionListener listener) {
        button.setActionListener(listener);
        this.buttons.add(button);
        return button;
    }

    public <T extends GuiTextFieldGeneric> TextFieldWrapper<T> addTextField(T textField, @Nullable ITextFieldListener<T> listener) {
        TextFieldWrapper<T> wrapper = new TextFieldWrapper<>(textField, listener);
        this.textFields.add(wrapper);
        return wrapper;
    }

    public <T extends WidgetBase> T addWidget(T widget) {
        this.widgets.add(widget);
        return widget;
    }

    public WidgetLabel addLabel(int x, int y, int width, int height, int textColor, String... lines) {
        return this.addLabel(x, y, width, height, textColor, Arrays.asList(lines));
    }

    public WidgetLabel addLabel(int x, int y, int width, int height, int textColor, List<String> lines) {
        if (!lines.isEmpty()) {
            if (width == -1) {
                for (String line : lines) {
                    width = Math.max(width, this.getStringWidth(line));
                }
            }
        }

        return this.addWidget(new WidgetLabel(x, y, width, height, textColor, lines));
    }

    protected boolean removeWidget(WidgetBase widget) {
        if (widget != null && this.widgets.contains(widget)) {
            this.widgets.remove(widget);
            return true;
        }

        return false;
    }

    protected void clearElements() {
        this.clearWidgets();
        this.clearButtons();
        this.clearTextFields();
    }

    protected void clearWidgets() {
        this.widgets.clear();
    }

    protected void clearButtons() {
        this.buttons.clear();
    }

    protected void clearTextFields() {
        this.textFields.clear();
    }

    protected void drawScreenBackground(int mouseX, int mouseY) {
        // Draw the dark background
        RenderUtils.drawRect(0, 0, this.width, this.height, TOOLTIP_BACKGROUND);
    }

    protected void drawTitle(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
        this.drawString(drawContext, this.getTitleString(), LEFT, TOP, COLOR_WHITE);
    }

    protected void drawContents(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
    }

    protected void drawButtons(int mouseX, int mouseY, float partialTicks, DrawContext drawContext) {
        for (ButtonBase button : this.buttons) {
            button.render(mouseX, mouseY, button.isMouseOver(), drawContext);
        }
    }

    protected void drawTextFields(int mouseX, int mouseY, DrawContext drawContext) {
        for (TextFieldWrapper<?> entry : this.textFields) {
            entry.draw(mouseX, mouseY, drawContext);
        }
    }

    protected void drawWidgets(int mouseX, int mouseY, DrawContext drawContext) {
        this.hoveredWidget = null;

        if (!this.widgets.isEmpty()) {
            for (WidgetBase widget : this.widgets) {
                widget.render(mouseX, mouseY, false, drawContext);

                if (widget.isMouseOver(mouseX, mouseY)) {
                    this.hoveredWidget = widget;
                }
            }
        }
    }

    protected void drawButtonHoverTexts(int mouseX, int mouseY, float partialTicks, DrawContext drawContext) {
        if (!this.shouldRenderHoverStuff()) {
            return;
        }

        for (ButtonBase button : this.buttons) {
            if (button.hasHoverText() && button.isMouseOver() && !button.getHoverStrings().isEmpty()) {
                RenderUtils.drawHoverText(mouseX, mouseY, button.getHoverStrings(), drawContext);
            }
        }
    }

    protected boolean shouldRenderHoverStuff() {
        return this.mc.currentScreen == this;
    }

    protected void drawHoveredWidget(int mouseX, int mouseY, DrawContext drawContext) {
        if (!this.shouldRenderHoverStuff()) {
            return;
        }

        if (this.hoveredWidget != null) {
            this.hoveredWidget.postRenderHovered(mouseX, mouseY, false, drawContext);
        }
    }

    public int getStringWidth(String text) {
        return this.textRenderer.getWidth(text);
    }

    public void drawString(DrawContext drawContext, String text, int x, int y, int color) {
        drawContext.drawText(this.textRenderer, text, x, y, color, false);
    }

    public void drawStringWithShadow(DrawContext drawContext, String text, int x, int y, int color) {
        drawContext.drawTextWithShadow(this.textRenderer, text, x, y, color);
    }

    public int getMaxPrettyNameLength(List<? extends IConfigBase> configs) {
        int width = 0;

        for (IConfigBase config : configs) {
            width = Math.max(width, this.getStringWidth(config.getConfigGuiDisplayName()));
        }

        return width;
    }
}
