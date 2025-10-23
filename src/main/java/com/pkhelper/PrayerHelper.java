package com.pkhelper;

import net.runelite.api.Prayer;
import net.runelite.api.SpriteID;

import java.awt.*;

/**
 * Utility methods for prayer-related functionality
 */
public class PrayerHelper {

    /**
     * Get the recommended prayer for a weapon style
     */
    public static Prayer getPrayerForStyle(WeaponInfo.Style style) {
        switch (style) {
            case MAGIC:
                return Prayer.PROTECT_FROM_MAGIC;
            case RANGE:
                return Prayer.PROTECT_FROM_MISSILES;
            case MELEE:
                return Prayer.PROTECT_FROM_MELEE;
            default:
                return null;
        }
    }

    /**
     * Get the sprite ID for a weapon style's corresponding prayer
     */
    public static int getSpriteForStyle(WeaponInfo.Style style) {
        switch (style) {
            case MAGIC:
                return SpriteID.PRAYER_PROTECT_FROM_MAGIC;
            case RANGE:
                return SpriteID.PRAYER_PROTECT_FROM_MISSILES;
            case MELEE:
                return SpriteID.PRAYER_PROTECT_FROM_MELEE;
            default:
                return -1;
        }
    }

    /**
     * Get highlight color for a weapon style
     */
    public static Color getColorForStyle(WeaponInfo.Style style) {
        switch (style) {
            case MAGIC:
                return new Color(100, 150, 255, 180); // blue
            case RANGE:
                return new Color(100, 255, 100, 180); // green
            case MELEE:
                return new Color(255, 100, 100, 180); // red
            default:
                return new Color(255, 255, 0, 180); // yellow
        }
    }

    /**
     * Get outline color for a weapon style (for player highlighting)
     */
    public static Color getOutlineColorForStyle(WeaponInfo.Style style) {
        switch (style) {
            case MELEE:
                return new Color(255, 60, 60, 200); // red
            case RANGE:
                return new Color(60, 255, 60, 200); // green
            case MAGIC:
                return new Color(80, 180, 255, 200); // blue
            default:
                return new Color(255, 255, 255, 180); // white
        }
    }
}