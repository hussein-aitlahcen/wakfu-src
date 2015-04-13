package com.ankamagames.wakfu.client.core.game.item;

import com.ankamagames.framework.external.*;

public enum ItemWorldUsageTarget implements ExportableEnum
{
    AUTO_USE((byte)0, "Utilisation sur le lanceur uniquement"), 
    WORLD((byte)1, "Utilisable sur une cellule du monde (port\u00e9e infinie)"), 
    DISTANCE((byte)2, "Utilisable sur une cellule du monde (avec port\u00e9e min max)");
    
    public final byte m_id;
    private final String m_label;
    
    private ItemWorldUsageTarget(final byte id, final String label) {
        this.m_id = id;
        this.m_label = label;
    }
    
    @Override
    public String getEnumId() {
        return Integer.toString(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_label;
    }
    
    @Override
    public String getEnumComment() {
        return this.m_label;
    }
    
    public static ItemWorldUsageTarget getFromId(final byte id) {
        for (final ItemWorldUsageTarget usage : values()) {
            if (usage.m_id == id) {
                return usage;
            }
        }
        return null;
    }
}
