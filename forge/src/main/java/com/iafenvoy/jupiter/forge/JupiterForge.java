package com.iafenvoy.jupiter.forge;

import com.iafenvoy.jupiter.Jupiter;
import com.iafenvoy.jupiter.forge.network.PacketByteBufC2S;
import com.iafenvoy.jupiter.forge.network.PacketByteBufS2C;
import com.iafenvoy.jupiter.malilib.config.ConfigManager;
import com.iafenvoy.jupiter.screen.ConfigSelectScreen;
import com.iafenvoy.jupiter.test.TestConfig;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.List;

@Mod(Jupiter.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class JupiterForge {
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new Identifier(Jupiter.MOD_ID, "buf"), () -> "1", s -> true, s -> true);

    public JupiterForge() {
        Jupiter.init();
    }

    @SubscribeEvent
    public static void process(FMLCommonSetupEvent event) {
        Jupiter.process();
        CHANNEL.registerMessage(0, PacketByteBufC2S.class, PacketByteBufC2S::encode, PacketByteBufC2S::decode, PacketByteBufC2S::handle);
        CHANNEL.registerMessage(1, PacketByteBufS2C.class, PacketByteBufS2C::encode, PacketByteBufS2C::decode, PacketByteBufS2C::handle);
        ForgeEntryPointLoader.INSTANCE.getEntries().forEach(x -> x.initializeCommonConfig(ConfigManager.getInstance()));
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class JupiterForgeClient {
        @SubscribeEvent
        public static void processClient(FMLClientSetupEvent event) {
            Jupiter.processClient();
            ForgeEntryPointLoader.INSTANCE.getEntries().forEach(x -> x.initializeClientConfig(ConfigManager.getInstance()));
            ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((client, screen) -> new ConfigSelectScreen<>(Text.translatable("jupiter.test_config"), screen, TestConfig.INSTANCE, null)));
        }
    }
}
