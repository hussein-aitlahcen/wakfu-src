package com.ankamagames.xulor2.core;

import java.awt.image.*;
import java.awt.*;
import java.util.*;
import com.ankamagames.xulor2.util.*;

public class CursorFactory
{
    private static final CursorFactory m_instance;
    private final EnumMap<CursorType, Cursor> m_cursors;
    private CursorType m_type;
    private boolean m_overrideUsed;
    
    private CursorFactory() {
        super();
        this.m_cursors = new EnumMap<CursorType, Cursor>(CursorType.class);
        this.m_overrideUsed = false;
    }
    
    public static CursorFactory getInstance() {
        return CursorFactory.m_instance;
    }
    
    public CursorType getType() {
        return this.m_type;
    }
    
    public void show(final CursorType type, final boolean override) {
        if ((!this.m_overrideUsed || override) && this.m_type != type) {
            final Cursor cursor = this.m_cursors.get(type);
            if (cursor != null) {
                this.hideCurrentCursor();
                cursor.show();
                this.m_type = type;
            }
        }
        if (override) {
            this.m_overrideUsed = true;
        }
    }
    
    public void unlock() {
        this.m_overrideUsed = false;
        if (this.m_type == CursorType.DEFAULT) {
            return;
        }
        this.hideCurrentCursor();
        this.m_type = CursorType.DEFAULT;
        this.m_cursors.get(this.m_type).show();
    }
    
    private void hideCurrentCursor() {
        final Cursor currentCursor = this.m_cursors.get(this.m_type);
        if (currentCursor != null) {
            currentCursor.hide();
        }
    }
    
    public void show(final CursorType type) {
        this.show(type, false);
    }
    
    public void addCursor(final CursorType type, int xHotspot, int yHotspot, final BufferedImage image) {
        if (type != null && image != null) {
            final Toolkit tk = Toolkit.getDefaultToolkit();
            final Dimension cursorSize = tk.getBestCursorSize(image.getWidth(), image.getHeight());
            final float xFactor = cursorSize.width / image.getWidth();
            final float yFactor = cursorSize.height / image.getHeight();
            xHotspot *= (int)xFactor;
            yHotspot *= (int)yFactor;
            this.m_cursors.put(type, new SimpleCursor(tk.createCustomCursor(image, new Point(xHotspot, yHotspot), null)));
            if (type.equals(CursorType.DEFAULT)) {
                this.show(type);
            }
        }
    }
    
    public void addAnimatedCursor(final CursorType type, int xHotspot, int yHotspot, final int delay, final ArrayList<BufferedImage> images) {
        if (images == null) {
            return;
        }
        final int size = images.size();
        if (type != null && size > 0) {
            if (size == 1) {
                this.addCursor(type, xHotspot, yHotspot, images.get(0));
                return;
            }
            final Toolkit tk = Toolkit.getDefaultToolkit();
            final BufferedImage image0 = images.get(0);
            final Dimension cursorSize = tk.getBestCursorSize(image0.getWidth(), image0.getHeight());
            final float xFactor = cursorSize.width / image0.getWidth();
            final float yFactor = cursorSize.height / image0.getHeight();
            xHotspot *= (int)xFactor;
            yHotspot *= (int)yFactor;
            final java.awt.Cursor[] cursors = new java.awt.Cursor[size];
            for (int i = 0; i < size; ++i) {
                cursors[i] = tk.createCustomCursor(images.get(i), new Point(xHotspot, yHotspot), null);
            }
            this.m_cursors.put(type, new AnimatedCursor(cursors, delay));
            if (type.equals(CursorType.DEFAULT)) {
                this.show(type);
            }
        }
    }
    
    static {
        m_instance = new CursorFactory();
    }
    
    public enum CursorType
    {
        DEFAULT, 
        HOVER, 
        MOVE, 
        TEXT, 
        HORIZONTAL_RESIZE, 
        VERTICAL_RESIZE, 
        NW_RESIZE, 
        SW_RESIZE, 
        HAND, 
        FORBIDDEN, 
        CUSTOM1, 
        CUSTOM2, 
        CUSTOM3, 
        CUSTOM4, 
        CUSTOM5, 
        CUSTOM6, 
        CUSTOM7, 
        CUSTOM8, 
        CUSTOM9, 
        CUSTOM10, 
        CUSTOM11, 
        CUSTOM12, 
        CUSTOM13, 
        CUSTOM14, 
        CUSTOM15, 
        CUSTOM16, 
        CUSTOM17, 
        CUSTOM18, 
        CUSTOM19, 
        CUSTOM20;
        
        public static CursorType value(final String value) {
            final CursorType[] arr$;
            final CursorType[] values = arr$ = values();
            for (final CursorType a : arr$) {
                if (a.name().equals(value.toUpperCase())) {
                    return a;
                }
            }
            return values[0];
        }
    }
}
