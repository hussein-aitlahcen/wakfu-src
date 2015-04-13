package com.ankamagames.xulor2.util;

import com.ankamagames.xulor2.graphics.*;
import org.jetbrains.annotations.*;
import com.ankamagames.xulor2.*;

public class TextData
{
    private String m_key;
    private int m_x;
    private int m_y;
    private int m_width;
    private int m_height;
    private XulorParticleSystem m_particle;
    
    public TextData(final String key, final int x, final int y, final int width, final int height) {
        this(key, x, y, width, height, null);
    }
    
    public TextData(final String key, final int x, final int y, final int width, final int height, @Nullable final XulorParticleSystem ps) {
        super();
        this.m_key = key;
        this.m_x = x;
        this.m_y = y;
        this.m_width = width;
        this.m_height = height;
        this.m_particle = ps;
    }
    
    public String getKey() {
        return this.m_key;
    }
    
    public int getX() {
        return this.m_x;
    }
    
    public int getY() {
        return this.m_y;
    }
    
    public int getWidth() {
        return this.m_width;
    }
    
    public int getHeight() {
        return this.m_height;
    }
    
    @Nullable
    public XulorParticleSystem getParticle() {
        return this.m_particle;
    }
    
    public void recomputeParticlePosition(final int x, final int y) {
    }
    
    public String getTranslatedString() {
        return Xulor.getInstance().getTranslatedString(this.m_key);
    }
}
