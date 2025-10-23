package com.pkhelper;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;

/**
 * Handles freeze state detection and tracking logic
 */
@Slf4j
public class FreezeTracker {
    private static final int FREEZE_DURATION_TICKS = 32; // 19.2 seconds (ice barrage/blitz)
    private static final int FLASH_DURATION_TICKS = 3;

    // Spell impact spotanims (when the spell hits the target)
    private static final int ICE_BARRAGE_HIT = 369;
    private static final int ICE_BLITZ_HIT = 367;
    private static final int ICE_BURST_HIT = 363;
    private static final int ICE_RUSH_HIT = 361;
    private static final int ENTANGLE_HIT = 179;
    private static final int SNARE_HIT = 180;
    private static final int BIND_HIT = 181;

    // Splash means the spell missed
    private static final int SPLASH = 85;

    private static int lastOpponentSpotanim = -1;

    /**
     * Process chat message for freeze detection (player freeze only)
     */
    public static void processChatMessage(String message, CombatState state) {
        String msg = message.toLowerCase();

        // Player freeze detection
        if (msg.contains("you have been frozen!") || msg.contains("you have been snared") ||
                msg.contains("you have been bound") || msg.contains("you have been entangled")) {
            state.setFrozen(true);
            state.setFreezeTicksRemaining(FREEZE_DURATION_TICKS);
            state.setFlashTicksRemaining(FLASH_DURATION_TICKS);
            log.info("[PKHelper] Freeze detected!");
        } else if (msg.contains("you have been unfrozen")) {
            state.setFrozen(false);
            state.setFreezeTicksRemaining(0);
            log.info("[PKHelper] Unfrozen detected!");
        }
    }

    /**
     * Check opponent's spotanim for freeze effects
     */
    public static void checkOpponentSpotanim(Player opponent, CombatState state) {
        if (opponent == null) {
            return;
        }

        // Check if freeze is already active
        if (state.isOpponentFrozen()) {
            return;
        }

        // Get the spotanim (graphic) on the player
        int spotanim = opponent.getGraphic();

        // Only process if spotanim changed
        if (spotanim == lastOpponentSpotanim) {
            return;
        }

        lastOpponentSpotanim = spotanim;

        // Log all spotanims for debugging
        if (spotanim != -1 && spotanim != SPLASH) {
            log.info("[PKHelper] Opponent spotanim changed to: {}", spotanim);
        }

        // Check if it's a freeze spell HIT (not a splash)
        if (isFreezeHit(spotanim)) {
            WorldPoint opponentLocation = opponent.getWorldLocation();
            state.setOpponentFrozen(true);
            state.setOpponentFreezeTicksRemaining(FREEZE_DURATION_TICKS);
            state.setOpponentLastLocation(opponentLocation);
            log.info("[PKHelper] *** OPPONENT FREEZE DETECTED! *** Spotanim ID: {}, Location: ({}, {})",
                    spotanim, opponentLocation.getX(), opponentLocation.getY());
        }
    }

    /**
     * Check if opponent has moved (indicating freeze ended)
     */
    public static void checkOpponentMovement(Player opponent, CombatState state) {
        if (opponent == null || !state.isOpponentFrozen()) {
            return;
        }

        WorldPoint currentLocation = opponent.getWorldLocation();
        WorldPoint lastLocation = state.getOpponentLastLocation();

        if (currentLocation == null || lastLocation == null) {
            return;
        }

        // If opponent moved, they're no longer frozen
        if (currentLocation.getX() != lastLocation.getX() ||
                currentLocation.getY() != lastLocation.getY()) {
            state.setOpponentFrozen(false);
            state.setOpponentFreezeTicksRemaining(0);
            log.info("[PKHelper] Opponent moved - freeze ended. From ({}, {}) to ({}, {})",
                    lastLocation.getX(), lastLocation.getY(), currentLocation.getX(), currentLocation.getY());
        }
    }

    /**
     * Check if a spotanim ID is a freeze spell HIT
     */
    private static boolean isFreezeHit(int spotanimId) {
        return spotanimId == ICE_BARRAGE_HIT ||
                spotanimId == ICE_BLITZ_HIT ||
                spotanimId == ICE_BURST_HIT ||
                spotanimId == ICE_RUSH_HIT ||
                spotanimId == ENTANGLE_HIT ||
                spotanimId == SNARE_HIT ||
                spotanimId == BIND_HIT;
    }

    /**
     * Update freeze timers each tick
     */
    public static void updateTimers(CombatState state) {
        // Update player freeze
        if (state.isFrozen() && state.getFreezeTicksRemaining() > 0) {
            state.setFreezeTicksRemaining(state.getFreezeTicksRemaining() - 1);
            if (state.getFreezeTicksRemaining() == 0) {
                state.setFrozen(false);
                log.info("[PKHelper] Player freeze expired");
            }
        }

        // Update opponent freeze
        if (state.isOpponentFrozen() && state.getOpponentFreezeTicksRemaining() > 0) {
            int ticksRemaining = state.getOpponentFreezeTicksRemaining() - 1;
            state.setOpponentFreezeTicksRemaining(ticksRemaining);

            if (ticksRemaining % 5 == 0 || ticksRemaining <= 3) {
                log.info("[PKHelper] Opponent freeze ticks remaining: {}", ticksRemaining);
            }

            if (ticksRemaining == 0) {
                state.setOpponentFrozen(false);
                log.info("[PKHelper] Opponent freeze expired (timer ran out)");
            }
        }

        // Update flash
        if (state.getFlashTicksRemaining() > 0) {
            state.setFlashTicksRemaining(state.getFlashTicksRemaining() - 1);
        }
    }
}