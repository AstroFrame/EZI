package com.knoxhack.ezi;

import net.minecraft.world.item.ItemStack;
import java.util.HashSet;
import java.util.Set;

public class FavoritesManager {
    private static final Set<String> favorites = new HashSet<>();

    public static boolean isFavorite(ItemStack stack) {
        return favorites.contains(stack.getItem().getDescriptionId());
    }

    public static void toggleFavorite(ItemStack stack) {
        String id = stack.getItem().getDescriptionId();
        if (favorites.contains(id)) {
            favorites.remove(id);
        } else {
            favorites.add(id);
        }
    }

    public static Set<String> getFavorites() {
        return favorites;
    }
}