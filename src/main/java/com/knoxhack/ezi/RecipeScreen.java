package com.knoxhack.ezi;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class RecipeScreen extends Screen {
    private final ItemStack targetStack;
    private List<Recipe<?>> recipes;
    private boolean showingUsages = false;
    private Button toggleButton;

    public RecipeScreen(ItemStack stack) {
        super(Component.literal("Recipes / Usages"));
        this.targetStack = stack;
    }

    @Override
    protected void init() {
        recipes = RecipeManager.findRecipesFor(targetStack);
        this.toggleButton = Button.builder(
                Component.literal("Switch"),
                button -> toggleView()
        ).bounds(width / 2 - 40, height - 40, 80, 20).build();
        this.addRenderableWidget(toggleButton);
    }

    private void toggleView() {
        showingUsages = !showingUsages;
        recipes = showingUsages
                ? UsageManager.findUsagesFor(targetStack)
                : RecipeManager.findRecipesFor(targetStack);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        if (recipes != null) {
            int startX = width / 2 - 40;
            int startY = 60;
            int i = 0;

            for (Recipe<?> recipe : recipes) {
                ItemStack result = recipe.getResultItem(Minecraft.getInstance().level.registryAccess());

                guiGraphics.renderItem(result, startX, startY + i * 20);
                guiGraphics.drawString(this.font, result.getHoverName(), startX + 20, startY + i * 20 + 6, 0xFFFFFF, false);

                i++;
                if (i >= 10) break;
            }
        }

        guiGraphics.drawCenteredString(this.font,
                showingUsages ? "Usages of: " + targetStack.getHoverName().getString()
                        : "Recipes for: " + targetStack.getHoverName().getString(),
                width / 2, 30, 0xFFFFFF);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            Minecraft.getInstance().setScreen(new EZIScreen());
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}