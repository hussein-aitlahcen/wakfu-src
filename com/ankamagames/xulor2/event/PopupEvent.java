package com.ankamagames.xulor2.event;

import com.ankamagames.xulor2.core.event.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class PopupEvent extends Event
{
    private static Logger m_logger;
    private static final ObjectPool m_pool;
    
    public static PopupEvent checkOut() {
        PopupEvent e;
        try {
            e = (PopupEvent)PopupEvent.m_pool.borrowObject();
            e.m_currentPool = PopupEvent.m_pool;
        }
        catch (Exception ex) {
            PopupEvent.m_logger.error((Object)"Probl\u00e8me au borrowObject.");
            e = new PopupEvent();
            e.onCheckOut();
        }
        return e;
    }
    
    static {
        PopupEvent.m_logger = Logger.getLogger((Class)PopupEvent.class);
        m_pool = new MonitoredPool(new ObjectFactory<PopupEvent>() {
            @Override
            public PopupEvent makeObject() {
                return new PopupEvent();
            }
        });
    }
}
