package com.iafenvoy.jupiter.malilib.gui.button;

import com.iafenvoy.jupiter.malilib.config.IConfigColorList;
import com.iafenvoy.jupiter.malilib.gui.GuiBase;
import com.iafenvoy.jupiter.malilib.gui.GuiColorListEdit;
import com.iafenvoy.jupiter.malilib.gui.interfaces.IConfigGui;
import com.iafenvoy.jupiter.malilib.gui.interfaces.IDialogHandler;
import com.iafenvoy.jupiter.malilib.util.GuiUtils;
import com.iafenvoy.jupiter.malilib.util.StringUtils;
import org.jetbrains.annotations.Nullable;

public class ConfigButtonColorList extends ButtonGeneric {
    private final IConfigColorList config;
    private final IConfigGui configGui;
    @Nullable
    private final IDialogHandler dialogHandler;

    public ConfigButtonColorList(int x, int y, int width, int height, IConfigColorList config, IConfigGui configGui, @Nullable IDialogHandler dialogHandler) {
        super(x, y, width, height, "");

        this.config = config;
        this.configGui = configGui;
        this.dialogHandler = dialogHandler;

        this.updateDisplayString();
    }

    @Override
    protected boolean onMouseClickedImpl(int mouseX, int mouseY, int mouseButton) {
        super.onMouseClickedImpl(mouseX, mouseY, mouseButton);

        if (this.dialogHandler != null) {
            this.dialogHandler.openDialog(new GuiColorListEdit(this.config, this.configGui, this.dialogHandler, null));
        } else {
            GuiBase.openGui(new GuiColorListEdit(this.config, this.configGui, null, GuiUtils.getCurrentScreen()));
        }

        return true;
    }

    @Override
    public void updateDisplayString() {
        this.displayString = StringUtils.getClampedDisplayStringRenderlen(this.config.getColors().stream().map(Object::toString).toList(), this.width - 10, "[ ", " ]");
    }
}
