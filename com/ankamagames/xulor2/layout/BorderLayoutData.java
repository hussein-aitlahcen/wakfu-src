package com.ankamagames.xulor2.layout;

import org.apache.log4j.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class BorderLayoutData extends AbstractLayoutData implements Releasable
{
    private static Logger m_logger;
    public static final String TAG = "borderLayoutData";
    public static final String SHORT_TAG = "bld";
    private Values m_data;
    private static final ObjectPool m_pool;
    public static final int DATA_HASH;
    
    public BorderLayoutData() {
        super();
        this.m_data = null;
    }
    
    public static BorderLayoutData checkOut() {
        BorderLayoutData c;
        try {
            c = (BorderLayoutData)BorderLayoutData.m_pool.borrowObject();
            c.m_currentPool = BorderLayoutData.m_pool;
        }
        catch (Exception e) {
            BorderLayoutData.m_logger.error((Object)"Probl\u00e8me au borrowObject.");
            c = new BorderLayoutData();
            c.onCheckOut();
        }
        return c;
    }
    
    @Override
    public String getTag() {
        return "borderLayoutData";
    }
    
    public Values getData() {
        return this.m_data;
    }
    
    public void setData(final Values data) {
        this.m_data = data;
    }
    
    @Override
    public void copyElement(final BasicElement b) {
        final BorderLayoutData e = (BorderLayoutData)b;
        super.copyElement(e);
        e.m_data = this.m_data;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_data = null;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == BorderLayoutData.DATA_HASH) {
            this.setData(Values.value(value));
            return true;
        }
        return super.setXMLAttribute(hash, value, cl);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        BorderLayoutData.m_logger = Logger.getLogger((Class)BorderLayoutData.class);
        m_pool = new MonitoredPool(new ObjectFactory<BorderLayoutData>() {
            @Override
            public BorderLayoutData makeObject() {
                return new BorderLayoutData();
            }
        });
        DATA_HASH = "data".hashCode();
    }
    
    public enum Values
    {
        CENTER, 
        NORTH, 
        SOUTH, 
        EAST, 
        WEST;
        
        public static Values value(final String value) {
            final Values[] arr$;
            final Values[] values = arr$ = values();
            for (final Values a : arr$) {
                if (a.name().equals(value.toUpperCase())) {
                    return a;
                }
            }
            return values[0];
        }
    }
}
