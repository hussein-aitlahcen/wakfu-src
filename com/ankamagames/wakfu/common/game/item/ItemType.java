package com.ankamagames.wakfu.common.game.item;

import com.ankamagames.framework.external.*;
import com.ankamagames.framework.graphics.image.*;
import java.util.*;

public enum ItemType implements ExportableEnum
{
    CLASSIC(0, "Classique (pas de contour)", Color.WHITE_ALPHA, 1), 
    RELIC(1, "Relique (Cuivre)", new Color(0.89f, 0.45f, 0.175f, 1.0f), 2), 
    WAKFU(2, "Wakfu (Bleu Wakfu)", new Color(0.24f, 0.9f, 0.9f, 1.0f), 3), 
    STASIS(3, "Stasis (Violet Stasis)", new Color(0.5f, 0.0f, 0.9f, 0.72f), 4), 
    EVENT(4, "Event (Violet)", new Color(1.0f, 0.6f, 0.6f, 1.0f), 5);
    
    private final int m_id;
    private final String m_label;
    private final byte m_sortOrder;
    private Color m_color;
    private static final Comparator<ItemType> TYPE_COMPARATOR;
    
    private ItemType(final int id, final String label, final Color color, final int sortOrder) {
        this.m_id = id;
        this.m_label = label;
        this.m_color = color;
        this.m_sortOrder = (byte)sortOrder;
    }
    
    @Override
    public String getEnumId() {
        return this.toString();
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
    
    public static Comparator<ItemType> getComparator() {
        return ItemType.TYPE_COMPARATOR;
    }
    
    static {
        TYPE_COMPARATOR = new Comparator<ItemType>() {
            @Override
            public int compare(final ItemType left, final ItemType right) {
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
