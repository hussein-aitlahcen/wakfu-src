package com.ankamagames.baseImpl.common.clientAndServer.game.characteristic;

import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import org.apache.log4j.*;
import gnu.trove.*;

public abstract class AbstractPropertyManager<T extends PropertyType> implements Poolable, RawConvertible<RawProperties>
{
    protected static Logger m_logger;
    protected final TByteByteHashMap m_properties;
    
    public AbstractPropertyManager() {
        super();
        this.m_properties = new TByteByteHashMap();
    }
    
    @Override
    public void onCheckOut() {
        this.m_properties.clear();
    }
    
    @Override
    public void onCheckIn() {
        this.m_properties.clear();
    }
    
    public boolean isActiveProperty(final T type) {
        final Byte value = this.m_properties.get(type.getId());
        return value != null && value != 0;
    }
    
    public byte getPropertyValue(final T type) {
        final Byte value = this.m_properties.get(type.getId());
        if (value == null) {
            return 0;
        }
        return value;
    }
    
    public void setPropertyValue(final T type, final byte value) {
        this.m_properties.put(type.getId(), value);
    }
    
    public byte add(final T type) {
        if (this.m_properties.containsKey(type.getId())) {
            final byte b = (byte)(this.m_properties.get(type.getId()) + 1);
            this.m_properties.put(type.getId(), b);
            return b;
        }
        this.m_properties.put(type.getId(), (byte)1);
        return 1;
    }
    
    public byte[] getActiveProperties() {
        return this.m_properties.keys();
    }
    
    public byte substract(final T type) {
        if (!this.m_properties.containsKey(type.getId())) {
            return 0;
        }
        final byte b = (byte)(this.m_properties.get(type.getId()) - 1);
        if (b <= 0) {
            this.m_properties.remove(type.getId());
            return 0;
        }
        this.m_properties.put(type.getId(), b);
        return b;
    }
    
    public void remove(final T type) {
        this.m_properties.remove(type.getId());
    }
    
    public void reset() {
        this.m_properties.clear();
    }
    
    public TByteByteHashMap getProperties() {
        return this.m_properties;
    }
    
    public boolean isEmpty() {
        return this.m_properties.isEmpty();
    }
    
    static {
        AbstractPropertyManager.m_logger = Logger.getLogger((Class)AbstractPropertyManager.class);
    }
}
