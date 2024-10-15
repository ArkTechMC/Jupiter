package com.iafenvoy.jupiter.config;

import com.iafenvoy.jupiter.network.ByteBufUtil;
import com.iafenvoy.jupiter.network.ClientNetworkHelper;
import com.iafenvoy.jupiter.network.NetworkConstants;
import net.minecraft.network.PacketByteBuf;

public class FakeConfigContainer extends AbstractConfigContainer {
    public FakeConfigContainer(AbstractConfigContainer parent) {
        super(parent.getConfigId(), parent.titleNameKey);
        this.configTabs.addAll(parent.getConfigTabs().stream().map(ConfigCategory::copy).toList());
    }

    @Override
    public void init() {
    }

    @Override
    public void load() {
    }

    @Override
    public void save() {
        PacketByteBuf buf = ByteBufUtil.create().writeIdentifier(this.getConfigId());
        buf.writeString(this.serialize());
        ClientNetworkHelper.sendToServer(NetworkConstants.CONFIG_SYNC_C2S, buf);
    }

    @Override
    protected SaveFullOption saveFullOption() {
        return SaveFullOption.ALL;
    }
}
