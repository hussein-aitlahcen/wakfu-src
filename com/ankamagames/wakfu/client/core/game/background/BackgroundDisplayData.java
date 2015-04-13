package com.ankamagames.wakfu.client.core.game.background;

import java.util.*;

public class BackgroundDisplayData
{
    private final BackgroundDisplayType m_type;
    private final int m_id;
    private final PageData[] m_pageData;
    
    public BackgroundDisplayData(final BackgroundDisplayType type, final int id, final ArrayList<PageData> pages) {
        super();
        this.m_type = type;
        this.m_id = id;
        this.m_pageData = new PageData[pages.size()];
        for (final PageData pageData : pages) {
            this.m_pageData[pageData.getIndex()] = pageData;
        }
    }
    
    public BackgroundDisplayType getType() {
        return this.m_type;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public PageData[] getPageData() {
        return this.m_pageData;
    }
}
