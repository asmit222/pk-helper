package com.pkhelper;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for parsing configuration values
 */
@Slf4j
public class ConfigParser {

    /**
     * Parse comma-separated item IDs from config string
     */
    public static Set<Integer> parseItemIds(String input) {
        Set<Integer> ids = new HashSet<>();
        if (input == null || input.trim().isEmpty()) {
            return ids;
        }

        String[] parts = input.split(",");
        for (String part : parts) {
            try {
                String trimmed = part.trim();
                if (!trimmed.isEmpty()) {
                    int id = Integer.parseInt(trimmed);
                    ids.add(id);
                    log.debug("Parsed item ID: {}", id);
                }
            } catch (NumberFormatException e) {
                log.warn("Invalid item ID in config: '{}'", part);
            }
        }
        log.debug("Parsed {} item IDs from config: {}", ids.size(), ids);
        return ids;
    }
}