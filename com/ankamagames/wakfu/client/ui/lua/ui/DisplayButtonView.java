package com.ankamagames.wakfu.client.ui.lua.ui;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.*;
import org.jetbrains.annotations.*;

public class DisplayButtonView extends ImmutableFieldProvider
{
    private final String m_tradKey;
    public static final String TEXT = "text";
    
    public DisplayButtonView(@NotNull final String tradKey) {
        super();
        this.m_tradKey = tradKey;
    }
    
    @Override
    public String[] getFields() {
        return DisplayButtonView.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if ("text".equals(fieldName)) {
            return WakfuTranslator.getInstance().getString(this.m_tradKey);
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "DisplayButtonView{m_tradKey='" + this.m_tradKey + '\'' + '}';
    }
}
