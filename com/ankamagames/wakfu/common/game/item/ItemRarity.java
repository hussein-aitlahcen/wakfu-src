package com.ankamagames.wakfu.common.game.item;

import com.ankamagames.framework.external.*;
import com.ankamagames.framework.graphics.image.*;
import java.util.*;
import org.apache.commons.lang3.*;

public enum ItemRarity implements ExportableEnum
{
    COMMON(0, "Qualit\u00e9 commune (gris)", new Color(0.65f, 0.67f, 0.63f, 1.0f), 1), 
    UNUSUAL(1, "Inhabituel (blanc)", Color.WHITE, 2), 
    RARE(2, "Qualit\u00e9 rare (vert)", new Color(0.0f, 0.75f, 0.0f, 1.0f), 3), 
    MYTHIC(3, "Mythique (orange)", new Color(1.0f, 0.6f, 0.0f, 1.0f), 4), 
    LEGENDARY(4, "L\u00e9gendaire (jaune)", new Color(1.0f, 1.0f, 0.5f, 1.0f), 5), 
    RELIC(5, "Relique (violet)", new Color(0.81f, 0.45f, 0.95f, 1.0f), 6), 
    PVP(6, "Pvp (turquoise)", new Color(0.58f, 0.94f, 0.96f, 1.0f), 8), 
    RELIC2(7, "Relique2 (rose)", new Color(1.0f, 0.65f, 0.84f, 1.0f), 7), 
    ADMIN(10, "Administrateur (rouge)", Color.RED, 10);
    
    private final int m_id;
    private final String m_label;
    private final byte m_sortOrder;
    private final Color m_color;
    private static final Comparator<ItemRarity> DESCENDING_RARITY_COMPARATOR;
    
    private ItemRarity(final int id, final String label, final Color color, final int sortOrder) {
        this.m_id = id;
        this.m_label = label;
        this.m_color = color;
        this.m_sortOrder = (byte)sortOrder;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_label;
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
    
    public Color getColor() {
        return this.m_color;
    }
    
    public byte getSortOrder() {
        return this.m_sortOrder;
    }
    
    public static Comparator<ItemRarity> getDescendingRarityComparator() {
        return ItemRarity.DESCENDING_RARITY_COMPARATOR;
    }
    
    public ItemRarity getNextRarity() {
        final ItemRarity[] values = values();
        Arrays.sort(values, getDescendingRarityComparator());
        final int index = ArrayUtils.indexOf(values, this);
        return (index == 0) ? null : values[index - 1];
    }
    
    public static ItemRarity fromId(final short rarity) {
        for (final ItemRarity r : values()) {
            if (r.m_id == rarity) {
                return r;
            }
        }
        return ItemRarity.COMMON;
    }
    
    static {
        DESCENDING_RARITY_COMPARATOR = new Comparator<ItemRarity>() {
            @Override
            public int compare(final ItemRarity left, final ItemRarity right) {
                if (left == right) {
                    return 0;
                }
                if (left == null) {
                    return 1;
                }
                if (right == null) {
                    return -1;
                }
                return Integer.signum(right.m_sortOrder - left.m_sortOrder);
            }
        };
    }
}
