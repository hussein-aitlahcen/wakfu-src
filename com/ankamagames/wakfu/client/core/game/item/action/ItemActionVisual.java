package com.ankamagames.wakfu.client.core.game.item.action;

import com.ankamagames.framework.external.*;

public enum ItemActionVisual implements ExportableEnum
{
    USE((byte)0, "hand", "desc.mru.use"), 
    SPLIT((byte)1, "splitItem", "desc.splitItem"), 
    ADD_COMPASS((byte)2, "hand", "desc.addCompass");
    
    private final byte m_id;
    private final String m_style;
    private final String m_translationKey;
    
    private ItemActionVisual(final byte id, final String style, final String translationKey) {
        this.m_id = id;
        this.m_style = style;
        this.m_translationKey = translationKey;
    }
    
    public static ItemActionVisual getFromId(final byte id) {
        for (final ItemActionVisual visual : values()) {
            if (visual.m_id == id) {
                return visual;
            }
        }
        return null;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public String getStyle() {
        return this.m_style;
    }
    
    public String getTranslationKey() {
        return this.m_translationKey;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.name();
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
}
