package com.ankamagames.wakfu.client.ui.component;

import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class TimelineTurnFieldProvider extends ImmutableFieldProvider
{
    public static final String TURN_FIELD = "turn";
    private int m_turn;
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("timelineElementType")) {
            return 2;
        }
        if (fieldName.equals("turn")) {
            return this.m_turn;
        }
        return null;
    }
    
    public int getTurn() {
        return this.m_turn;
    }
    
    public void setTurn(final int turn) {
        this.m_turn = turn;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "turn");
    }
}
