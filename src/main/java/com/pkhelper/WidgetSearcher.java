package com.pkhelper;

import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;

/**
 * Utility class for searching widgets
 */
public class WidgetSearcher {

    /**
     * Find a widget by sprite ID
     */
    public static Widget findWidgetBySprite(Client client, int spriteId) {
        for (Widget root : client.getWidgetRoots()) {
            if (root == null) {
                continue;
            }
            Widget found = findWidgetRecursive(root, spriteId);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    /**
     * Recursively search for a widget with the given sprite ID
     */
    private static Widget findWidgetRecursive(Widget widget, int spriteId) {
        if (widget == null) {
            return null;
        }

        if (widget.getSpriteId() == spriteId) {
            return widget;
        }

        // Search dynamic children
        Widget found = searchChildren(widget.getDynamicChildren(), spriteId);
        if (found != null) {
            return found;
        }

        // Search static children
        found = searchChildren(widget.getStaticChildren(), spriteId);
        if (found != null) {
            return found;
        }

        // Search nested children
        return searchChildren(widget.getNestedChildren(), spriteId);
    }

    /**
     * Search through an array of child widgets
     */
    private static Widget searchChildren(Widget[] children, int spriteId) {
        if (children == null) {
            return null;
        }

        for (Widget child : children) {
            Widget found = findWidgetRecursive(child, spriteId);
            if (found != null) {
                return found;
            }
        }

        return null;
    }
}