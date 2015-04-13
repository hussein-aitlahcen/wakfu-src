package com.ankamagames.framework.fileFormat.rss;

public class RSSItem
{
    private String m_title;
    private String m_link;
    private String m_description;
    private RSSImage m_image;
    
    public RSSItem(final String title, final String link, final String description) {
        super();
        this.m_title = title;
        this.m_link = link;
        this.m_description = description;
    }
    
    public String getTitle() {
        return this.m_title;
    }
    
    public void setTitle(final String title) {
        this.m_title = title;
    }
    
    public String getLink() {
        return this.m_link;
    }
    
    public void setLink(final String link) {
        this.m_link = link;
    }
    
    public RSSImage getImage() {
        return this.m_image;
    }
    
    public void setImage(final RSSImage image) {
        this.m_image = image;
    }
    
    public String getDescription() {
        return this.m_description;
    }
    
    public void setDescription(final String description) {
        this.m_description = description;
    }
}
