package com.pkhelper;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;

/**
 * Manages the current combat state for the PK Helper plugin
 */
@Getter
@Setter
public class CombatState {
    private Player currentOpponent;
    private int currentOpponentWeaponId = -1;
    private String currentOpponentWeaponName = "";
    private WeaponInfo.Style currentOpponentStyle = WeaponInfo.Style.UNKNOWN;

    private boolean frozen = false;
    private int freezeTicksRemaining = 0;
    private int flashTicksRemaining = 0;
    private int combatTimer = 0;

    // Opponent freeze tracking
    private boolean opponentFrozen = false;
    private int opponentFreezeTicksRemaining = 0;
    private WorldPoint opponentLastLocation = null;

    /**
     * Reset all combat-related state
     */
    public void reset() {
        currentOpponent = null;
        currentOpponentWeaponId = -1;
        currentOpponentWeaponName = "";
        currentOpponentStyle = WeaponInfo.Style.UNKNOWN;
        frozen = false;
        freezeTicksRemaining = 0;
        flashTicksRemaining = 0;
        combatTimer = 0;
        opponentFrozen = false;
        opponentFreezeTicksRemaining = 0;
        opponentLastLocation = null;
    }

    /**
     * Check if we have an active opponent
     */
    public boolean hasOpponent() {
        return currentOpponent != null;
    }

    /**
     * Get freeze time remaining in seconds
     */
    public int getFreezeSecondsRemaining() {
        return (int) Math.ceil(freezeTicksRemaining * 0.6);
    }

    /**
     * Get opponent freeze time remaining in seconds
     */
    public int getOpponentFreezeSecondsRemaining() {
        return (int) Math.ceil(opponentFreezeTicksRemaining * 0.6);
    }

    /**
     * Check if flash should be displayed
     */
    public boolean shouldFlash() {
        return flashTicksRemaining > 0;
    }
}