
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
public class PKHelperFrozenOverlay extends Overlay {
    private final Client client;
    private final PKHelperPlugin plugin;
    private final PKHelperConfig config;
    private BufferedImage freezeIcon;

    private static final int ICON_SIZE = 16;
    private static final int FONT_SIZE = 18;

    @Inject
    public PKHelperFrozenOverlay(Client client, PKHelperPlugin plugin, PKHelperConfig config) {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);

        try (InputStream in = getClass().getResourceAsStream("/Ice_Barrage_icon.png")) {
            if (in != null) {
                freezeIcon = ImageIO.read(in);
                log.info("Freeze icon loaded successfully: {}x{}", freezeIcon.getWidth(), freezeIcon.getHeight());
            } else {
                log.error("InputStream is null - Ice_Barrage_icon.png not found in resources!");
            }
        } catch (Exception e) {
            log.error("Could not load freeze icon", e);
        }
    }

    @Override
    public Dimension render(Graphics2D g) {
        Player local = client.getLocalPlayer();
        if (local == null || !plugin.isFrozen()) return null;

        int seconds = plugin.getFreezeSecondsRemaining();
        if (seconds <= 0) return null;

        LocalPoint lp = local.getLocalLocation();
        Point head = Perspective.localToCanvas(client, lp, client.getPlane(), local.getLogicalHeight() + 40);
        if (head == null) return null;

        // Use config offsets - FIXED: use freezeIconOffsetX/Y instead of freezeCounterOffsetX/Y
        int x = head.getX() + config.freezeIconOffsetX();
        int y = head.getY() + config.freezeIconOffsetY();

        // draw icon
        if (freezeIcon != null)
            g.drawImage(freezeIcon, x, y - ICON_SIZE, ICON_SIZE, ICON_SIZE, null);

        // choose color
        Color c = seconds <= 2 ? Color.RED : seconds <= 5 ? Color.YELLOW : Color.WHITE;
        g.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));
        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(seconds), x + ICON_SIZE + 7, y + 1);
        g.setColor(c);
        g.drawString(String.valueOf(seconds), x + ICON_SIZE + 6, y);

        // flash overlay
        if (config.enableFlash() && plugin.shouldFlash()) {
            Color fc = config.flashColor();
            int alpha = (int) (255 * (config.flashOpacityPercent() / 100.0));
            g.setColor(new Color(fc.getRed(), fc.getGreen(), fc.getBlue(), alpha));
            g.fillRect(0, 0, client.getCanvasWidth(), client.getCanvasHeight());
        }
        return null;
    }
}