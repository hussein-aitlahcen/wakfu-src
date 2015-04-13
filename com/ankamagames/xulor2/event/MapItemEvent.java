package com.ankamagames.xulor2.event;

import org.apache.log4j.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class MapItemEvent extends ItemEvent
{
    private static final Logger m_logger;
    private int m_meshX;
    private int m_meshY;
    private int m_meshWidth;
    private int m_meshHeight;
    private static final ObjectPool m_pool;
    
    public static MapItemEvent checkOut(final MouseEvent me, final EventDispatcher target, final Events type, final DisplayableMapPoint value, final EntitySprite mesh) {
        MapItemEvent e;
        try {
            e = (MapItemEvent)MapItemEvent.m_pool.borrowObject();
            e.m_currentPool = MapItemEvent.m_pool;
        }
        catch (Exception ex) {
            MapItemEvent.m_logger.error((Object)"Probl\u00e8me au borrowObject.");
            e = new MapItemEvent();
            e.onCheckOut();
        }
        e.setButton(me.m_button);
        e.setClickCount(me.m_clickCount);
        e.setModifiers(me.m_modifiers);
        e.setScreenX(me.m_screenX);
        e.setScreenY(me.m_screenY);
        e.setSoundConsumed(me.isSoundConsumed());
        e.setTarget(target);
        e.setType(type);
        e.setItemValue(value);
        e.setMeshBounds(mesh);
        return e;
    }
    
    public int getMeshX() {
        return this.m_meshX;
    }
    
    public int getMeshY() {
        return this.m_meshY;
    }
    
    public int getMeshWidth() {
        return this.m_meshWidth;
    }
    
    public int getMeshHeight() {
        return this.m_meshHeight;
    }
    
    public DisplayableMapPoint getDisplayableMapPoint() {
        return (DisplayableMapPoint)this.getItemValue();
    }
    
    private void setMeshBounds(final EntitySprite mesh) {
        this.m_meshX = (int)mesh.getCenterX();
        this.m_meshY = (int)mesh.getCenterY();
        this.m_meshWidth = mesh.getWidth();
        this.m_meshHeight = mesh.getHeight();
    }
    
    static {
        m_logger = Logger.getLogger((Class)MapItemEvent.class);
        m_pool = new MonitoredPool(new ObjectFactory<MapItemEvent>() {
            @Override
            public MapItemEvent makeObject() {
                return new MapItemEvent();
            }
        });
    }
}
