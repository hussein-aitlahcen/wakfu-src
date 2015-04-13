package com.ankamagames.framework.kernel.core.common;

import org.apache.commons.pool.impl.*;
import org.apache.log4j.*;
import java.util.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.kernel.core.common.debug.*;

public class MonitoredPool extends SoftReferenceObjectPool
{
    protected static final Logger m_logger;
    protected String m_poolName;
    protected static final Object MUTEX;
    protected static final HashMap<String, MonitoredPool> m_monitoredPools;
    
    public static MonitoredPool getPool(final String pooledObjectName) {
        synchronized (MonitoredPool.MUTEX) {
            return MonitoredPool.m_monitoredPools.get(pooledObjectName);
        }
    }
    
    public static Iterable<String> getPools() {
        synchronized (MonitoredPool.MUTEX) {
            return MonitoredPool.m_monitoredPools.keySet();
        }
    }
    
    public static void registerPool(final String pooledObjectName, final MonitoredPool pool) {
        synchronized (MonitoredPool.MUTEX) {
            int i;
            String poolName;
            for (i = 0, poolName = pooledObjectName; MonitoredPool.m_monitoredPools.containsKey(poolName); poolName = pooledObjectName + " #" + i++) {}
            pool.m_poolName = poolName;
            MonitoredPool.m_monitoredPools.put(poolName, pool);
        }
    }
    
    public MonitoredPool(final PoolableObjectFactory factory) {
        super(factory);
        try {
            final Object obj = this.borrowObject();
            registerPool(this.m_poolName = obj.getClass().getName(), this);
            this.returnObject(obj);
        }
        catch (Exception e) {
            MonitoredPool.m_logger.error((Object)"Exception lev\u00e9e : ", (Throwable)e);
        }
    }
    
    public String getName() {
        return this.m_poolName;
    }
    
    public MonitoredPool(final PoolableObjectFactory factory, final int initSize) {
        super(factory);
        int size = initSize;
        if (size < 1) {
            size = 1;
        }
        try {
            final Object[] objs = new Object[size];
            for (int i = 0; i < size; ++i) {
                objs[i] = this.borrowObject();
            }
            for (int i = 0; i < size; ++i) {
                this.returnObject(objs[i]);
                objs[i] = null;
            }
            final Object obj = this.borrowObject();
            this.returnObject(obj);
            registerPool(this.m_poolName = obj.getClass().getName(), this);
        }
        catch (Exception e) {
            MonitoredPool.m_logger.error((Object)"Exception lev\u00e9e : ", (Throwable)e);
        }
    }
    
    @Override
    public synchronized Object borrowObject() throws Exception {
        final Object obj = super.borrowObject();
        PoolDebug.add(obj);
        return obj;
    }
    
    @Override
    public synchronized void returnObject(final Object obj) throws Exception {
        super.returnObject(obj);
        PoolDebug.remove(obj);
    }
    
    static {
        m_logger = Logger.getLogger((Class)MonitoredPool.class);
        MUTEX = new Object();
        m_monitoredPools = new HashMap<String, MonitoredPool>();
    }
}
