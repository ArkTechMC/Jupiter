package fi.dy.masa.malilib.gui;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.config.IConfigColorList;
import fi.dy.masa.malilib.gui.interfaces.IConfigGui;
import fi.dy.masa.malilib.gui.interfaces.IDialogHandler;
import fi.dy.masa.malilib.gui.widgets.WidgetColorListEdit;
import fi.dy.masa.malilib.gui.widgets.WidgetColorListEditEntry;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.Color4f;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.KeyCodes;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;

public class GuiColorListEdit extends GuiListBase<Color4f, WidgetColorListEditEntry, WidgetColorListEdit> {
    protected final IConfigColorList config;
    protected final IConfigGui configGui;
    @Nullable
    protected final IDialogHandler dialogHandler;
    protected int dialogWidth;
    protected int dialogHeight;
    protected int dialogLeft;
    protected int dialogTop;
    protected int labelWidth;
    protected int textFieldWidth;

    public GuiColorListEdit(IConfigColorList config, IConfigGui configGui, @Nullable IDialogHandler dialogHandler, Screen parent) {
        super(0, 0);

        this.config = config;
        this.configGui = configGui;
        this.dialogHandler = dialogHandler;
        this.title = StringUtils.translate("malilib.gui.title.color_list_edit", config.getName());

        // When we have a dialog handler, then we are inside the Liteloader config menu.
        // In there we don't want to use the normal "GUI replacement and render parent first" trick.
        // The "dialog handler" stuff is used within the Liteloader config menus,
        // because there we can't change the mc.currentScreen reference to this GUI,
        // because otherwise Liteloader will freak out.
        // So instead we are using a weird wrapper "sub panel" thingy in there, and thus
        // we can NOT try to render the parent GUI here in that case, otherwise it will
        // lead to an infinite recursion loop and a StackOverflowError.
        if (this.dialogHandler == null) {
            this.setParent(parent);
        }
    }

    protected void setWidthAndHeight() {
        this.dialogWidth = 400;
        this.dialogHeight = GuiUtils.getScaledWindowHeight() - 90;
    }

    protected void centerOnScreen() {
        if (this.getParent() != null) {
            this.dialogLeft = this.getParent().width / 2 - this.dialogWidth / 2;
            this.dialogTop = this.getParent().height / 2 - this.dialogHeight / 2;
        } else {
            this.dialogLeft = 20;
            this.dialogTop = 20;
        }
    }

    @Override
    public void initGui() {
        this.setWidthAndHeight();
        this.centerOnScreen();
        this.reCreateListWidget();

        super.initGui();
    }

    public IConfigColorList getConfig() {
        return this.config;
    }

    @Override
    protected int getBrowserWidth() {
        return this.dialogWidth - 14;
    }

    @Override
    protected int getBrowserHeight() {
        return this.dialogHeight - 30;
    }

    @Override
    protected WidgetColorListEdit createListWidget(int listX, int listY) {
        // The listX and listY are set via the constructor, which in this dialog-like GUI's case is too early to know them
        return new WidgetColorListEdit(this.dialogLeft + 10, this.dialogTop + 20, this.getBrowserWidth(), this.getBrowserHeight(), this.dialogWidth - 100, this);
    }

    @Override
    public void removed() {
        if (this.getListWidget().wereConfigsModified()) {
            this.getListWidget().applyPendingModifications();
            ConfigManager.getInstance().onConfigsChanged(this.configGui.getModId());
        }

        super.removed();
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
        if (this.getParent() != null) {
            this.getParent().render(drawContext, mouseX, mouseY, partialTicks);
        }

        super.render(drawContext, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void drawScreenBackground(int mouseX, int mouseY) {
        RenderUtils.drawOutlinedBox(this.dialogLeft, this.dialogTop, this.dialogWidth, this.dialogHeight, 0xFF000000, COLOR_HORIZONTAL_BAR);
    }

    @Override
    protected void drawTitle(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
        this.drawStringWithShadow(drawContext, this.title, this.dialogLeft + 10, this.dialogTop + 6, COLOR_WHITE);
    }

    @Override
    public boolean onKeyTyped(int keyCode, int scanCode, int modifiers) {
        if (keyCode == KeyCodes.KEY_ESCAPE && this.dialogHandler != null) {
            this.dialogHandler.closeDialog();
            return true;
        } else {
            return super.onKeyTyped(keyCode, scanCode, modifiers);
        }
    }
}
