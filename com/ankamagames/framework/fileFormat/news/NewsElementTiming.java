package com.ankamagames.framework.fileFormat.news;

import org.jetbrains.annotations.*;

public class NewsElementTiming
{
    private final int m_start;
    private final int m_end;
    public static final NewsElementTiming ALWAYS;
    
    @NotNull
    static NewsElementTiming create(final int start, final int end) {
        if (start == -1 && end == -1) {
            return NewsElementTiming.ALWAYS;
        }
        return new NewsElementTiming(start, end);
    }
    
    private NewsElementTiming(final int start, final int end) {
        super();
        this.m_start = start;
        this.m_end = end;
    }
    
    public int getStart() {
        return this.m_start;
    }
    
    public int getEnd() {
        return this.m_end;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final NewsElementTiming that = (NewsElementTiming)o;
        return this.m_end == that.m_end && this.m_start == that.m_start;
    }
    
    @Override
    public int hashCode() {
        int result = this.m_start;
        result = 31 * result + this.m_end;
        return result;
    }
    
    static {
        ALWAYS = new NewsElementTiming(-1, -1);
    }
}
