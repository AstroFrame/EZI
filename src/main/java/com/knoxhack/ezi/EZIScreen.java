package com.knoxhack.ezi;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EZIScreen extends Screen {
    private EditBox searchBox;
    private List<ItemStack> allItems = new ArrayList<>();
    private List<ItemStack> filteredItems = new ArrayList<>();
    private int page = 0;
    private static final int ITEMS_PER_ROW = 10;
    private static final int ITEMS_PER_COLUMN = 6;
    private Button nextPageButton;
    private Button prevPageButton;
    private Button themeButton;
    private boolean sortByName = true;

    public EZIScreen() {
        super(Component.literal("EZI - Easy Items Index"));
    }

    @Override
    protected void init() {
        int centerX = width / 2;

        this.searchBox = new EditBox(this.font, centerX - 100, 20, 200, 20, Component.literal("Search"));
        this.addRenderableWidget(searchBox);

        this.nextPageButton = Button.builder(Component.literal(">"), button -> nextPage()).bounds(centerX + 120, 20, 20, 20).build();
        this.prevPageButton = Button.builder(Component.literal("<"), button -> prevPage()).bounds(centerX - 140, 20, 20, 20).build();
        this.themeButton = Button.builder(Component.literal(ThemeManager.isDarkMode() ? "Light" : "Dark"), button -> toggleTheme()).bounds(centerX - 40, height - 40, 80, 20).build();

        this.addRenderableWidget(nextPageButton);
        this.addRenderableWidget(prevPageButton);
        this.addRenderableWidget(themeButton);

        for (Item item : BuiltInRegistries.ITEM) {
            allItems.add(new ItemStack(item));
        }
        updateFilter();
    }

    private void toggleTheme() {
        ThemeManager.toggleTheme();
        themeButton.setMessage(Component.literal(ThemeManager.isDarkMode() ? "Light" : "Dark"));
    }

    private void nextPage() {
        if ((page + 1) * ITEMS_PER_ROW * ITEMS_PER_COLUMN < filteredItems.size()) {
            page++;
        }
    }

    private void prevPage() {
        if (page > 0) {
            page--;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        searchBox.render(guiGraphics, mouseX, mouseY, partialTicks);

        int startX = width / 2 - (ITEMS_PER_ROW * 20) / 2;
        int startY = 60;

        int index = page * ITEMS_PER_ROW * ITEMS_PER_COLUMN;

        for (int i = 0; i < ITEMS_PER_ROW * ITEMS_PER_COLUMN && index + i < filteredItems.size(); i++) {
            ItemStack stack = filteredItems.get(index + i);
            int x = startX + (i % ITEMS_PER_ROW) * 20;
            int y = startY + (i / ITEMS_PER_ROW) * 20;

            guiGraphics.renderItem(stack, x, y);

            if (FavoritesManager.isFavorite(stack)) {
                guiGraphics.drawString(this.font, "*", x, y, 0xFFFF00, true);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int startX = width / 2 - (ITEMS_PER_ROW * 20) / 2;
            int startY = 60;

            int index = page * ITEMS_PER_ROW * ITEMS_PER_COLUMN;

            for (int i = 0; i < ITEMS_PER_ROW * ITEMS_PER_COLUMN && index + i < filteredItems.size(); i++) {
                ItemStack stack = filteredItems.get(index + i);
                int x = startX + (i % ITEMS_PER_ROW) * 20;
                int y = startY + (i / ITEMS_PER_ROW) * 20;

                if (mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16) {
                    Minecraft.getInstance().setScreen(new RecipeScreen(stack));
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void tick() {
        super.tick();
        searchBox.tick();
        updateFilter();
    }

    private void updateFilter() {
        String query = searchBox.getValue().toLowerCase();
        filteredItems = allItems.stream()
                .filter(stack -> stack.getHoverName().getString().toLowerCase().contains(query))
                .sorted(sortByName ? Comparator.comparing(stack -> stack.getHoverName().getString()) : Comparator.comparing(stack -> stack.getItem().getDescriptionId()))
                .collect(Collectors.toList());
        page = 0;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            Minecraft.getInstance().setScreen(null);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}