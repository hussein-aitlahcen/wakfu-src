package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.game.fighter.*;

public class TimelineCellView extends ImmutableFieldProvider implements CharacteristicUpdateListener
{
    public static final String CHARAC_VALUE = "characValue";
    public static final String CHARAC_MAX_VALUE = "characMaxValue";
    public static final String ICON_URL = "iconUrl";
    public static final String IS_HP = "isHp";
    private static final String[] FIELDS;
    private final FighterCharacteristic m_charac;
    
    public TimelineCellView(final FighterCharacteristic charac) {
        super();
        (this.m_charac = charac).addListener(this);
    }
    
    @Override
    public String[] getFields() {
        return TimelineCellView.FIELDS;
    }
    
    @Override
    public void onCharacteristicUpdated(final AbstractCharacteristic charac) {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "characMaxValue");
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "characValue");
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("characValue")) {
            return this.m_charac.value();
        }
        if (fieldName.equals("characMaxValue")) {
            return this.m_charac.max();
        }
        if (fieldName.equals("isHp")) {
            return this.m_charac.getType() == FighterCharacteristicType.AREA_HP || this.m_charac.getType() == FighterCharacteristicType.HP;
        }
        if (fieldName.equals("iconUrl")) {
            return null;
        }
        return null;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TimelineCellView");
        sb.append("{m_charac=").append(this.m_charac);
        sb.append('}');
        return sb.toString();
    }
    
    static {
        FIELDS = new String[] { "characValue", "iconUrl" };
    }
}
