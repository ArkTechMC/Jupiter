package com.iafenvoy.jupiter.forge;

import com.iafenvoy.jupiter.Jupiter;
import com.iafenvoy.jupiter.forge.network.PacketByteBufC2S;
import com.iafenvoy.jupiter.forge.network.PacketByteBufS2C;
import com.iafenvoy.jupiter.screen.ConfigSelectScreen;
import com.iafenvoy.jupiter.test.TestConfig;
import dev.architectury.platform.forge.EventBuses;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod(Jupiter.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class JupiterForge {
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new Identifier(Jupiter.MOD_ID, "buf"), () -> "1", s -> true, s -> true);

    public JupiterForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(Jupiter.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        Jupiter.init();
    }

    @SubscribeEvent
    public static void process(FMLCommonSetupEvent event) {
        Jupiter.process();
        CHANNEL.registerMessage(0, PacketByteBufC2S.class, PacketByteBufC2S::encode, PacketByteBufC2S::decode, PacketByteBufC2S::handle);
        CHANNEL.registerMessage(1, PacketByteBufS2C.class, PacketByteBufS2C::encode, PacketByteBufS2C::decode, PacketByteBufS2C::handle);
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class JupiterForgeClient {
        @SubscribeEvent
        public static void processClient(FMLClientSetupEvent event) {
            Jupiter.processClient();
            ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((client, screen) -> new ConfigSelectScreen<>(Text.translatable("jupiter.test_config"), screen, TestConfig.INSTANCE, null)));
        }
    }
}
