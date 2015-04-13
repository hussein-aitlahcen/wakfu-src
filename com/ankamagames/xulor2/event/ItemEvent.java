package com.ankamagames.xulor2.event;

import org.apache.log4j.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class ItemEvent extends MouseEvent
{
    private static Logger m_logger;
    private Object m_itemValue;
    private static final ObjectPool m_pool;
    private static int checkouts;
    private static int releases;
    
    public ItemEvent() {
        super();
        this.m_itemValue = null;
    }
    
    public static ItemEvent checkOut(final EventDispatcher target, final Events type, final Object value) {
        ++ItemEvent.checkouts;
        ItemEvent e;
        try {
            e = (ItemEvent)ItemEvent.m_pool.borrowObject();
            e.m_currentPool = ItemEvent.m_pool;
        }
        catch (Exception ex) {
            ItemEvent.m_logger.error((Object)"Probl\u00e8me au borrowObject.");
            e = new ItemEvent();
            e.onCheckOut();
        }
        e.setTarget(target);
        e.setType(type);
        e.setItemValue(value);
        return e;
    }
    
    public static ItemEvent checkOut(final MouseEvent me, final EventDispatcher target, final Events type, final Object value) {
        ++ItemEvent.checkouts;
        ItemEvent e;
        try {
            e = (ItemEvent)ItemEvent.m_pool.borrowObject();
            e.m_currentPool = ItemEvent.m_pool;
        }
        catch (Exception ex) {
            ItemEvent.m_logger.error((Object)"Probl\u00e8me au borrowObject.");
            e = new ItemEvent();
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
        return e;
    }
    
    @Override
    public void release() {
        super.release();
        ++ItemEvent.releases;
    }
    
    public Object getItemValue() {
        return this.m_itemValue;
    }
    
    public void setItemValue(final Object itemValue) {
        this.m_itemValue = itemValue;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_itemValue = null;
    }
    
    static {
        ItemEvent.m_logger = Logger.getLogger((Class)ItemEvent.class);
        m_pool = new MonitoredPool(new ObjectFactory<ItemEvent>() {
            @Override
            public ItemEvent makeObject() {
                return new ItemEvent();
            }
        });
        ItemEvent.checkouts = 0;
        ItemEvent.releases = 0;
    }
}
