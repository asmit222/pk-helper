package com.pkhelper;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.game.ItemManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

/**
 * Handles detection of item combat styles based on ID, name, and custom configurations
 */
@Slf4j
@Singleton
public class ItemStyleDetector {
    private final ItemManager itemManager;

    // Magic item keywords
    private static final String[] MAGIC_KEYWORDS = {
            "staff", "wand", "trident", "sceptre", "shadow", "mystic",
            "ahrims", "ahrim's", "ancestral", "wizard", "infinity", "bloodbark",
            "dagon", "eternal", "occult", "tome of fire", "tome of water",
            "mages book", "mage's book", "virtus", "elidinis", "harmonised",
            "nightmare staff", "tumeken"
    };

    // Ranged item keywords
    private static final String[] RANGED_KEYWORDS = {
            "bow", "crossbow", "ballista", "blowpipe", "dart", "knife",
            "javelin", "throwing", "chinchompa", "dhide", "d'hide", "karil",
            "armadyl chestplate", "armadyl chainskirt", "ranger", "anguish",
            "accumulator", "ava", "crystal body", "crystal legs",
            "blessed d", "blessed coif", "masori", "zaryte vamb"
    };

    // Melee item keywords
    private static final String[] MELEE_KEYWORDS = {
            "scimitar", "whip", "godsword", "dagger", "longsword", "sword",
            "maul", "mace", "claws", "rapier", "scythe", "halberd",
            "axe", "warhammer", "platebody", "platelegs", "chainbody", "full helm",
            "berserker", "warrior", "torso", "fighter", "dragon defender", "avernic",
            "amulet of torture", "amulet of strength", "bandos", "torva",
            "justiciar", "inquisitor", "obsidian platebody", "obsidian platelegs",
            "ferocious gloves", "barrows gloves"
    };

    @Inject
    public ItemStyleDetector(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    /**
     * Detect item style with custom IDs, WeaponInfo map, and name-based fallback
     */
    public WeaponInfo.Style detectStyle(int itemId, Set<Integer> customMagicIds,
                                        Set<Integer> customRangedIds, Set<Integer> customMeleeIds) {
        // Step 1: Check custom IDs first
        if (customMagicIds.contains(itemId)) {
            return WeaponInfo.Style.MAGIC;
        }
        if (customRangedIds.contains(itemId)) {
            return WeaponInfo.Style.RANGE;
        }
        if (customMeleeIds.contains(itemId)) {
            return WeaponInfo.Style.MELEE;
        }

        // Step 2: Check WeaponInfo map
        WeaponInfo.Style weaponStyle = WeaponInfo.getStyle(itemId);
        if (weaponStyle != WeaponInfo.Style.UNKNOWN) {
            return weaponStyle;
        }

        // Step 3: Name-based detection
        return detectStyleByName(itemId);
    }

    /**
     * Detect style based on item name keywords
     */
    private WeaponInfo.Style detectStyleByName(int itemId) {
        try {
            String itemName = itemManager.getItemComposition(itemId).getName().toLowerCase();

            if (containsAny(itemName, MAGIC_KEYWORDS)) {
                return WeaponInfo.Style.MAGIC;
            }

            if (containsAny(itemName, RANGED_KEYWORDS)) {
                return WeaponInfo.Style.RANGE;
            }

            if (containsAny(itemName, MELEE_KEYWORDS)) {
                return WeaponInfo.Style.MELEE;
            }

        } catch (Exception e) {
            log.debug("Failed to detect style for item ID {}", itemId, e);
        }

        return WeaponInfo.Style.UNKNOWN;
    }

    /**
     * Check if text contains any of the keywords
     */
    private boolean containsAny(String text, String[] keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}