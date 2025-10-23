package com.pkhelper;

import net.runelite.api.Client;
import net.runelite.api.Player;

/**
 * Handles opponent detection and tracking logic
 */
public class OpponentTracker {
    private static final int COMBAT_TIMEOUT_TICKS = 10; // ≈6 seconds

    /**
     * Find the current opponent (either attacking you or being attacked by you)
     */
    public static Player findOpponent(Client client) {
        Player local = client.getLocalPlayer();
        if (local == null) {
            return null;
        }

        // Check if local player is attacking someone
        if (local.getInteracting() instanceof Player) {
            return (Player) local.getInteracting();
        }

        // Check for any player targeting you
        for (Player p : client.getPlayers()) {
            if (p != null && p.getInteracting() != null && p.getInteracting().equals(local)) {
                return p;
            }
        }

        return null;
    }

    /**
     * Check if a player is dead based on health ratio
     */
    public static boolean isDead(Player player) {
        return player != null && player.getHealthRatio() == 0;
    }
}