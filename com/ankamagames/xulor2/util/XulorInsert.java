package com.ankamagames.xulor2.util;

import com.ankamagames.xulor2.core.*;

public class XulorInsert implements XulorLoadUnload
{
    public EventDispatcher m_element;
    public EventDispatcher m_parent;
    public String m_id;
    public long m_options;
    public int m_layer;
    
    public XulorInsert(final EventDispatcher element, final EventDispatcher parent, final String id, final int layer, final long options) {
        super();
        this.m_element = null;
        this.m_parent = null;
        this.m_id = null;
        this.m_options = 0L;
        this.m_layer = -30000;
        this.m_element = element;
        this.m_parent = parent;
        this.m_id = id;
        this.m_layer = layer;
        this.m_options = options;
    }
}
