package com.ankamagames.xulor2.event;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class MapEvent extends MouseEvent
{
    private static final Logger m_logger;
    private static final ObjectPool m_pool;
    private float m_isoX;
    private float m_isoY;
    private Object m_value;
    
    public static MapEvent checkOut() {
        MapEvent e;
        try {
            e = (MapEvent)MapEvent.m_pool.borrowObject();
            e.m_currentPool = MapEvent.m_pool;
        }
        catch (Exception ex) {
            MapEvent.m_logger.error((Object)"Probl\u00e8me au borrowObject.");
            e = new MapEvent();
            e.onCheckOut();
        }
        return e;
    }
    
    public static MapEvent checkOut(final MouseEvent me, final float isoX, final float isoY, final Object value) {
        final MapEvent e = checkOut();
        e.setButton(me.m_button);
        e.setClickCount(me.m_clickCount);
        e.setModifiers(me.m_modifiers);
        e.setScreenX(me.m_screenX);
        e.setScreenY(me.m_screenY);
        e.setTarget(me.getTarget());
        e.setIsoX(isoX);
        e.setIsoY(isoY);
        e.setValue(value);
        return e;
    }
    
    public float getIsoX() {
        return this.m_isoX;
    }
    
    public void setIsoX(final float isoX) {
        this.m_isoX = isoX;
    }
    
    public float getIsoY() {
        return this.m_isoY;
    }
    
    public void setIsoY(final float isoY) {
        this.m_isoY = isoY;
    }
    
    public Object getValue() {
        return this.m_value;
    }
    
    public void setValue(final Object value) {
        this.m_value = value;
    }
    
    static {
        m_logger = Logger.getLogger((Class)MapEvent.class);
        m_pool = new MonitoredPool(new ObjectFactory<MapEvent>() {
            @Override
            public MapEvent makeObject() {
                return new MapEvent();
            }
        });
    }
}
