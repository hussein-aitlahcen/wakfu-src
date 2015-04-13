package com.ankamagames.framework.fileFormat.rss;

import java.util.*;

public class RSSChannel
{
    private String m_title;
    private String m_link;
    private String m_description;
    private RSSImage m_image;
    private final ArrayList<RSSItem> m_items;
    
    public RSSChannel(final String title, final String link, final String description) {
        super();
        this.m_items = new ArrayList<RSSItem>();
        this.m_title = title;
        this.m_link = link;
        this.m_description = description;
    }
    
    public void setTitle(final String title) {
        this.m_title = title;
    }
    
    public void setLink(final String link) {
        this.m_link = link;
    }
    
    public void setDescription(final String description) {
        this.m_description = description;
    }
    
    public String getTitle() {
        return this.m_title;
    }
    
    public String getLink() {
        return this.m_link;
    }
    
    public String getDescription() {
        return this.m_description;
    }
    
    public void addItem(final RSSItem item) {
        this.m_items.add(item);
    }
    
    public ArrayList<RSSItem> getItems() {
        return this.m_items;
    }
    
    public RSSImage getImage() {
        return this.m_image;
    }
    
    public void setImage(final RSSImage image) {
        this.m_image = image;
    }
}
