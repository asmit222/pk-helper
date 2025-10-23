
package com.pkhelper;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

@Slf4j
public class PKHelperOpponentFreezeOverlay extends Overlay {
    private final Client client;
    private final PKHelperPlugin plugin;
    private final PKHelperConfig config;
    private BufferedImage freezeIcon;

    private static final int ICON_SIZE = 16;
    private static final int FONT_SIZE = 18;

    @Inject
    public PKHelperOpponentFreezeOverlay(Client client, PKHelperPlugin plugin, PKHelperConfig config) {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);

        try (InputStream in = getClass().getResourceAsStream("/net/runelite/client/plugins/pkhelper/Ice_Barrage_icon.png")) {
            if (in != null) {
                freezeIcon = ImageIO.read(in);
                log.info("[PKHelper] Opponent freeze icon loaded successfully");
            } else {
                log.warn("[PKHelper] Could not find freeze icon resource");
            }
        } catch (Exception e) {
            log.warn("[PKHelper] Could not load opponent freeze icon", e);
        }
    }

    @Override
    public Dimension render(Graphics2D g) {
        if (!config.showOpponentFreezeCountdown()) {
            return null;
        }

        Player opponent = plugin.getCurrentOpponent();
        if (opponent == null) {
            return null;
        }

        boolean isOpponentFrozen = plugin.isOpponentFrozen();
        if (!isOpponentFrozen) {
            return null;
        }

        int seconds = plugin.getOpponentFreezeSecondsRemaining();

        if (seconds <= 0) {
            return null;
        }

        LocalPoint lp = opponent.getLocalLocation();
        if (lp == null) {
            return null;
        }

        Point head = Perspective.localToCanvas(client, lp, client.getPlane(), opponent.getLogicalHeight() + 40);
        if (head == null) {
            return null;
        }

        // Use config offsets
        int x = head.getX() + config.opponentFreezeIconOffsetX();
        int y = head.getY() + config.opponentFreezeIconOffsetY();

        // Draw icon
        if (freezeIcon != null) {
            g.drawImage(freezeIcon, x, y - ICON_SIZE, ICON_SIZE, ICON_SIZE, null);
        }

        // Choose color - SAME as player freeze colors (white/yellow/red)
        Color c = seconds <= 2 ? Color.RED : seconds <= 5 ? Color.YELLOW : Color.WHITE;
        g.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));
        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(seconds), x + ICON_SIZE + 7, y + 1);
        g.setColor(c);
        g.drawString(String.valueOf(seconds), x + ICON_SIZE + 6, y);

        return null;
    }
}