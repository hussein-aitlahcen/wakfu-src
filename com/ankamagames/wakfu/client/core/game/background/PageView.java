package com.ankamagames.wakfu.client.core.game.background;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.*;

public class PageView implements FieldProvider
{
    public static final String TEMPLATE_LAYOUT_ID_FIELD = "templateLayoutId";
    public static final String IMAGE_URL_FIELD = "imageUrl";
    public static final String TEXT1_FIELD = "text1";
    public static final String TEXT2_FIELD = "text2";
    public static final String[] FIELDS;
    private final int m_id;
    private final short m_index;
    private final int m_gfxId;
    private final short m_templateLayoutId;
    private final String m_text1;
    private final String m_text2;
    
    public PageView(final int id, final short index, final int gfxId, final short templateLayoutId) {
        super();
        this.m_id = id;
        this.m_index = index;
        this.m_gfxId = gfxId;
        this.m_templateLayoutId = templateLayoutId;
        final String textBase = WakfuTranslator.getInstance().getString(67, id, new Object[0]);
        final String[] strings = textBase.split("\\#{2,}");
        this.m_text1 = strings[0];
        this.m_text2 = ((strings.length > 1) ? strings[1] : "");
    }
    
    @Override
    public String[] getFields() {
        return PageView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("templateLayoutId")) {
            return this.m_templateLayoutId;
        }
        if (fieldName.equals("imageUrl")) {
            return WakfuConfiguration.getInstance().getDisplayBackgroundImage(this.m_gfxId);
        }
        if (fieldName.equals("text1")) {
            return this.m_text1;
        }
        if (fieldName.equals("text2")) {
            return this.m_text2;
        }
        return null;
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
    
    static {
        FIELDS = new String[] { "templateLayoutId", "imageUrl", "text1", "text2" };
    }
}
