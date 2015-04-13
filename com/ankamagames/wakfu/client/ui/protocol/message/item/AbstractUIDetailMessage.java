package com.ankamagames.wakfu.client.ui.protocol.message.item;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;

public class AbstractUIDetailMessage<T extends FieldProvider> extends UIMessage
{
    private T m_item;
    private String m_parentWindowId;
    private int m_x;
    private int m_y;
    
    public AbstractUIDetailMessage() {
        super();
        this.m_x = 0;
        this.m_y = 0;
    }
    
    public void setItem(final T item) {
        this.m_item = item;
    }
    
    public T getItem() {
        return this.m_item;
    }
    
    public String getParentWindowId() {
        return this.m_parentWindowId;
    }
    
    public void setParentWindowId(final String parentWindowId) {
        this.m_parentWindowId = parentWindowId;
    }
    
    public void setX(final int x) {
        this.m_x = x;
    }
    
    public void setY(final int y) {
        this.m_y = y;
    }
    
    public int getX() {
        return this.m_x;
    }
    
    public int getY() {
        return this.m_y;
    }
}
