package com.ankamagames.wakfu.client.core.world.dynamicElement;

import com.ankamagames.baseImpl.graphics.game.DynamicElement.*;

public enum WakfuDynamicElementType implements DynamicElementType
{
    SIMPLE(1, "Animation simple") {
        @Override
        public DynamicElementTypeProvider createProvider() {
            return new SimpleDynamicElementTypeProvider();
        }
    }, 
    NATION(2, "Animation en fonction de la nation") {
        @Override
        public DynamicElementTypeProvider createProvider() {
            return new NationDynamicElementTypeProvider();
        }
    }, 
    HW_GUILD_ENTRY(3, "Prend les couleurs de la guilde li\u00e9 \u00e0 l'IE ") {
        @Override
        public DynamicElementTypeProvider createProvider() {
            return new HavenWorldEntryGuildDynamicElementTypeProvider();
        }
    }, 
    HW_GUILD(4, "Prend les couleurs de la guilde du HM") {
        @Override
        public DynamicElementTypeProvider createProvider() {
            return new HavenWorldGuildDynamicElementTypeProvider();
        }
    };
    
    private final int m_id;
    private final String m_description;
    
    private WakfuDynamicElementType(final int id, final String description) {
        this.m_id = id;
        this.m_description = description;
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
    
    public int getId() {
        return this.m_id;
    }
    
    public static WakfuDynamicElementType getFromId(final int typeId) {
        for (int i = 0; i < values().length; ++i) {
            final WakfuDynamicElementType type = values()[i];
            if (type.m_id == typeId) {
                return type;
            }
        }
        throw new UnsupportedOperationException("Impossible de trouver le type d'\u00e9l\u00e9ment Dynamique " + typeId);
    }
}
