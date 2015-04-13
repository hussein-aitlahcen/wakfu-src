package com.ankamagames.wakfu.client.moderationNew.panel;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.constants.*;
import org.apache.commons.lang3.*;
import com.ankamagames.wakfu.client.core.*;
import org.jetbrains.annotations.*;

public class ModerationSanctionView extends ImmutableFieldProvider
{
    public static final String TYPE = "type";
    public static final String ID = "id";
    public static final String TEXT = "sanctionText";
    private final ModerationSanction m_constant;
    
    public ModerationSanctionView(final ModerationSanction constant) {
        super();
        this.m_constant = constant;
    }
    
    @Override
    public String[] getFields() {
        return ArrayUtils.EMPTY_STRING_ARRAY;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if ("type".equals(fieldName)) {
            return this.m_constant.getSanctionType();
        }
        if ("sanctionText".equals(fieldName)) {
            return WakfuTranslator.getInstance().getString(String.format("moderationPanel.sanction%d", this.m_constant.getId()));
        }
        if ("id".equals(fieldName)) {
            return this.m_constant.getId();
        }
        return null;
    }
}
