package com.ankamagames.xulor2.util;

import org.apache.log4j.*;
import com.ankamagames.xulor2.*;

public class SimpleCursor implements Cursor
{
    private static Logger m_logger;
    private java.awt.Cursor m_cursor;
    
    public SimpleCursor(final java.awt.Cursor cursor) {
        super();
        this.m_cursor = cursor;
    }
    
    @Override
    public void show() {
        Xulor.getInstance().getAppUI().setCursor(this.m_cursor);
    }
    
    @Override
    public void hide() {
    }
    
    static {
        SimpleCursor.m_logger = Logger.getLogger((Class)SimpleCursor.class);
    }
}
