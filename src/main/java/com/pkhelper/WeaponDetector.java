package com.pkhelper;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Player;
import net.runelite.api.kit.KitType;
import net.runelite.client.game.ItemManager;

import javax.inject.Inject;

/**
 * Handles weapon detection and style classification
 */
@Slf4j
public class WeaponDetector {
    private final ItemManager itemManager;

    @Inject
    public WeaponDetector(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    /**
     * Get weapon ID from player's equipment
     */
    public int getWeaponId(Player player) {
        if (player == null || player.getPlayerComposition() == null) {
            return -1;
        }
        return player.getPlayerComposition().getEquipmentId(KitType.WEAPON);
    }

    /**
     * Get weapon name from weapon ID
     */
    public String getWeaponName(int weaponId) {
        if (weaponId == -1) {
            return "";
        }

        try {
            return itemManager.getItemComposition(weaponId).getName();
        } catch (Exception e) {
            log.warn("Failed to get weapon name for ID: {}", weaponId, e);
            return "Unknown";
        }
    }

    /**
     * Detect weapon style from ID and name
     */
    public WeaponInfo.Style detectStyle(int weaponId, String weaponName) {
        // Step 1: Use WeaponInfo map
        WeaponInfo.Style mapStyle = WeaponInfo.getStyle(weaponId);
        if (mapStyle != WeaponInfo.Style.UNKNOWN) {
            return mapStyle;
        }

        // Step 2: Fallback keyword detection
        return detectStyleFromName(weaponName);
    }

    /**
     * Detect weapon style from weapon name keywords
     */
    private WeaponInfo.Style detectStyleFromName(String weaponName) {
        if (weaponName == null || weaponName.isEmpty()) {
            return WeaponInfo.Style.UNKNOWN;
        }

        String lower = weaponName.toLowerCase();

        if (containsAny(lower, "staff", "wand", "trident", "sceptre", "shadow", "tumeken")) {
            return WeaponInfo.Style.MAGIC;
        }

        if (containsAny(lower, "bow", "dart", "knife", "crossbow", "ballista", "throwing", "chinchompa", "javelin")) {
            return WeaponInfo.Style.RANGE;
        }

        // Default to melee for any other weapon
        return WeaponInfo.Style.MELEE;
    }

    /**
     * Helper method to check if string contains any of the given substrings
     */
    private boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}