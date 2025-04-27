package com.knoxhack.ezi;

public class ThemeManager {
    private static boolean darkMode = true;

    public static boolean isDarkMode() {
        return darkMode;
    }

    public static void toggleTheme() {
        darkMode = !darkMode;
    }
}