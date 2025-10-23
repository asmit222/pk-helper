
package com.pkhelper;

import java.util.HashMap;
import java.util.Map;

public class WeaponInfo {
    public enum Style { MELEE, RANGE, MAGIC, UNKNOWN }

    public static final Map<Integer, Style> WEAPON_STYLE_MAP;

    static {
        WEAPON_STYLE_MAP = new HashMap<>();

        // ---- Melee Weapons ----
        WEAPON_STYLE_MAP.put(11802, Style.MELEE); // Armadyl godsword
        WEAPON_STYLE_MAP.put(11804, Style.MELEE); // Bandos godsword
        WEAPON_STYLE_MAP.put(11806, Style.MELEE); // Saradomin godsword
        WEAPON_STYLE_MAP.put(11808, Style.MELEE); // Zamorak godsword
        WEAPON_STYLE_MAP.put(13576, Style.MELEE); // Dragon warhammer
        WEAPON_STYLE_MAP.put(20784, Style.MELEE); // Dragon claws
        WEAPON_STYLE_MAP.put(1215,  Style.MELEE); // Dragon dagger
        WEAPON_STYLE_MAP.put(5698,  Style.MELEE); // Dragon dagger (p++)
        WEAPON_STYLE_MAP.put(4587,  Style.MELEE); // Dragon scimitar
        WEAPON_STYLE_MAP.put(13263, Style.MELEE); // Abyssal tentacle
        WEAPON_STYLE_MAP.put(4151,  Style.MELEE); // Abyssal whip
        WEAPON_STYLE_MAP.put(21009, Style.MELEE); // Elder maul
        WEAPON_STYLE_MAP.put(13265, Style.MELEE); // Abyssal bludgeon
        WEAPON_STYLE_MAP.put(21012, Style.MELEE); // Dragon sword
        WEAPON_STYLE_MAP.put(1305,  Style.MELEE); // Dragon longsword
        WEAPON_STYLE_MAP.put(3204,  Style.MELEE); // Granite maul
        WEAPON_STYLE_MAP.put(11838, Style.MELEE); // Saradomin's blessed sword
        WEAPON_STYLE_MAP.put(12771, Style.MELEE); // Ghrazi rapier
        WEAPON_STYLE_MAP.put(20557, Style.MELEE); // Scythe of vitur
        WEAPON_STYLE_MAP.put(13560, Style.MELEE); // LMS sword variant
        WEAPON_STYLE_MAP.put(13271, Style.MELEE); // Darklight
        WEAPON_STYLE_MAP.put(13652, Style.MELEE); // Dragon claws (corrupt)
        WEAPON_STYLE_MAP.put(13654, Style.MELEE); // Vesta's longsword
        WEAPON_STYLE_MAP.put(13656, Style.MELEE); // Vesta's spear
        WEAPON_STYLE_MAP.put(13658, Style.MELEE); // Statius's warhammer
        WEAPON_STYLE_MAP.put(22978, Style.MELEE); // Ursine chainmace
        WEAPON_STYLE_MAP.put(25736, Style.MELEE); // Soulreaper axe

        // ---- Ranged Weapons ----
        WEAPON_STYLE_MAP.put(11785, Style.RANGE); // Armadyl crossbow
        WEAPON_STYLE_MAP.put(12926, Style.RANGE); // Toxic blowpipe
        WEAPON_STYLE_MAP.put(21902, Style.RANGE); // Craw's bow
        WEAPON_STYLE_MAP.put(11235, Style.RANGE); // Dark bow
        WEAPON_STYLE_MAP.put(861,   Style.RANGE); // Magic shortbow
        WEAPON_STYLE_MAP.put(6724,  Style.RANGE); // Seercull
        WEAPON_STYLE_MAP.put(9705,  Style.RANGE); // Karil's crossbow
        WEAPON_STYLE_MAP.put(22323, Style.RANGE); // Webweaver bow
        WEAPON_STYLE_MAP.put(13550, Style.RANGE); // LMS bow variant
        WEAPON_STYLE_MAP.put(9185,  Style.RANGE); // Rune crossbow
        WEAPON_STYLE_MAP.put(21013, Style.RANGE); // Dragon hunter crossbow
        WEAPON_STYLE_MAP.put(11212, Style.RANGE); // Crystal bow
        WEAPON_STYLE_MAP.put(19481, Style.RANGE); // Heavy ballista
        WEAPON_STYLE_MAP.put(13552, Style.RANGE); // LMS crossbow variant
        WEAPON_STYLE_MAP.put(10156, Style.RANGE); // Twisted bow
        WEAPON_STYLE_MAP.put(20997, Style.RANGE); // Ballista
        WEAPON_STYLE_MAP.put(11230, Style.RANGE); // Black chinchompa
        WEAPON_STYLE_MAP.put(13650, Style.RANGE); // Morrigan's javelin
        WEAPON_STYLE_MAP.put(13660, Style.RANGE); // Morrigan's throwing axe
        WEAPON_STYLE_MAP.put(25865, Style.RANGE); // Venator bow
        WEAPON_STYLE_MAP.put(27187, Style.RANGE); // Eclipse atlatl

        // ---- Magic Weapons ----
        WEAPON_STYLE_MAP.put(4675,  Style.MAGIC); // Ancient staff
        WEAPON_STYLE_MAP.put(11905, Style.MAGIC); // Trident of the seas
        WEAPON_STYLE_MAP.put(12899, Style.MAGIC); // Trident of the swamp (toxic)
        WEAPON_STYLE_MAP.put(21006, Style.MAGIC); // Kodai wand
        WEAPON_STYLE_MAP.put(6914,  Style.MAGIC); // Master wand
        WEAPON_STYLE_MAP.put(8841,  Style.MAGIC); // Void knight mace
        WEAPON_STYLE_MAP.put(22288, Style.MAGIC); // Tumeken's shadow
        WEAPON_STYLE_MAP.put(13555, Style.MAGIC); // LMS staff variant
        WEAPON_STYLE_MAP.put(4678,  Style.MAGIC); // Ancient staff (ornament kit)
        WEAPON_STYLE_MAP.put(22207, Style.MAGIC); // Harmonised nightmare staff
        WEAPON_STYLE_MAP.put(21003, Style.MAGIC); // Elder wand
        WEAPON_STYLE_MAP.put(22296, Style.MAGIC); // Accursed sceptre
        WEAPON_STYLE_MAP.put(6916,  Style.MAGIC); // Ahrim's staff
        WEAPON_STYLE_MAP.put(21255, Style.MAGIC); // Thammaron's sceptre
        WEAPON_STYLE_MAP.put(12904, Style.MAGIC); // Toxic staff of the dead
        WEAPON_STYLE_MAP.put(11791, Style.MAGIC); // Staff of the dead
        WEAPON_STYLE_MAP.put(13652, Style.MAGIC); // Zuriel's staff
        WEAPON_STYLE_MAP.put(27275, Style.MAGIC); // Warped sceptre
    }

    public static Style getStyle(int weaponId) {
        return WEAPON_STYLE_MAP.getOrDefault(weaponId, Style.UNKNOWN);
    }

    public static String getRecommendedPrayer(Style style) {
        switch (style) {
            case MELEE:
                return "Protect from Melee";
            case RANGE:
                return "Protect from Missiles";
            case MAGIC:
                return "Protect from Magic";
            default:
                return "Unknown";
        }
    }
}