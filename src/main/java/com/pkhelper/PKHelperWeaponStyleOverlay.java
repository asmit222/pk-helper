package com.pkhelper;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.inject.Inject;
import java.awt.*;

@Slf4j
public class PKHelperWeaponStyleOverlay extends Overlay {
    private final Client client;
    private final PKHelperPlugin plugin;
    private final PKHelperConfig config;
    private final ItemManager itemManager;

    // Layout settings
    private static final int TEXT_X = 20;
    private static final int TEXT_Y_START = 40;
    private static final int LINE_HEIGHT = 18;
    private static final int BOX_WIDTH = 250;
    private static final int BOX_HEIGHT = 60;

    @Inject
    private PKHelperWeaponStyleOverlay(Client client, PKHelperPlugin plugin, PKHelperConfig config, ItemManager itemManager) {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.itemManager = itemManager;

        setPosition(OverlayPosition.TOP_LEFT);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
    }

    @Override
    public Dimension render(Graphics2D g) {
        Player opp = plugin.getCurrentOpponent();
        if (opp == null) {
            return null;
        }

        String weaponName = plugin.getCurrentOpponentWeaponName();
        int weaponId = plugin.getCurrentOpponentWeaponId();

        // --- Determine style and prayer recommendation ---
        WeaponInfo.Style style = WeaponInfo.getStyle(weaponId);
        if (style == WeaponInfo.Style.UNKNOWN && weaponName != null) {
            String lower = weaponName.toLowerCase();
            if (lower.contains("staff") || lower.contains("wand") || lower.contains("trident") || lower.contains("sceptre"))
                style = WeaponInfo.Style.MAGIC;
            else if (lower.contains("bow") || lower.contains("dart") || lower.contains("knife") || lower.contains("crossbow"))
                style = WeaponInfo.Style.RANGE;
            else if (lower.contains("sword") || lower.contains("godsword") || lower.contains("mace") ||
                    lower.contains("dagger") || lower.contains("spear") || lower.contains("whip") ||
                    lower.contains("maul") || lower.contains("claws"))
                style = WeaponInfo.Style.MELEE;
        }

        String prayerHint = WeaponInfo.getRecommendedPrayer(style);

        // --- Choose text color by style ---
        Color color;
        switch (style) {
            case MAGIC:
                color = new Color(100, 150, 255); // blue
                break;
            case RANGE:
                color = new Color(100, 255, 100); // green
                break;
            case MELEE:
                color = new Color(255, 100, 100); // red
                break;
            default:
                color = Color.WHITE;
        }

        // --- Draw overlay background ---
        g.setColor(new Color(0, 0, 0, 150)); // semi-transparent box
        g.fillRoundRect(TEXT_X - 10, TEXT_Y_START - 20, BOX_WIDTH, BOX_HEIGHT, 10, 10);

        // --- Draw text ---
        g.setFont(new Font("Arial", Font.BOLD, 14));
        int y = TEXT_Y_START;

        g.setColor(color);
        g.drawString("Weapon: " + (weaponName == null || weaponName.isEmpty() ? "Unknown" : weaponName), TEXT_X, y);
        y += LINE_HEIGHT;

        g.setColor(Color.YELLOW);
        g.drawString("Prayer: " + prayerHint, TEXT_X, y);

        return new Dimension(BOX_WIDTH, BOX_HEIGHT);
    }
}
