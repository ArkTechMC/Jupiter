package com.iafenvoy.jupiter.neoforge;

import com.iafenvoy.jupiter.ConfigManager;
import com.iafenvoy.jupiter.Jupiter;
import com.iafenvoy.jupiter.ServerConfigManager;
import com.iafenvoy.jupiter.neoforge.network.PacketByteBufC2S;
import com.iafenvoy.jupiter.neoforge.network.PacketByteBufS2C;
import com.iafenvoy.jupiter.render.screen.ConfigSelectScreen;
import com.iafenvoy.jupiter.test.TestConfig;
import net.minecraft.text.Text;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.ConfigScreenHandler;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

@Mod(Jupiter.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class JupiterNeoForge {
    public JupiterNeoForge() {
    }

    @SubscribeEvent
    public static void process(FMLCommonSetupEvent event) {
        Jupiter.process();
        ForgeEntryPointLoader.INSTANCE.getEntries().forEach(x -> x.initializeCommonConfig(ConfigManager.getInstance()));
    }

    @SubscribeEvent
    public static void register(RegisterPayloadHandlerEvent event) {
        final IPayloadRegistrar registrar = event.registrar(Jupiter.MOD_ID).versioned("1");
        registrar.play(PacketByteBufC2S.ID, PacketByteBufC2S::decode, handler -> handler.server(PacketByteBufC2S::handle));
        registrar.play(PacketByteBufS2C.ID, PacketByteBufS2C::decode, handler -> handler.server(PacketByteBufS2C::handle));
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class JupiterForgeClient {
        @SubscribeEvent
        public static void processClient(FMLClientSetupEvent event) {
            Jupiter.processClient();
            ForgeEntryPointLoader.INSTANCE.getEntries().forEach(x -> x.initializeClientConfig(ConfigManager.getInstance()));
            ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((client, screen) -> new ConfigSelectScreen<>(Text.translatable("jupiter.test_config"), screen, TestConfig.INSTANCE, null)));
        }

        @SubscribeEvent
        public static void registerClientListener(RegisterClientReloadListenersEvent event) {
            event.registerReloadListener(ConfigManager.getInstance());
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void registerServerListener(AddReloadListenerEvent event) {
            event.addListener(new ServerConfigManager());
        }
    }
}
