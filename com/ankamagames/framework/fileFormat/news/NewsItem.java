package com.ankamagames.framework.fileFormat.news;

import org.apache.log4j.*;
import java.net.*;
import java.util.*;
import org.jetbrains.annotations.*;

public class NewsItem
{
    public static final Logger m_logger;
    private final String m_guid;
    private Date m_publicationDate;
    private String m_title;
    private String description;
    private URL m_link;
    private final EnumSet<NewsContext> m_contexts;
    private int m_priority;
    private NewsElementBackground m_background;
    private List<NewsElement> m_elements;
    
    public NewsItem(final String guid) {
        super();
        this.description = null;
        this.m_link = null;
        this.m_contexts = EnumSet.noneOf(NewsContext.class);
        this.m_priority = 0;
        this.m_elements = new ArrayList<NewsElement>();
        this.m_guid = guid;
    }
    
    public String getGuid() {
        return this.m_guid;
    }
    
    public Date getPublicationDate() {
        return new Date(this.m_publicationDate.getTime());
    }
    
    public String getTitle() {
        return this.m_title;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public URL getLink() {
        return this.m_link;
    }
    
    public int getPriority() {
        return this.m_priority;
    }
    
    public NewsElementBackground getBackground() {
        return this.m_background;
    }
    
    public List<NewsElement> getElements() {
        return this.m_elements;
    }
    
    public <T extends NewsElement> List<T> getElements(final Class<T> classType) {
        final ArrayList<T> list = new ArrayList<T>();
        for (int i = 0; i < this.m_elements.size(); ++i) {
            final NewsElement element = this.m_elements.get(i);
            if (element.getClass() == classType) {
                list.add((T)element);
            }
        }
        return list;
    }
    
    void setPublicationDate(final Date publicationDate) {
        this.m_publicationDate = publicationDate;
    }
    
    void setTitle(final String title) {
        this.m_title = title;
    }
    
    void setDescription(final String description) {
        this.description = description;
    }
    
    void setLink(final URL link) {
        this.m_link = link;
    }
    
    void setPriority(final int priority) {
        this.m_priority = priority;
    }
    
    void addContext(final NewsContext context) {
        this.m_contexts.add(context);
    }
    
    void addElement(@NotNull final NewsElement element) {
        this.m_elements.add(element);
    }
    
    void setBackground(final NewsElementBackground background) {
        this.m_background = background;
    }
    
    @Override
    public String toString() {
        return "{NewsItem guid=" + this.m_guid + " contexts:" + this.m_contexts.size() + " pubDate=" + this.m_publicationDate + " priority=" + this.m_priority + " title=" + this.m_title + " link=" + this.m_link + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)NewsItem.class);
    }
}
