package com.iafenvoy.jupiter.render.screen;

import com.iafenvoy.jupiter.Jupiter;
import com.iafenvoy.jupiter.config.container.AbstractConfigContainer;
import com.iafenvoy.jupiter.config.container.FakeConfigContainer;
import com.iafenvoy.jupiter.config.container.FileConfigContainer;
import com.iafenvoy.jupiter.network.ClientConfigNetwork;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ConfigSelectScreen<S extends FileConfigContainer, C extends FileConfigContainer> extends Screen {
    private final Screen parent;
    private final S serverConfig;
    @Nullable
    private final C clientConfig;
    @Nullable
    private FakeConfigContainer fakeServerConfig;

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
            this.client.setScreen(this.parent);
        }).dimensions(x - 100, y - 25 - 10, 200, 20)
                .build());
        //Server
        final ButtonWidget serverButton = this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("jupiter.screen.server_config"), button -> {
            assert this.client != null;
            assert this.serverConfig != null;
            this.client.setScreen(new ServerConfigScreen(this, this.getServerConfig()));
        }).tooltip(Tooltip.of(Text.translatable("jupiter.screen.check_server")))
                .dimensions(x - 100, y - 10, 200, 20)
                .build());
        serverButton.active = true;
        //Client
        final ButtonWidget clientButton = this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("jupiter.screen.client_config"), button -> {
            assert this.client != null;
            assert this.clientConfig != null;
            this.client.setScreen(new ClientConfigScreen(this, this.clientConfig));
        }).tooltip(Tooltip.of(Text.translatable(this.clientConfig != null ? "jupiter.screen.open_client" : "jupiter.screen.disable_client")))
                .dimensions(x - 100, y + 25 - 10, 200, 20)
                .build());
        clientButton.active = this.clientConfig != null;

        if (this.connectedToDedicatedServer()) {
            this.fakeServerConfig = new FakeConfigContainer(this.serverConfig);
            serverButton.active = false;
            ClientConfigNetwork.startConfigSync(this.serverConfig.getConfigId(), nbt -> {
                if (nbt == null)
                    serverButton.setTooltip(Tooltip.of(Text.translatable("jupiter.screen.disable_server")));
                else {
                    try {
                        assert this.fakeServerConfig != null;
                        this.fakeServerConfig.deserializeNbt(nbt);
                        serverButton.setTooltip(Tooltip.of(Text.translatable("jupiter.screen.open_server")));
                        serverButton.active = true;
                    } catch (Exception e) {
                        Jupiter.LOGGER.error("Failed to parse server config data from server: {}", this.serverConfig.getConfigId(), e);
                        serverButton.setTooltip(Tooltip.of(Text.translatable("jupiter.screen.error_server")));
                    }
                }
            });
        } else serverButton.setTooltip(Tooltip.of(Text.translatable("jupiter.screen.open_server")));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        assert this.client != null;
        context.drawCenteredTextWithShadow(this.client.textRenderer, this.title, this.width / 2, this.height / 2 - 50, -1);
    }

    @Override
    public void close() {
        assert this.client != null;
        this.client.setScreen(this.parent);
    }

    @Override
    public boolean shouldPause() {
        return true;
    }

    private AbstractConfigContainer getServerConfig() {
        if (!this.connectedToDedicatedServer())
            return this.serverConfig;
        assert this.fakeServerConfig != null;
        return this.fakeServerConfig;
    }

    public boolean connectedToDedicatedServer() {
        assert this.client != null;
        ClientPlayNetworkHandler handler = this.client.getNetworkHandler();
        IntegratedServer server = this.client.getServer();
        return handler != null && handler.getConnection().isOpen() && (server == null || server.isRemote());
    }
}
