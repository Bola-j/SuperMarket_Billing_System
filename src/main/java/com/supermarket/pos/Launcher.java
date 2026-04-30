package com.supermarket.pos;

/**
 * A separate Launcher class that does NOT extend Application.
 * This is required for some packaging tools and fat JARs to correctly
 * initialize the JavaFX runtime.
 */
public class Launcher {
    public static void main(String[] args) {
        Main.main(args);
    }
}
