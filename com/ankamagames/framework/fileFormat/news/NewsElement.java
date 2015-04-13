package com.ankamagames.framework.fileFormat.news;

import com.ankamagames.framework.kernel.core.maths.*;
import org.jetbrains.annotations.*;

public abstract class NewsElement
{
    protected final Rect m_area;
    private final NewsElementType m_type;
    private NewsElementBackground m_background;
    private NewsElementBackground m_backgroundOver;
    private NewsElementTiming m_timing;
    
    NewsElement(@NotNull final NewsElementType type, @NotNull final Rect area) {
        super();
        this.m_type = type;
        this.m_area = area;
    }
    
    public Rect getArea() {
        return new Rect(this.m_area);
    }
    
    public NewsElementType getType() {
        return this.m_type;
    }
    
    public NewsElementBackground getBackground() {
        return this.m_background;
    }
    
    public NewsElementBackground getBackgroundOver() {
        return this.m_backgroundOver;
    }
    
    @Nullable
    public NewsElementTiming getTiming() {
        return this.m_timing;
    }
    
    void setTiming(final NewsElementTiming timing) {
        this.m_timing = timing;
    }
    
    void setBackground(final NewsElementBackground background) {
        this.m_background = background;
    }
    
    void setBackgroundOver(final NewsElementBackground backgroundOver) {
        this.m_backgroundOver = backgroundOver;
    }
}
