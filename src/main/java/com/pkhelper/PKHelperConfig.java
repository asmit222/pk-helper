
package com.pkhelper;

import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup("pkhelper")
public interface PKHelperConfig extends Config {
    enum FarcastDisplayMode {
        NEVER("Never"),
        ALWAYS("Always"),
        MAGIC_WEAPON_ONLY("Magic Weapon Only");

        private final String name;

        FarcastDisplayMode(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    // ===== SECTIONS =====

    @ConfigSection(
            name = "Freeze Settings",
            description = "Settings for freeze countdown timers",
            position = 0
    )
    String freezeSection = "freezeSection";

    @ConfigSection(
            name = "Prayer & Enemy Highlights",
            description = "Settings for prayer and enemy highlighting",
            position = 1
    )
    String prayerSection = "prayerSection";

    @ConfigSection(
            name = "Farcast Tiles",
            description = "Settings for farcast tile highlighting",
            position = 2
    )
    String farcastSection = "farcastSection";

    @ConfigSection(
            name = "Inventory Highlights",
            description = "Settings for inventory gear highlighting",
            position = 3
    )
    String inventorySection = "inventorySection";

    // ===== FREEZE SETTINGS =====

    @ConfigItem(
            keyName = "showCountdown",
            name = "Show Freeze Countdown",
            description = "Display freeze countdown above your player when frozen",
            section = freezeSection
    )
    default boolean showCountdown() {
        return true;
    }

    @ConfigItem(
            keyName = "showOpponentFreezeCountdown",
            name = "Show Opponent Freeze Countdown",
            description = "Display freeze countdown above your opponent when they are frozen",
            section = freezeSection
    )
    default boolean showOpponentFreezeCountdown() {
        return true;
    }

    @ConfigItem(
            keyName = "enableFlash",
            name = "Enable Screen Flash",
            description = "Flash the screen briefly when you are frozen",
            section = freezeSection
    )
    default boolean enableFlash() {
        return true;
    }

    @Alpha
    @ConfigItem(
            keyName = "flashColor",
            name = "Flash Color",
            description = "Color of the flash when you are frozen",
            section = freezeSection
    )
    default Color flashColor() {
        return Color.WHITE;
    }

    @ConfigItem(
            keyName = "flashOpacity",
            name = "Flash Opacity",
            description = "Opacity of the flash (0–100%)",
            section = freezeSection
    )
    default int flashOpacityPercent() {
        return 10;
    }

    @ConfigItem(
            keyName = "flashDurationMs",
            name = "Flash Duration (ms)",
            description = "How long the flash lasts in milliseconds",
            section = freezeSection
    )
    default int flashDurationMs() {
        return 150;
    }

    @Range(min = -100, max = 100)
    @ConfigItem(
            keyName = "freezeIconOffsetX",
            name = "Freeze Icon X Offset",
            description = "Horizontal offset for the freeze icon and countdown (negative = left, positive = right)",
            section = freezeSection
    )
    default int freezeIconOffsetX() {
        return 25;
    }

    @Range(min = -100, max = 100)
    @ConfigItem(
            keyName = "freezeIconOffsetY",
            name = "Freeze Icon Y Offset",
            description = "Vertical offset for the freeze icon and countdown (negative = up, positive = down)",
            section = freezeSection
    )
    default int freezeIconOffsetY() {
        return -5;
    }

    @Range(min = -100, max = 100)
    @ConfigItem(
            keyName = "opponentFreezeIconOffsetX",
            name = "Opponent Freeze Icon X Offset",
            description = "Horizontal offset for opponent's freeze icon and countdown (negative = left, positive = right)",
            section = freezeSection
    )
    default int opponentFreezeIconOffsetX() {
        return 25;
    }

    @Range(min = -100, max = 100)
    @ConfigItem(
            keyName = "opponentFreezeIconOffsetY",
            name = "Opponent Freeze Icon Y Offset",
            description = "Vertical offset for opponent's freeze icon and countdown (negative = up, positive = down)",
            section = freezeSection
    )
    default int opponentFreezeIconOffsetY() {
        return -5;
    }

    // ===== PRAYER & ENEMY SETTINGS =====

    @ConfigItem(
            keyName = "showPrayerHighlights",
            name = "Show Prayer Highlights",
            description = "Highlight the correct protection prayer based on opponent's attack style",
            section = prayerSection
    )
    default boolean showPrayerHighlights() {
        return true;
    }

    @ConfigItem(
            keyName = "showEnemyBorder",
            name = "Show Enemy Border",
            description = "Highlight the opponent with a colored border based on their attack style",
            section = prayerSection
    )
    default boolean showEnemyBorder() {
        return true;
    }

    // ===== FARCAST SETTINGS =====

    @ConfigItem(
            keyName = "farcastDisplayMode",
            name = "Show Farcast Tiles",
            description = "When to highlight tiles where you can hit with magic but opponent can't hit with rapid ranged (8-10 tiles)",
            section = farcastSection
    )
    default FarcastDisplayMode farcastDisplayMode() {
        return FarcastDisplayMode.ALWAYS;
    }

    @Alpha
    @ConfigItem(
            keyName = "farcastOptimalColor",
            name = "Optimal Farcast Color (8 tiles)",
            description = "Color for tiles at 8 tiles distance (just outside rapid range - most optimal)",
            section = farcastSection
    )
    default Color farcastOptimalColor() {
        return new Color(0, 255, 0, 50);
    }

    @Alpha
    @ConfigItem(
            keyName = "farcastSafeColor",
            name = "Safe Farcast Color (9 tiles)",
            description = "Color for tiles at 9 tiles distance (safe farcast range)",
            section = farcastSection
    )
    default Color farcastSafeColor() {
        return new Color(0, 200, 255, 50);
    }

    @Alpha
    @ConfigItem(
            keyName = "farcastMaxColor",
            name = "Max Range Color (10 tiles)",
            description = "Color for tiles at 10 tiles distance (max magic range - edge)",
            section = farcastSection
    )
    default Color farcastMaxColor() {
        return new Color(255, 255, 0, 50);
    }

    // ===== INVENTORY SETTINGS =====

    @ConfigItem(
            keyName = "showInventoryHighlights",
            name = "Show Inventory Gear Highlights",
            description = "Highlight weapons and armor in your inventory based on combat style (blue=magic, green=ranged, red=melee)",
            section = inventorySection
    )
    default boolean showInventoryHighlights() {
        return true;
    }

    @ConfigItem(
            keyName = "customMagicIds",
            name = "Custom Magic Item IDs",
            description = "Comma-separated list of item IDs to highlight as magic (blue). Example: 4675,11905,12899",
            section = inventorySection
    )
    default String customMagicIds() {
        return "";
    }

    @ConfigItem(
            keyName = "customRangedIds",
            name = "Custom Ranged Item IDs",
            description = "Comma-separated list of item IDs to highlight as ranged (green). Example: 11785,12926,21902",
            section = inventorySection
    )
    default String customRangedIds() {
        return "";
    }

    @ConfigItem(
            keyName = "customMeleeIds",
            name = "Custom Melee Item IDs",
            description = "Comma-separated list of item IDs to highlight as melee (red). Example: 11802,13576,20784",
            section = inventorySection
    )
    default String customMeleeIds() {
        return "";
    }
}