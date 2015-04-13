package com.ankamagames.xulor2.appearance;

import com.ankamagames.framework.kernel.core.common.*;
import org.apache.log4j.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public class MapAppearance extends DecoratorAppearance implements Releasable
{
    private static Logger m_logger;
    public static final String TAG = "mapAppearance";
    private PixmapElement m_pixmap;
    private Color m_modulationColor;
    private int m_startX;
    private int m_endX;
    private int m_startY;
    private int m_endY;
    private boolean m_startXInit;
    private boolean m_endXInit;
    private boolean m_startYInit;
    private boolean m_endYInit;
    public static final int MODULATION_COLOR_HASH;
    public static final int START_X;
    public static final int START_Y;
    public static final int END_X;
    public static final int END_Y;
    
    public MapAppearance() {
        super();
        this.m_pixmap = null;
        this.m_modulationColor = null;
        this.m_startXInit = false;
        this.m_endXInit = false;
        this.m_startYInit = false;
        this.m_endYInit = false;
    }
    
    @Override
    public void add(final EventDispatcher e) {
        if (e instanceof PixmapElement) {
            this.m_pixmap = (PixmapElement)e;
            if (this.m_widget instanceof MapWidget) {
                ((MapWidget)this.m_widget).setMapBackgroundPixmap(((PixmapElement)e).getPixmap());
            }
        }
        super.add(e);
    }
    
    @Override
    public String getTag() {
        return "mapAppearance";
    }
    
    @Override
    public Color getModulationColor() {
        return this.m_modulationColor;
    }
    
    @Override
    public void setModulationColor(final Color modulationColor) {
        if (this.m_modulationColor == modulationColor) {
            return;
        }
        this.m_modulationColor = modulationColor;
        if (this.m_widget instanceof MapWidget) {
            ((MapWidget)this.m_widget).setModulationColor(modulationColor);
        }
    }
    
    public int getStartX() {
        return this.m_startX;
    }
    
    public void setStartX(final int startX) {
        this.m_startX = startX;
        this.m_startXInit = true;
        final MapWidget map = (MapWidget)this.m_widget;
        if (map != null) {
            map.setMapBackgroundStartX(this.m_startX);
        }
    }
    
    public int getEndX() {
        return this.m_endX;
    }
    
    public void setEndX(final int endX) {
        this.m_endX = endX;
        this.m_endXInit = true;
        final MapWidget map = (MapWidget)this.m_widget;
        if (map != null) {
            map.setMapBackgroundEndX(this.m_endX);
        }
    }
    
    public int getStartY() {
        return this.m_startY;
    }
    
    public void setStartY(final int startY) {
        this.m_startY = startY;
        this.m_startYInit = true;
        final MapWidget map = (MapWidget)this.m_widget;
        if (map != null) {
            map.setMapBackgroundStartY(this.m_startY);
        }
    }
    
    public int getEndY() {
        return this.m_endY;
    }
    
    public void setEndY(final int endY) {
        this.m_endY = endY;
        this.m_endYInit = true;
        final MapWidget map = (MapWidget)this.m_widget;
        if (map != null) {
            map.setMapBackgroundEndY(this.m_endY);
        }
    }
    
    @Override
    public void setWidget(final Widget w) {
        super.setWidget(w);
        if (w instanceof MapWidget) {
            final MapWidget map = (MapWidget)w;
            if (this.m_modulationColor != null) {
                map.setModulationColor(this.m_modulationColor);
            }
            if (this.m_pixmap != null) {
                map.setMapBackgroundPixmap(this.m_pixmap.getPixmap());
            }
            map.setMapBackgroundStartX(this.m_startX);
            map.setMapBackgroundStartY(this.m_startY);
            map.setMapBackgroundEndX(this.m_endX);
            map.setMapBackgroundEndY(this.m_endY);
        }
    }
    
    @Override
    public boolean preProcess(final int deltaTime) {
        if (this.m_widgetChanged && this.m_widget instanceof MapWidget) {
            final MapWidget map = (MapWidget)this.m_widget;
            if (this.m_pixmap != null) {
                map.setMapBackgroundPixmap(this.m_pixmap.getPixmap());
            }
            map.setMapBackgroundStartX(this.m_startX);
            map.setMapBackgroundStartY(this.m_startY);
            map.setMapBackgroundEndX(this.m_endX);
            map.setMapBackgroundEndY(this.m_endY);
        }
        return super.preProcess(deltaTime);
    }
    
    @Override
    public void copyElement(final BasicElement i) {
        final MapAppearance e = (MapAppearance)i;
        super.copyElement(e);
        if (this.m_startXInit) {
            e.setStartX(this.m_startX);
        }
        if (this.m_startYInit) {
            e.setStartY(this.m_startY);
        }
        if (this.m_endXInit) {
            e.setEndX(this.m_endX);
        }
        if (this.m_endYInit) {
            e.setEndY(this.m_endY);
        }
        e.setModulationColor(this.m_modulationColor);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_pixmap = null;
        this.m_modulationColor = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == MapAppearance.MODULATION_COLOR_HASH) {
            this.setModulationColor(cl.convertToColor(value));
        }
        else if (hash == MapAppearance.START_X) {
            this.setStartX(PrimitiveConverter.getInteger(value));
        }
        else if (hash == MapAppearance.START_Y) {
            this.setStartY(PrimitiveConverter.getInteger(value));
        }
        else if (hash == MapAppearance.END_X) {
            this.setEndX(PrimitiveConverter.getInteger(value));
        }
        else {
            if (hash != MapAppearance.END_Y) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setEndY(PrimitiveConverter.getInteger(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == MapAppearance.MODULATION_COLOR_HASH) {
            this.setModulationColor((Color)value);
        }
        else if (hash == MapAppearance.START_X) {
            this.setStartX(PrimitiveConverter.getInteger(value));
        }
        else if (hash == MapAppearance.START_Y) {
            this.setStartY(PrimitiveConverter.getInteger(value));
        }
        else if (hash == MapAppearance.END_X) {
            this.setEndX(PrimitiveConverter.getInteger(value));
        }
        else {
            if (hash != MapAppearance.END_Y) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setEndY(PrimitiveConverter.getInteger(value));
        }
        return true;
    }
    
    static {
        MapAppearance.m_logger = Logger.getLogger((Class)MapAppearance.class);
        MODULATION_COLOR_HASH = "modulationColor".hashCode();
        START_X = "startX".hashCode();
        START_Y = "startY".hashCode();
        END_X = "endX".hashCode();
        END_Y = "endY".hashCode();
    }
}
