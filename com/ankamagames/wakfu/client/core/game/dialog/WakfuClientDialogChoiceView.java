package com.ankamagames.wakfu.client.core.game.dialog;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;

public class WakfuClientDialogChoiceView implements FieldProvider
{
    public static final String TEXT_FIELD = "text";
    public static final String ICON_URL_FIELD = "iconUrl";
    public static final String VISIBLE_FIELD = "visible";
    private final int m_id;
    private final WakfuClientDialogChoiceType m_choiceType;
    private boolean m_visible;
    public static final String[] FIELDS;
    
    public WakfuClientDialogChoiceView(final int id, final WakfuClientDialogChoiceType choiceType) {
        super();
        this.m_visible = false;
        this.m_id = id;
        this.m_choiceType = choiceType;
    }
    
    @Override
    public String[] getFields() {
        return WakfuClientDialogChoiceView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("text")) {
            switch (this.m_id) {
                case -2: {
                    return "[...]";
                }
                case -1: {
                    return WakfuTranslator.getInstance().getString("dialogEnd");
                }
                default: {
                    return WakfuTranslator.getInstance().getString(76, this.m_id, new Object[0]);
                }
            }
        }
        else if (fieldName.equals("iconUrl")) {
            if (this.m_choiceType == WakfuClientDialogChoiceType.NONE) {
                return null;
            }
            return WakfuConfiguration.getInstance().getDialogChoiceIconUrl(this.m_choiceType.getId());
        }
        else {
            if (fieldName.equals("visible")) {
                return this.m_visible;
            }
            return null;
        }
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public void setVisible(final boolean visible) {
        this.m_visible = visible;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "visible");
    }
    
    static {
        FIELDS = new String[] { "text", "visible" };
    }
}
