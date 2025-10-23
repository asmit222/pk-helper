
package com.pkhelper;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;

@Slf4j
@PluginDescriptor(
        name = "PK Helper",
        description = "Shows freeze timer & icon, opponent weapon, and attack-type indicator",
        tags = {"pvp", "pk", "weapon", "overlay", "freeze", "indicator"}
)
public class PKHelperPlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private PKHelperFrozenOverlay freezeOverlay;

    @Inject
    private PKHelperOpponentFreezeOverlay opponentFreezeOverlay;

    @Inject
    private PKHelperPrayerOverlay prayerOverlay;

    @Inject
    private PKHelperAttackStyleOverlay attackIndicatorOverlay;

    @Inject
    private PKHelperFarcastOverlay farcastOverlay;

    @Inject
    private PKHelperInventoryOverlay inventoryOverlay;

    @Inject
    private ItemManager itemManager;

    @Inject
    private PKHelperConfig config;

    @Inject
    private WeaponDetector weaponDetector;

    private CombatState combatState;

    @Override
    protected void startUp() {
        combatState = new CombatState();

        overlayManager.add(freezeOverlay);
        overlayManager.add(opponentFreezeOverlay);
        overlayManager.add(attackIndicatorOverlay);
        overlayManager.add(prayerOverlay);
        overlayManager.add(farcastOverlay);
        overlayManager.add(inventoryOverlay);
        log.info("[PKHelper] Started");
    }

    @Override
    protected void shutDown() {
        overlayManager.remove(freezeOverlay);
        overlayManager.remove(opponentFreezeOverlay);
        overlayManager.remove(attackIndicatorOverlay);
        overlayManager.remove(prayerOverlay);
        overlayManager.remove(farcastOverlay);
        overlayManager.remove(inventoryOverlay);
        combatState.reset();
    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        FreezeTracker.processChatMessage(event.getMessage(), combatState);
    }

    @Subscribe
    public void onActorDeath(ActorDeath actorDeath) {
        Player local = client.getLocalPlayer();
        if (local == null) {
            return;
        }

        if (actorDeath.getActor() == local || actorDeath.getActor() == combatState.getCurrentOpponent()) {
            log.info("[PKHelper] Death detected - resetting combat state");
            combatState.reset();
        }
    }


    @Subscribe
    public void onGameTick(GameTick tick) {
        if (client.getGameState() != GameState.LOGGED_IN) {
            return;
        }

        Player local = client.getLocalPlayer();
        if (local == null) {
            return;
        }

        // Check for deaths
        if (OpponentTracker.isDead(local) || OpponentTracker.isDead(combatState.getCurrentOpponent())) {
            combatState.reset();
            return;
        }

        // Find potential opponent
        Player target = OpponentTracker.findOpponent(client);

        // Only switch opponents if we don't have one, or if the new target is the one we're attacking
        if (target != null) {
            Player currentOpponent = combatState.getCurrentOpponent();

            // Switch opponent only if:
            // 1. We don't have a current opponent, OR
            // 2. We are actively attacking the new target (player initiated the switch)
            if (currentOpponent == null || local.getInteracting() == target) {
                combatState.setCurrentOpponent(target);
                combatState.setCombatTimer(10); // Reset timeout
            }
            // If we have an opponent and someone else attacks us, maintain current opponent
            else if (currentOpponent != null) {
                // Just refresh the combat timer for the existing opponent
                combatState.setCombatTimer(10);
            }

            // Check opponent's spotanim for freeze (use current opponent, not new target)
            if (combatState.getCurrentOpponent() != null) {
                FreezeTracker.checkOpponentSpotanim(combatState.getCurrentOpponent(), combatState);

                // Check if opponent moved (breaks freeze)
                FreezeTracker.checkOpponentMovement(combatState.getCurrentOpponent(), combatState);
            }
        } else {
            // Handle combat timeout
            if (combatState.getCombatTimer() > 0) {
                combatState.setCombatTimer(combatState.getCombatTimer() - 1);
            } else {
                combatState.setCurrentOpponent(null);
            }
        }

        // Update opponent weapon even if not actively in combat
        // This ensures weapon switches are detected immediately
        if (combatState.getCurrentOpponent() != null) {
            updateOpponentWeapon();
        }

        // Update freeze timers
        FreezeTracker.updateTimers(combatState);
    }

    private void updateOpponentWeapon() {
        Player opponent = combatState.getCurrentOpponent();
        if (opponent == null) {
            return;
        }

        int newWeaponId = weaponDetector.getWeaponId(opponent);
        if (newWeaponId != combatState.getCurrentOpponentWeaponId()) {
            combatState.setCurrentOpponentWeaponId(newWeaponId);

            if (newWeaponId != -1) {
                String weaponName = weaponDetector.getWeaponName(newWeaponId);
                WeaponInfo.Style style = weaponDetector.detectStyle(newWeaponId, weaponName);

                combatState.setCurrentOpponentWeaponName(weaponName);
                combatState.setCurrentOpponentStyle(style);

                log.info("[PKHelper] Opponent weapon changed to {} ({} / ID {})",
                        weaponName, style, newWeaponId);
            } else {
                combatState.setCurrentOpponentWeaponName("");
                combatState.setCurrentOpponentStyle(WeaponInfo.Style.UNKNOWN);
            }
        }
    }

    // ---- Getters for overlays ----
    public Player getCurrentOpponent() {
        return combatState.getCurrentOpponent();
    }

    public int getCurrentOpponentWeaponId() {
        return combatState.getCurrentOpponentWeaponId();
    }

    public String getCurrentOpponentWeaponName() {
        return combatState.getCurrentOpponentWeaponName();
    }

    public WeaponInfo.Style getCurrentOpponentStyle() {
        return combatState.getCurrentOpponentStyle();
    }

    public boolean isFrozen() {
        return combatState.isFrozen();
    }

    public boolean shouldFlash() {
        return combatState.shouldFlash();
    }

    public int getFreezeSecondsRemaining() {
        return combatState.getFreezeSecondsRemaining();
    }

    public boolean isOpponentFrozen() {
        return combatState.isOpponentFrozen();
    }

    public int getOpponentFreezeSecondsRemaining() {
        return combatState.getOpponentFreezeSecondsRemaining();
    }

    @Provides
    PKHelperConfig provideConfig(ConfigManager cm) {
        return cm.getConfig(PKHelperConfig.class);
    }
}