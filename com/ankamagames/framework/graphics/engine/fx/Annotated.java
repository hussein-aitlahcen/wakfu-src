package com.ankamagames.framework.graphics.engine.fx;

import com.ankamagames.framework.graphics.engine.*;

public class Annotated
{
    public final int m_crc;
    public final String m_name;
    
    public Annotated(final String name) {
        super();
        this.m_name = name;
        this.m_crc = Engine.getTechnic(name);
    }
}
