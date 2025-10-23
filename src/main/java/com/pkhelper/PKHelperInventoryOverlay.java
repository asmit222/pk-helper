package com.pkhelper;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.inject.Inject;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Highlights weapons and armor in the inventory based on their attack style
 */
@Slf4j
public class PKHelperInventoryOverlay extends Overlay {
    private final Client client;
    private final PKHelperConfig config;
    private final ItemStyleDetector itemStyleDetector;

    private static final int INVENTORY_WIDGET_ID = 149;
    private static final int INVENTORY_SLOTS = 28;

    // Cached custom IDs
    private Set<Integer> customMagicIds = new HashSet<>();
    private Set<Integer> customRangedIds = new HashSet<>();
    private Set<Integer> customMeleeIds = new HashSet<>();
    private String lastMagicConfig = "";
    private String lastRangedConfig = "";
    private String lastMeleeConfig = "";

    @Inject
    public PKHelperInventoryOverlay(Client client, PKHelperConfig config,
                                    ItemStyleDetector itemStyleDetector) {
        this.client = client;
        this.config = config;
        this.itemStyleDetector = itemStyleDetector;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!config.showInventoryHighlights()) {
            return null;
        }

        // Update cached custom IDs if config changed
        updateCustomIds();

        ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);
        if (inventory == null) {
            return null;
        }

        Widget inventoryWidget = client.getWidget(INVENTORY_WIDGET_ID, 0);
        if (inventoryWidget == null || inventoryWidget.isHidden()) {
            return null;
        }

        Item[] items = inventory.getItems();
        Widget[] children = inventoryWidget.getDynamicChildren();

        if (children == null || children.length < INVENTORY_SLOTS) {
            return null;
        }

        // Iterate through inventory slots
        for (int i = 0; i < Math.min(INVENTORY_SLOTS, items.length); i++) {
            Item item = items[i];
            if (item == null || item.getId() <= 0) {
                continue;
            }

            Widget itemWidget = children[i];
            if (itemWidget == null) {
                continue;
            }

            // Determine item style
            WeaponInfo.Style style = getItemStyle(item.getId());
            if (style == WeaponInfo.Style.UNKNOWN) {
                continue;
            }

            // Get item bounds
            Rectangle bounds = itemWidget.getBounds();
            if (bounds == null) {
                continue;
            }

            // Draw highlight
            drawItemHighlight(graphics, bounds, style);
        }

        return null;
    }

    /**
     * Update cached custom IDs from config if changed
     */

    private void updateCustomIds() {
        String magicConfig = config.customMagicIds();
        String rangedConfig = config.customRangedIds();
        String meleeConfig = config.customMeleeIds();

        if (!magicConfig.equals(lastMagicConfig)) {
            customMagicIds = ConfigParser.parseItemIds(magicConfig);
            lastMagicConfig = magicConfig;
            log.debug("Updated custom magic IDs: {}", customMagicIds);
        }

        if (!rangedConfig.equals(lastRangedConfig)) {
            customRangedIds = ConfigParser.parseItemIds(rangedConfig);
            lastRangedConfig = rangedConfig;
            log.debug("Updated custom ranged IDs: {}", customRangedIds);
        }

        if (!meleeConfig.equals(lastMeleeConfig)) {
            customMeleeIds = ConfigParser.parseItemIds(meleeConfig);
            lastMeleeConfig = meleeConfig;
            log.debug("Updated custom melee IDs: {}", customMeleeIds);
        }
    }

    /**
     * Parse comma-separated item IDs
     */


    /**
     * Determine the attack style of an item (weapon or armor)
     */
    private WeaponInfo.Style getItemStyle(int itemId) {
        return itemStyleDetector.detectStyle(itemId, customMagicIds, customRangedIds, customMeleeIds);
    }

    /**
     * Draw highlight around inventory item
     */
    private void drawItemHighlight(Graphics2D graphics, Rectangle bounds, WeaponInfo.Style style) {
        Color color;
        switch (style) {
            case MAGIC:
                color = new Color(100, 150, 255, 200); // Blue
                break;
            case RANGE:
                color = new Color(100, 255, 100, 200); // Green
                break;
            case MELEE:
                color = new Color(255, 100, 100, 200); // Red
                break;
            default:
                return;
        }

        // Draw border
        graphics.setStroke(new BasicStroke(2));
        graphics.setColor(color);
        graphics.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

        // Draw semi-transparent fill
        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
        graphics.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }
}