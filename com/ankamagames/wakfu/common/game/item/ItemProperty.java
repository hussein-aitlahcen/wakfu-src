package com.ankamagames.wakfu.common.game.item;

import com.ankamagames.framework.external.*;
import org.jetbrains.annotations.*;

public enum ItemProperty implements ExportableEnum
{
    CHALLENGE(0, "Objet de challenge"), 
    TREASURE(1, "Objet tr\u00e9sor (interface sp\u00e9ciale)"), 
    QUEST(2, "Objet de qu\u00eate"), 
    GUILD_COLOR(3, "Prend la couleur de la guilde"), 
    NO_ENU_DROP(4, "Ne peut pas \u00eatre drop\u00e9 par la bourse enutrof"), 
    BOOK(5, "Livre qui peut \u00eatre plac\u00e9 dans une biblioth\u00e8que"), 
    NO_FEEDBACK(6, "Cet item ne demande pas les feedbacks sur les actions qu'il d\u00e9clenche (plantation)"), 
    SHOP_ITEM(7, "Item propos\u00e9 uniquement au shop"), 
    EXCLUSIVE_EQUIPMENT_ITEM(8, "[Relique] Il ne peut y avoir qu'un seul Item ayant cette propri\u00e9t\u00e9 \u00e9quip\u00e9 \u00e0 la fois"), 
    CREATE_EVENT_ON_DROP(9, "Au drop de l'item, un \u00e9v\u00e8nement de jeu est cr\u00e9\u00e9 (pour les qu\u00eates et les scenarios)"), 
    NO_BONUS_LOOT_TRY(10, "Pas de jet bonus de loot pour cet item"), 
    ADMIN_XP(11, "1 xp = 1 level, level max = 32000 et ne gagne de l'xp uniquement via la commande admin"), 
    EXCLUSIVE_EQUIPMENT_ITEM_2(12, "[Relique2] Il ne peut y avoir qu'un seul Item ayant cette propri\u00e9t\u00e9 \u00e9quip\u00e9 \u00e0 la fois"), 
    NOT_RECYCLABLE(13, "L'objet ne peut pas \u00eatre recycl\u00e9"), 
    DONT_CONSUME_ON_USE(14, "L'objet n'est pas consomm\u00e9 \u00e0 l'utilisation (avec effets uniquement)");
    
    private final int m_id;
    private final String m_description;
    
    private ItemProperty(final int id, final String description) {
        this.m_id = id;
        this.m_description = description;
    }
    
    @Nullable
    public static ItemProperty getProperty(final int id) {
        final ItemProperty[] props = values();
        for (int i = 0, length = props.length; i < length; ++i) {
            final ItemProperty prop = props[i];
            if (prop.m_id == id) {
                return prop;
            }
        }
        return null;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_description;
    }
    
    @Override
    public String getEnumComment() {
        return this.m_description;
    }
}
