package com.ankamagames.framework.kernel.core.common;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.framework.kernel.core.common.debug.*;

public class MemoryObjectPool
{
    private static final boolean DEBUG = false;
    private static final Logger m_logger;
    private MemoryObject[] m_memoryObjects;
    private final FreeList m_freeList;
    private final Class m_classType;
    private final MemoryObject.ObjectFactory m_factory;
    private final int m_classCRC;
    private final Object m_freelistMutex;
    
    public static MemoryObject.ObjectFactory createDefaultFactory(final Class classType) {
        return new MemoryObject.ObjectFactory(classType) {
            @Override
            public MemoryObject create() {
                try {
                    return classType.newInstance();
                }
                catch (InstantiationException e) {
                    MemoryObjectPool.m_logger.error((Object)"", (Throwable)e);
                }
                catch (IllegalAccessException e2) {
                    throw new IllegalArgumentException(classType.toString(), e2);
                }
                catch (ClassCastException e3) {
                    throw new IllegalArgumentException(classType + " n'est pas de type MemoryObject");
                }
                return null;
            }
        };
    }
    
    public MemoryObjectPool(final int numObjects, final Class classType) {
        this(numObjects, classType, createDefaultFactory(classType));
    }
    
    public MemoryObjectPool(final int numObjects, final Class classType, final MemoryObject.ObjectFactory factory) {
        super();
        this.m_freelistMutex = new Object();
        this.checkFactoryType(factory, classType);
        this.m_factory = factory;
        this.m_classType = classType;
        this.m_classCRC = MemoryObject.computeClassCRC(classType);
        this.m_memoryObjects = new MemoryObject[numObjects];
        this.m_freeList = new FreeList(numObjects);
        for (int i = 0; i < this.m_memoryObjects.length; ++i) {
            this.m_memoryObjects[i] = this.createObject(i);
        }
        MemoryObjectPoolManager.getInstance().registerPool(this);
    }
    
    private boolean checkFactoryType(final MemoryObject.ObjectFactory factory, final Class classType) {
        final MemoryObject memoryObject = factory.create();
        if (memoryObject.getClass() != classType) {
            throw new IllegalArgumentException("factory invalide : devrait renvoy\u00e9e un " + classType);
        }
        return true;
    }
    
    public final MemoryObject getMemoryObject() {
        int index;
        final MemoryObject object;
        synchronized (this.m_freelistMutex) {
            index = this.m_freeList.checkout();
            if (index == -1) {
                this.resize(this.getNewSize());
                index = this.m_freeList.checkout();
            }
            object = this.m_memoryObjects[index];
        }
        object.resetReferences();
        try {
            object.checkout();
            PoolDebug.add(object);
            return object;
        }
        catch (Exception e) {
            synchronized (this.m_freelistMutex) {
                this.m_freeList.checkin(index);
            }
            throw new RuntimeException("Exception lev\u00e9e lors de l'extraction d'un \u00e9l\u00e9ment du pool", e);
        }
    }
    
    public final void release(final MemoryObject memoryObject) {
        synchronized (this.m_freelistMutex) {
            this.m_freeList.checkin(memoryObject.getObjectGUID());
            PoolDebug.remove(memoryObject);
        }
    }
    
    protected void releaseAll() {
        synchronized (this.m_freelistMutex) {
            this.m_freeList.freeAll();
        }
    }
    
    public final int getClassCRC() {
        return this.m_classCRC;
    }
    
    public final Class getClassType() {
        return this.m_classType;
    }
    
    public final int getNumFree() {
        synchronized (this.m_freelistMutex) {
            return this.m_freeList.getNumFree();
        }
    }
    
    public final int getNumUsed() {
        synchronized (this.m_freelistMutex) {
            return this.m_freeList.getNumUsed();
        }
    }
    
    public final int getSize() {
        synchronized (this.m_freelistMutex) {
            return this.m_freeList.getSize();
        }
    }
    
    private int getNewSize() {
        final int size = this.m_memoryObjects.length;
        if (size < 4096) {
            return size * 2;
        }
        return size + 4096;
    }
    
    private void resize(final int size) {
        assert size > this.m_memoryObjects.length;
        final MemoryObject[] memoryObjects = new MemoryObject[size];
        System.arraycopy(this.m_memoryObjects, 0, memoryObjects, 0, this.m_memoryObjects.length);
        for (int i = this.m_memoryObjects.length; i < size; ++i) {
            memoryObjects[i] = this.createObject(i);
        }
        this.m_freeList.resize(size);
        this.m_memoryObjects = memoryObjects;
    }
    
    private MemoryObject createObject(final int guid) {
        try {
            final MemoryObject memoryObject = this.m_factory.create();
            memoryObject.registerToPool(guid, this);
            return memoryObject;
        }
        catch (Exception e) {
            MemoryObjectPool.m_logger.error((Object)"Failed to create object", (Throwable)e);
            return null;
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)MemoryObjectPool.class);
    }
}
