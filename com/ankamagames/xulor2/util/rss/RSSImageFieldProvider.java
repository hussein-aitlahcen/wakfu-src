package com.ankamagames.xulor2.util.rss;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.framework.fileFormat.rss.*;

public class RSSImageFieldProvider implements FieldProvider
{
    public static final String URL = "url";
    public static final String TITLE = "title";
    public static final String LINK = "link";
    private final RSSImage m_image;
    
    public RSSImageFieldProvider(final RSSImage image) {
        super();
        this.m_image = image;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("url")) {
            return this.m_image.getUrl();
        }
        if (fieldName.equals("title")) {
            return this.m_image.getTitle();
        }
        if (fieldName.equals("link")) {
            return RSSUtils.HTMLLinkToTextViewLink(this.m_image.getLink());
        }
        return null;
    }
    
    public RSSImage getImage() {
        return this.m_image;
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
