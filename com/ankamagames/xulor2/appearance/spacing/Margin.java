package com.ankamagames.xulor2.appearance.spacing;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class Margin extends InsetsElement
{
    public static final String TAG = "margin";
    private static final Logger m_logger;
    private static final ObjectPool m_pool;
    
    @Override
    public String getTag() {
        return "margin";
    }
    
    public static Margin checkOut() {
        Margin c;
        try {
            c = (Margin)Margin.m_pool.borrowObject();
            c.m_currentPool = Margin.m_pool;
        }
        catch (Exception e) {
            Margin.m_logger.error((Object)"Probl\u00e8me au borrowObject.");
            c = new Margin();
            c.onCheckOut();
        }
        return c;
    }
    
    static {
        m_logger = Logger.getLogger((Class)Margin.class);
        m_pool = new MonitoredPool(new ObjectFactory<Margin>() {
            @Override
            public Margin makeObject() {
                return new Margin();
            }
        }, 1000);
    }
}
