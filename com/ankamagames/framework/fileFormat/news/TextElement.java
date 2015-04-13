package com.ankamagames.framework.fileFormat.news;

import java.net.*;
import com.ankamagames.framework.kernel.core.maths.*;
import org.jetbrains.annotations.*;

public class TextElement extends NewsElement
{
    private final String m_text;
    private URL m_link;
    private Rect m_triggeringArea;
    
    TextElement(@NotNull final Rect area, final String text) {
        super(NewsElementType.TEXT, area);
        this.m_link = null;
        this.m_triggeringArea = null;
        if (text != null) {
            this.m_text = text;
        }
        else {
            this.m_text = "";
        }
    }
    
    public String getText() {
        return this.m_text;
    }
    
    public URL getLink() {
        return this.m_link;
    }
    
    public boolean hasTriggeringArea() {
        return this.m_triggeringArea != null;
    }
    
    public Rect getTriggeringArea() {
        return new Rect(this.m_triggeringArea);
    }
    
    void setLink(final URL link) {
        this.m_link = link;
    }
    
    void setTriggeringArea(final Rect triggeringArea) {
        this.m_triggeringArea = triggeringArea;
    }
}
