package com.ankamagames.framework.fileFormat.news;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.translator.*;
import java.util.concurrent.atomic.*;
import org.jetbrains.annotations.*;
import java.util.*;

public class NewsChannel
{
    public static final Logger m_logger;
    private final Language m_language;
    private final Date m_publicationDate;
    private final TreeSet<NewsItem> m_items;
    private final AtomicInteger m_imagesToLoadCount;
    
    NewsChannel(@NotNull final Language language, @Nullable final Date publicationDate) {
        super();
        this.m_language = language;
        this.m_publicationDate = new Date(publicationDate.getTime());
        this.m_items = new TreeSet<NewsItem>(NewsComparatorByPriority.INSTANCE);
        this.m_imagesToLoadCount = new AtomicInteger(0);
    }
    
    @NotNull
    public Language getLanguage() {
        return this.m_language;
    }
    
    @Nullable
    public Date getPublicationDate() {
        return this.m_publicationDate;
    }
    
    public int getNotLoadedImagesCount() {
        return this.m_imagesToLoadCount.get();
    }
    
    public Collection<NewsItem> getItems() {
        return this.m_items;
    }
    
    void addItem(final NewsItem item) {
        this.m_items.add(item);
    }
    
    void setImagesToLoadCount(final int imagesToLoadCount) {
        this.m_imagesToLoadCount.set(imagesToLoadCount);
    }
    
    int decrementImagesToLoadCount() {
        return this.m_imagesToLoadCount.decrementAndGet();
    }
    
    @Override
    public String toString() {
        return new StringBuffer().append("{NewsChannel lang=").append(this.m_language.getActualLocale()).append(" pubDate=").append(this.m_publicationDate).append(" items count=").append(this.m_items.size()).append('}').toString();
    }
    
    static {
        m_logger = Logger.getLogger((Class)NewsChannel.class);
    }
}
