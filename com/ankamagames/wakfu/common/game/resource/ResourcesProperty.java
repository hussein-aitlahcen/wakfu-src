package com.ankamagames.wakfu.common.game.resource;

import com.ankamagames.framework.external.*;
import org.jetbrains.annotations.*;

public enum ResourcesProperty implements ExportableEnum
{
    AUTO_RESPAWN(0, "Ressource Auto-respawn"), 
    CHALLENGE_RESOURCE(1, "Ressource r\u00e9serv\u00e9e aux Challenges"), 
    DONT_SAVE(2, "Ressource qu'il ne faut pas sauvegarder"), 
    NO_NATION_LAWS(3, "Ressource non prisent en compte par les lois"), 
    CHAOS_RESOURCE(4, "Ressource r\u00e9serv\u00e9e aux Chaos");
    
    private final int m_id;
    private final String m_description;
    
    private ResourcesProperty(final int id, final String description) {
        this.m_id = id;
        this.m_description = description;
    }
    
    @Nullable
    public static ResourcesProperty getProperty(final int id) {
        final ResourcesProperty[] props = values();
        for (int i = 0; i < props.length; ++i) {
            final ResourcesProperty prop = props[i];
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
        return null;
    }
}
