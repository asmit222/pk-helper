package com.pkhelper;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.inject.Inject;
import java.awt.*;

/**
 * Highlights tiles that are optimal for farcasting
 * (within magic range but outside rapid ranged range)
 */
@Slf4j
public class PKHelperFarcastOverlay extends Overlay {
    private final Client client;
    private final PKHelperPlugin plugin;
    private final PKHelperConfig config;
    private final ItemManager itemManager; // ✅ Added

    // Combat ranges
    private static final int MAGIC_MAX_RANGE = 10;
    private static final int RAPID_RANGE_MAX = 7; // Most weapons on rapid

    private WorldPoint lastOpponentLocation = null;
    private int stableFrames = 0;
    private static final int STABILITY_THRESHOLD = 3; // Number of frames opponent must stay in place

    @Inject
    public PKHelperFarcastOverlay(Client client, PKHelperPlugin plugin, PKHelperConfig config, ItemManager itemManager) {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.itemManager = itemManager;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        PKHelperConfig.FarcastDisplayMode displayMode = config.farcastDisplayMode();

        // Check display mode
        if (displayMode == PKHelperConfig.FarcastDisplayMode.NEVER) {
            return null;
        }

        // Only show tiles when wielding a magic weapon if configured
        if (displayMode == PKHelperConfig.FarcastDisplayMode.MAGIC_WEAPON_ONLY) {
            if (!isWieldingMagicWeapon()) {
                lastOpponentLocation = null;
                stableFrames = 0;
                return null;
            }
        }

        Player opponent = plugin.getCurrentOpponent();
        if (opponent == null) {
            lastOpponentLocation = null;
            stableFrames = 0;
            return null;
        }

        WorldPoint opponentLocation = opponent.getWorldLocation();
        if (opponentLocation == null) {
            return null;
        }

        Player local = client.getLocalPlayer();
        if (local == null) {
            return null;
        }

        WorldPoint localLocation = local.getWorldLocation();
        if (localLocation.getPlane() != opponentLocation.getPlane()) {
            lastOpponentLocation = null;
            stableFrames = 0;
            return null;
        }

        // Stability check - only update if opponent has been in same position for multiple frames
        if (lastOpponentLocation == null || !lastOpponentLocation.equals(opponentLocation)) {
            lastOpponentLocation = opponentLocation;
            stableFrames = 0;
        } else {
            stableFrames++;
        }

        // Use the stable location for rendering
        WorldPoint renderLocation = lastOpponentLocation;

        // Draw farcast tiles in a radius around opponent
        for (int dx = -MAGIC_MAX_RANGE; dx <= MAGIC_MAX_RANGE; dx++) {
            for (int dy = -MAGIC_MAX_RANGE; dy <= MAGIC_MAX_RANGE; dy++) {
                WorldPoint tile = new WorldPoint(
                        renderLocation.getX() + dx,
                        renderLocation.getY() + dy,
                        renderLocation.getPlane()
                );

                int distance = renderLocation.distanceTo(tile);

                // Check if tile is in farcast range and draw with appropriate color
                if (isFarcastTile(distance)) {
                    Color tileColor = getColorForDistance(distance);
                    drawTile(graphics, tile, tileColor);
                }
            }
        }

        return null;
    }

    /**
     * Detects if the player is currently wielding a magic weapon.
     * Uses both WeaponInfo (by ID) and name-based fallback logic.
     */
    private boolean isWieldingMagicWeapon() {
        ItemContainer equipment = client.getItemContainer(InventoryID.EQUIPMENT);
        if (equipment == null) {
            return false;
        }

        Item weapon = equipment.getItem(EquipmentInventorySlot.WEAPON.getSlotIdx());
        if (weapon == null) {
            return false;
        }

        int weaponId = weapon.getId();
        WeaponInfo.Style style = WeaponInfo.getStyle(weaponId);

        // ✅ Step 1: check WeaponInfo map
        if (style == WeaponInfo.Style.MAGIC) {
            return true;
        }

        // ✅ Step 2: fallback to item name detection
        String name = itemManager.getItemComposition(weaponId).getName();
        if (name == null) {
            return false;
        }

        String lower = name.toLowerCase();
        return (lower.contains("staff")
                || lower.contains("wand")
                || lower.contains("trident")
                || lower.contains("sceptre")
                || lower.contains("shadow")
                || lower.contains("kodai")
                || lower.contains("tome")
                || lower.contains("harmonised"));
    }

    /**
     * Check if a distance is in the farcast range
     * (can hit with magic, but opponent can't hit with rapid ranged)
     */
    private boolean isFarcastTile(int distance) {
        // Farcast range: 8-10 tiles (outside rapid range, within magic range)
        return distance >= (RAPID_RANGE_MAX + 1) && distance <= MAGIC_MAX_RANGE;
    }

    /**
     * Get color based on distance with user-configured colors
     */
    private Color getColorForDistance(int distance) {
        if (distance == 8) {
            return config.farcastOptimalColor();
        } else if (distance == 9) {
            return config.farcastSafeColor();
        } else if (distance == 10) {
            return config.farcastMaxColor();
        }

        return new Color(0, 200, 255, 30);
    }

    /**
     * Draw a highlighted tile
     */
    private boolean drawTile(Graphics2D graphics, WorldPoint worldPoint, Color color) {
        if (!WorldPoint.isInScene(client, worldPoint.getX(), worldPoint.getY())) {
            return false;
        }

        LocalPoint localPoint = LocalPoint.fromWorld(client, worldPoint);
        if (localPoint == null) {
            return false;
        }

        Polygon tilePoly = Perspective.getCanvasTilePoly(client, localPoint);
        if (tilePoly == null) {
            return false;
        }

        graphics.setColor(color);
        graphics.fillPolygon(tilePoly);

        Color borderColor = new Color(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                Math.min(255, color.getAlpha() + 80)
        );
        graphics.setColor(borderColor);
        graphics.setStroke(new BasicStroke(2));
        graphics.drawPolygon(tilePoly);

        return true;
    }
}
