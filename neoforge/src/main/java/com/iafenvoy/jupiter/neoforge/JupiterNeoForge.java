package com.iafenvoy.jupiter.neoforge;

import com.iafenvoy.jupiter.ConfigManager;
import com.iafenvoy.jupiter.Jupiter;
import com.iafenvoy.jupiter.ServerConfigManager;
import com.iafenvoy.jupiter.network.neoforge.ClientNetworkHelperImpl;
import com.iafenvoy.jupiter.network.neoforge.ServerNetworkHelperImpl;
import com.iafenvoy.jupiter.render.screen.ConfigSelectScreen;
import com.iafenvoy.jupiter.test.TestConfig;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.text.Text;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.Map;

@Mod(Jupiter.MOD_ID)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public final class JupiterNeoForge {
    public JupiterNeoForge() {
    }

    @SubscribeEvent
    public static void process(FMLCommonSetupEvent event) {
        Jupiter.process();
        NeoForgeEntryPointLoader.INSTANCE.getEntries().forEach(x -> x.initializeCommonConfig(ConfigManager.getInstance()));
    }

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        Jupiter.init();
        final PayloadRegistrar registrar = event.registrar("1");
        for (Map.Entry<CustomPayload.Id<CustomPayload>, PacketCodec<PacketByteBuf, CustomPayload>> entry : ServerNetworkHelperImpl.TYPES.entrySet())
            registrar.playBidirectional(
                    entry.getKey(),
                    entry.getValue(),
                    new DirectionalPayloadHandler<>(
                            ClientNetworkHelperImpl::handleData,
                            ServerNetworkHelperImpl::handleData
                    )
            );
    }

    @EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class JupiterNeoForgeClient {
        @SubscribeEvent
        public static void processClient(FMLClientSetupEvent event) {
            Jupiter.processClient();
            NeoForgeEntryPointLoader.INSTANCE.getEntries().forEach(x -> x.initializeClientConfig(ConfigManager.getInstance()));
            ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (client, screen) -> new ConfigSelectScreen<>(Text.translatable("jupiter.test_config"), screen, TestConfig.INSTANCE, null));
        }

        @SubscribeEvent
        public static void registerClientListener(RegisterClientReloadListenersEvent event) {
            event.registerReloadListener(ConfigManager.getInstance());
        }
    }

    @EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME)
    public static class NeoForgeEvents {
        @SubscribeEvent
        public static void registerServerListener(AddReloadListenerEvent event) {
            event.addListener(new ServerConfigManager());
        }
    }
}
