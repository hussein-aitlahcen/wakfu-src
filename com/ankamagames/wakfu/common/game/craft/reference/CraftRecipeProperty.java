package com.ankamagames.wakfu.common.game.craft.reference;

import com.ankamagames.framework.external.*;
import org.jetbrains.annotations.*;

public enum CraftRecipeProperty implements ExportableEnum
{
    SECRET(0, "Recette secr\u00e8te"), 
    AUTO_CONSUME_ITEM(1, "Execute les actions sur l'item produit");
    
    private final int m_id;
    private final String m_description;
    
    private CraftRecipeProperty(final int id, final String description) {
        this.m_id = id;
        this.m_description = description;
    }
    
    @Nullable
    public static CraftRecipeProperty getProperty(final int id) {
        final CraftRecipeProperty[] props = values();
        for (int i = 0; i < props.length; ++i) {
            final CraftRecipeProperty prop = props[i];
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
