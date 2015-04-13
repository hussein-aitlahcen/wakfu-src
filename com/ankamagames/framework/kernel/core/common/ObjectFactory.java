package com.ankamagames.framework.kernel.core.common;

import org.apache.commons.pool.*;
import org.apache.log4j.*;
import java.util.*;

public abstract class ObjectFactory<E extends Poolable> implements PoolableObjectFactory
{
    private static final boolean CHECK_OBJECT_VALIDITY = false;
    private static final int NOT_FOUND = -1;
    private static final Logger m_logger;
    private final ArrayList<Poolable> m_actives;
    
    public ObjectFactory() {
        super();
        this.m_actives = new ArrayList<Poolable>(0);
    }
    
    @Override
    public abstract E makeObject();
    
    @Override
    public void activateObject(final Object obj) {
        try {
            ((Poolable)obj).onCheckOut();
        }
        catch (Exception e) {
            ObjectFactory.m_logger.error((Object)"Exception on checkOut : ", (Throwable)e);
        }
    }
    
    @Override
    public void passivateObject(final Object obj) {
        try {
            ((Poolable)obj).onCheckIn();
        }
        catch (Exception e) {
            ObjectFactory.m_logger.error((Object)"Exception on checkIn : ", (Throwable)e);
            throw new RuntimeException(e.toString());
        }
    }
    
    @Override
    public void destroyObject(final Object obj) {
    }
    
    @Override
    public boolean validateObject(final Object obj) {
        return true;
    }
    
    private int findObjectIndex(final Object obj) {
        for (int i = 0, size = this.m_actives.size(); i < size; ++i) {
            if (this.m_actives.get(i) == obj) {
                return i;
            }
        }
        return -1;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ObjectFactory.class);
    }
}
