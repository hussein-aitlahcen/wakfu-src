package com.ankamagames.xulor2.util.rss;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.framework.fileFormat.rss.*;

public class RSSItemFieldProvider implements FieldProvider
{
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String LINK = "link";
    public static final String IMAGE = "image";
    private final RSSItem m_item;
    private RSSImageFieldProvider m_image;
    
    public RSSItemFieldProvider(final RSSItem item) {
        super();
        this.m_image = null;
        this.m_item = item;
        if (item.getImage() != null) {
            this.m_image = new RSSImageFieldProvider(item.getImage());
        }
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("title")) {
            return this.m_item.getTitle();
        }
        if (fieldName.equals("description")) {
            return RSSUtils.HTML2Text(this.m_item.getDescription());
        }
        if (fieldName.equals("link")) {
            return RSSUtils.HTMLLinkToTextViewLink(this.m_item.getLink());
        }
        if (fieldName.equals("image")) {
            return this.m_image;
        }
        return null;
    }
    
    public RSSItem getItem() {
        return this.m_item;
    }
    
    @Override
    public String[] getFields() {
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
}
