package com.pkhelper;

import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import javax.inject.Inject;
import java.awt.*;

public class PKHelperAttackStyleOverlay extends Overlay {
    private final Client client;
    private final PKHelperPlugin plugin;
    private final PKHelperConfig config;
    private final ModelOutlineRenderer modelOutlineRenderer;

    @Inject
    public PKHelperAttackStyleOverlay(Client client, PKHelperPlugin plugin, PKHelperConfig config, ModelOutlineRenderer modelOutlineRenderer) {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.modelOutlineRenderer = modelOutlineRenderer;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!config.showEnemyBorder()) {
            return null;
        }

        Player opponent = plugin.getCurrentOpponent();
        if (opponent == null) {
            return null;
        }

        WeaponInfo.Style style = plugin.getCurrentOpponentStyle();
        if (style == WeaponInfo.Style.UNKNOWN) {
            return null;
        }

        Color outlineColor = PrayerHelper.getOutlineColorForStyle(style);

        modelOutlineRenderer.drawOutline(
                opponent,
                3,
                outlineColor,
                200
        );

        return null;
    }
}