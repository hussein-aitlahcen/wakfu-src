package com.ankamagames.framework.kernel.core.common;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import java.util.*;
import com.ankamagames.framework.fileFormat.xml.*;
import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.framework.graphics.engine.*;
import gnu.trove.*;

public final class MemoryObjectPoolManager
{
    private static final int DEFAULT_NUM_OBJECTS = 256;
    private static final Logger m_logger;
    private static final MemoryObjectPoolManager m_instance;
    private final TIntIntHashMap m_poolsSize;
    private final TIntObjectHashMap<MemoryObjectPool> m_pools;
    private final TIntObjectHashMap<MemoryObject.ObjectFactory> m_factories;
    private static final TObjectIntHashMap<MemoryObjectPool> m_lastCount;
    
    private MemoryObjectPoolManager() {
        super();
        this.m_factories = new TIntObjectHashMap<MemoryObject.ObjectFactory>();
        this.m_pools = new TIntObjectHashMap<MemoryObjectPool>();
        this.m_poolsSize = new TIntIntHashMap();
    }
    
    MemoryObjectPool getOrCreateMemoryPool(final int classCRC, final Class classType) {
        MemoryObjectPool pool = this.m_pools.get(classCRC);
        if (pool == null) {
            int size = this.m_poolsSize.get(classCRC);
            if (size == 0) {
                size = 256;
            }
            pool = this.createDefaultPool(classCRC, classType, size);
        }
        return pool;
    }
    
    private MemoryObjectPool createDefaultPool(final int crc, final Class classType, final int size) {
        MemoryObject.ObjectFactory factory = this.m_factories.get(crc);
        if (factory == null) {
            factory = createFactoryFor(null, classType);
            this.m_factories.put(crc, factory);
        }
        return new MemoryObjectPool(size, classType, factory);
    }
    
    public final MemoryObjectPool[] getMemoryPools() {
        final MemoryObjectPool[] pools = new MemoryObjectPool[this.m_pools.size()];
        return this.m_pools.getValues(pools);
    }
    
    private static MemoryObject.ObjectFactory createFactoryFor(String factoryName, final String className) throws Exception {
        if (StringUtils.isEmptyOrNull(factoryName)) {
            factoryName = className + "$ObjectFactory";
        }
        return (MemoryObject.ObjectFactory)Class.forName(factoryName).newInstance();
    }
    
    private static MemoryObject.ObjectFactory createFactoryFor(final String factoryName, final Class classType) {
        try {
            return createFactoryFor(factoryName, classType.getName());
        }
        catch (Exception e) {
            MemoryObjectPoolManager.m_logger.error((Object)e);
            return MemoryObjectPool.createDefaultFactory(classType);
        }
    }
    
    public final void loadConfiguration(final XMLDocumentContainer configFile) {
        final DocumentEntry poolsNode = configFile.getRootNode().getChildByName("memoryObjectPools");
        if (poolsNode == null) {
            MemoryObjectPoolManager.m_logger.warn((Object)"No pools configuration found");
            return;
        }
        final ArrayList<DocumentEntry> pools = poolsNode.getChildrenByName("pool");
        if (pools == null) {
            MemoryObjectPoolManager.m_logger.warn((Object)"No pools configuration found");
            return;
        }
        for (int numPools = pools.size(), i = 0; i < numPools; ++i) {
            final DocumentEntry pool = pools.get(i);
            final DocumentEntry classParameter = pool.getParameterByName("class");
            final DocumentEntry sizeParamter = pool.getParameterByName("size");
            final String className = classParameter.getStringValue();
            final int size = sizeParamter.getIntValue();
            final int crcValue = MemoryObject.computeClassCRC(className);
            this.setPoolStartSize(crcValue, size);
            final DocumentEntry factoryClassParameter = pool.getParameterByName("factory_class");
            final String factoryName = (factoryClassParameter != null) ? factoryClassParameter.getStringValue() : null;
            try {
                this.m_factories.put(crcValue, createFactoryFor(factoryName, className));
            }
            catch (Exception e) {
                MemoryObjectPoolManager.m_logger.error((Object)("probl\u00e8me avec la factory pour la classe " + className + "\n" + e.getMessage()));
            }
        }
    }
    
    public final void loadConfiguration(final String configFileName) {
        final XMLDocumentAccessor accessor = XMLDocumentAccessor.getInstance();
        final XMLDocumentContainer configFile = accessor.getNewDocumentContainer();
        try {
            accessor.open(configFileName);
            accessor.read(configFile, new DocumentEntryParser[0]);
            accessor.close();
        }
        catch (Exception e) {
            MemoryObjectPoolManager.m_logger.error((Object)"Exception", (Throwable)e);
        }
        this.loadConfiguration(configFile);
    }
    
    public static MemoryObjectPoolManager getInstance() {
        return MemoryObjectPoolManager.m_instance;
    }
    
    final void registerPool(final MemoryObjectPool pool) {
        this.m_pools.put(pool.getClassCRC(), pool);
        EngineStats.getInstance().monitorPool(pool);
    }
    
    private void setPoolStartSize(final int classCRC, final int size) {
        this.m_poolsSize.put(classCRC, size);
    }
    
    public static void dump() {
        final StringBuilder sb = new StringBuilder("**** dump MemoryObjectPools ***\n");
        final TIntObjectIterator<MemoryObjectPool> iter = getInstance().m_pools.iterator();
        while (iter.hasNext()) {
            iter.advance();
            final MemoryObjectPool pool = iter.value();
            final int last = MemoryObjectPoolManager.m_lastCount.get(pool);
            sb.append(pool.getClassType() + " active=" + pool.getNumUsed() + " idle=" + pool.getNumFree() + " poolSize=" + pool.getSize());
            sb.append("  ").append(pool.getNumUsed() - last);
            MemoryObjectPoolManager.m_lastCount.put(pool, pool.getNumUsed());
            sb.append("\n");
        }
        MemoryObjectPoolManager.m_logger.info((Object)sb.toString());
    }
    
    static {
        m_logger = Logger.getLogger((Class)MemoryObjectPoolManager.class);
        m_instance = new MemoryObjectPoolManager();
        m_lastCount = new TObjectIntHashMap<MemoryObjectPool>();
    }
}
