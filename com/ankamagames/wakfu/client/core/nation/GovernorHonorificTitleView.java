package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.*;

public class GovernorHonorificTitleView extends ImmutableFieldProvider
{
    public static final String TITLE_TEXT = "titleText";
    public final String[] FIELDS;
    private final GovernorHonorificTitle m_title;
    
    public GovernorHonorificTitleView(final GovernorHonorificTitle title) {
        super();
        this.FIELDS = new String[] { "titleText" };
        this.m_title = title;
    }
    
    @Override
    public String[] getFields() {
        return new String[0];
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("titleText")) {
            return (this.m_title == null) ? null : WakfuTranslator.getInstance().getString(this.m_title.getTranslatorKey());
        }
        return null;
    }
    
    public GovernorHonorificTitle getTitle() {
        return this.m_title;
    }
    
    public short getTitleId() {
        return (short)((this.m_title == null) ? -1 : this.m_title.getId());
    }
}
