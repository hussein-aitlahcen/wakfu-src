package com.ankamagames.wakfu.client.core.game.dungeon;

import com.ankamagames.wakfu.client.ui.component.*;
import org.jetbrains.annotations.*;

public class DungeonFilterTypeView extends ImmutableFieldProvider
{
    public static final String NAME = "name";
    public static final String VALUE = "value";
    private final DungeonFilterType m_type;
    
    public DungeonFilterTypeView(final DungeonFilterType type) {
        super();
        this.m_type = type;
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.m_type.getName();
        }
        if (fieldName.equals("value")) {
            return this.m_type;
        }
        return null;
    }
    
    public DungeonFilterType getType() {
        return this.m_type;
    }
}
