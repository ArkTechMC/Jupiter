package com.iafenvoy.jupiter.screen;

import com.iafenvoy.jupiter.api.ConfigData;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class ConfigSelectScreen<S extends ConfigData, C extends ConfigData> extends Screen {
    private final Screen parent;
    private final S serverConfig;
    @Nullable
    private final C clientConfig;
    private ButtonWidget serverButton;
    private ButtonWidget clientButton;

    public ConfigSelectScreen(Text title, Screen parent, S serverConfig, @Nullable C clientConfig) {
        super(title);
        this.parent = parent;
        this.serverConfig = serverConfig;
        this.clientConfig = clientConfig;
    }

    @Override
    protected void init() {
        super.init();
        int x = this.width / 2;
        int y = this.height / 2;
        //Back
        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("jupiter.screen.back"), button -> {
            assert this.client != null;
            assert this.serverConfig != null;
            this.client.setScreen(new AbstractConfigScreen(this, this.serverConfig));
        }).dimensions(x - 100, y - 25 - 10, 200, 20)
                .build());
        //Server
        this.serverButton = this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("jupiter.screen.server_config"), button -> {
            assert this.client != null;
            assert this.serverConfig != null;
            this.client.setScreen(new AbstractConfigScreen(this, this.serverConfig));
        }).tooltip(Tooltip.of(Text.translatable("jupiter.screen.check_server")))
                .dimensions(x - 100, y - 10, 200, 20)
                .build());
        this.serverButton.active = true;
        //Client
        this.clientButton = this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("jupiter.screen.client_config"), button -> {
            assert this.client != null;
            assert this.clientConfig != null;
            this.client.setScreen(new AbstractConfigScreen(this, this.clientConfig));
        }).tooltip(Tooltip.of(Text.translatable(this.clientConfig != null ? "jupiter.screen.open_client" : "jupiter.screen.disable_client")))
                .dimensions(x - 100, y + 25 - 10, 200, 20)
                .build());
        this.clientButton.active = this.clientConfig != null;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        assert this.client != null;
        context.drawCenteredTextWithShadow(this.client.textRenderer, this.title, this.width / 2, this.height / 2 - 50, -1);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        assert this.client != null;
        this.client.setScreen(this.parent);
    }
}
