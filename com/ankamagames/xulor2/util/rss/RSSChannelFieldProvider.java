package com.ankamagames.xulor2.util.rss;

import com.ankamagames.framework.reflect.*;
import java.util.*;
import com.ankamagames.framework.fileFormat.rss.*;

public class RSSChannelFieldProvider implements FieldProvider
{
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String LINK = "link";
    public static final String IMAGE = "image";
    public static final String ITEMS = "items";
    private RSSChannel m_channel;
    private RSSImageFieldProvider m_image;
    private final ArrayList<RSSItemFieldProvider> m_items;
    
    public RSSChannelFieldProvider(final RSSChannel channel) {
        super();
        this.m_items = new ArrayList<RSSItemFieldProvider>();
        this.m_channel = channel;
        if (this.m_channel.getImage() != null) {
            this.m_image = new RSSImageFieldProvider(this.m_channel.getImage());
        }
        final ArrayList<RSSItem> items = this.m_channel.getItems();
        for (int i = 0, size = items.size(); i < size; ++i) {
            this.m_items.add(new RSSItemFieldProvider(items.get(i)));
        }
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("title")) {
            return RSSUtils.HTMLLinkToTextViewLink(this.m_channel.getLink(), this.m_channel.getTitle());
        }
        if (fieldName.equals("description")) {
            return RSSUtils.HTML2Text(this.m_channel.getDescription());
        }
        if (fieldName.equals("link")) {
            return RSSUtils.HTMLLinkToTextViewLink(this.m_channel.getLink());
        }
        if (fieldName.equals("image")) {
            return this.m_image;
        }
        if (fieldName.equals("items")) {
            return this.m_items;
        }
        return null;
    }
    
    public RSSChannel getChannel() {
        return this.m_channel;
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
