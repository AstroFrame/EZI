package com.knoxhack.ezi;

import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EZIKeyBindings {
    public static final KeyMapping OPEN_EZI = new KeyMapping(
            "key.ezi.open",
            GLFW.GLFW_KEY_I,
            "key.categories.inventory"
    );

    @SubscribeEvent
    public static void register(final RegisterKeyMappingsEvent event) {
        event.register(OPEN_EZI);
    }
}