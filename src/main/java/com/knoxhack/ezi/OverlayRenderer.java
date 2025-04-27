package com.knoxhack.ezi;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EZIMod.MODID, value = Dist.CLIENT)
public class OverlayRenderer {

    @SubscribeEvent
    public static void renderOverlay(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;

        ItemStack hovered = mc.player.inventory.getSelected();
        if (!hovered.isEmpty()) {
            GuiGraphics graphics = event.getGuiGraphics();
            graphics.renderItemDecorations(mc.font, hovered, 10, 10);
        }
    }
}