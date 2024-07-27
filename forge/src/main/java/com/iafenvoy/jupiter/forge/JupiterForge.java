package com.iafenvoy.jupiter.forge;

import com.iafenvoy.jupiter.Jupiter;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Jupiter.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class JupiterForge {
    public JupiterForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(Jupiter.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        Jupiter.init();
    }

    @SubscribeEvent
    public static void process(FMLCommonSetupEvent event) {
        Jupiter.process();
    }

    @SubscribeEvent
    public static void processClient(FMLClientSetupEvent event) {
        Jupiter.processClient();
    }
}
