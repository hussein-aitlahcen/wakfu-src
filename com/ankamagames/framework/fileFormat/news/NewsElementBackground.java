package com.ankamagames.framework.fileFormat.news;

import com.ankamagames.framework.graphics.image.*;
import org.jetbrains.annotations.*;

public class NewsElementBackground
{
    private final Color m_color;
    private final NewsImage m_image;
    
    NewsElementBackground(final Color color, final NewsImage image) {
        super();
        this.m_color = color;
        this.m_image = image;
    }
    
    public boolean hasColor() {
        return this.m_color != null;
    }
    
    @Nullable
    public Color getColor() {
        return this.m_color;
    }
    
    public boolean hasImage() {
        return this.m_image != null;
    }
    
    @Nullable
    public NewsImage getImage() {
        return this.m_image;
    }
}
