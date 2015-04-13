package com.ankamagames.framework.fileFormat.rss;

public class RSSImage
{
    private String m_url;
    private String m_title;
    private String m_link;
    
    public RSSImage(final String url, final String title, final String link) {
        super();
        this.m_url = url;
        this.m_title = title;
        this.m_link = link;
    }
    
    public String getUrl() {
        return this.m_url;
    }
    
    public void setUrl(final String url) {
        this.m_url = url;
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
}
