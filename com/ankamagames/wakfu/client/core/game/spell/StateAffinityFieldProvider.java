package com.ankamagames.wakfu.client.core.game.spell;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.game.spell.*;

public class StateAffinityFieldProvider extends ImmutableFieldProvider
{
    public static final String STATE_FIELD = "state";
    public static final String VALUE_FIELD = "value";
    private short m_stateId;
    private int m_value;
    
    public StateAffinityFieldProvider(final short stateId, final int value) {
        super();
        this.m_stateId = stateId;
        this.m_value = value;
    }
    
    public void setValue(final int value) {
        this.m_value = value;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "value");
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("state")) {
            return StateManager.getInstance().getState(this.m_stateId);
        }
        if (fieldName.equals("value")) {
            final StringBuilder sb = new StringBuilder();
            if (this.m_value >= 0) {
                sb.append("+");
            }
            sb.append(this.m_value).append("%");
            return sb.toString();
        }
        return null;
    }
    
    public enum StateAffinityBonusType
    {
        APPLICATION, 
        RESISTANCE;
    }
}
