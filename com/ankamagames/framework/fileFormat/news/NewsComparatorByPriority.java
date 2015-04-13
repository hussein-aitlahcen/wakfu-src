package com.ankamagames.framework.fileFormat.news;

import org.apache.log4j.*;
import java.util.*;

public class NewsComparatorByPriority implements Comparator<NewsItem>
{
    public static final Logger m_logger;
    public static final NewsComparatorByPriority INSTANCE;
    
    @Override
    public int compare(final NewsItem newsItem1, final NewsItem newsItem2) {
        if (newsItem1 == newsItem2) {
            return 0;
        }
        final int prio1 = newsItem1.getPriority();
        final int prio2 = newsItem2.getPriority();
        if (prio1 != prio2) {
            return prio1 - prio2;
        }
        final Date date1 = newsItem1.getPublicationDate();
        final Date date2 = newsItem2.getPublicationDate();
        if (!date1.equals(date2)) {
            if (date1 == null) {
                return 1;
            }
            if (date2 == null) {
                return -1;
            }
            return date1.compareTo(date2);
        }
        else {
            if (newsItem1.getGuid() != newsItem2.getGuid()) {
                return newsItem1.getGuid().compareTo(newsItem2.getGuid());
            }
            return newsItem2.hashCode() - newsItem1.hashCode();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)NewsComparatorByPriority.class);
        INSTANCE = new NewsComparatorByPriority();
    }
}
