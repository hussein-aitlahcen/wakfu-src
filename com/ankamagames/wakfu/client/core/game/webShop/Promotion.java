package com.ankamagames.wakfu.client.core.game.webShop;

import com.ankamagames.wakfu.client.ui.component.*;
import org.jetbrains.annotations.*;

public class Promotion extends ImmutableFieldProvider
{
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    private final String m_name;
    private final String m_description;
    
    public Promotion(final String name, final String description) {
        super();
        this.m_name = name;
        this.m_description = description;
    }
    
    @Override
    public String[] getFields() {
        return Promotion.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.m_name;
        }
        if (fieldName.equals("description")) {
            return this.m_description;
        }
        return null;
    }
}
