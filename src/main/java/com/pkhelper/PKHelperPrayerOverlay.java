package com.pkhelper;

import net.runelite.api.Client;
import net.runelite.api.Prayer;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.inject.Inject;
import java.awt.*;

public class PKHelperPrayerOverlay extends Overlay {
    private final Client client;
    private final PKHelperPlugin plugin;
    private final PKHelperConfig config;

    @Inject
    public PKHelperPrayerOverlay(Client client, PKHelperPlugin plugin, PKHelperConfig config) {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
    }

    @Override
    public Dimension render(Graphics2D g) {
        if (!config.showPrayerHighlights()) {
            return null;
        }

        WeaponInfo.Style style = plugin.getCurrentOpponentStyle();
        if (style == WeaponInfo.Style.UNKNOWN) {
            return null;
        }

        Prayer correctPrayer = PrayerHelper.getPrayerForStyle(style);
        int correctSprite = PrayerHelper.getSpriteForStyle(style);

        if (correctPrayer == null || correctSprite == -1) {
            return null;
        }

        // Only highlight if prayer is NOT active
        if (!client.isPrayerActive(correctPrayer)) {
            Widget widget = WidgetSearcher.findWidgetBySprite(client, correctSprite);
            if (widget != null && !widget.isHidden()) {
                drawHighlight(g, widget, style);
            }
        }

        return null;
    }

    private void drawHighlight(Graphics2D g, Widget widget, WeaponInfo.Style style) {
        Rectangle bounds = widget.getBounds();
        if (bounds == null) {
            return;
        }

        Color color = PrayerHelper.getColorForStyle(style);

        g.setStroke(new BasicStroke(3));
        g.setColor(color);
        g.draw(bounds);
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 40));
        g.fill(bounds);
    }
}