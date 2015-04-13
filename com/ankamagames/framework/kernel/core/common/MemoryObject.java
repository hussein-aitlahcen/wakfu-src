package com.ankamagames.framework.kernel.core.common;

import com.ankamagames.framework.kernel.core.maths.*;

public abstract class MemoryObject extends ReferenceCounter
{
    private int m_objectGUID;
    private MemoryObjectPool m_pool;
    
    public final int getObjectGUID() {
        return this.m_objectGUID;
    }
    
    public static int computeClassCRC(final Class classType) {
        return computeClassCRC(classType.getName());
    }
    
    public static int computeClassCRC(final String className) {
        return MathHelper.getCRC(className);
    }
    
    @Override
    protected void onNegativeNumReferences() {
        super.onNegativeNumReferences();
        if (this.m_pool != null) {
            this.m_pool.release(this);
        }
    }
    
    @Override
    protected final void delete() {
        super.delete();
        this.checkin();
    }
    
    protected abstract void checkout();
    
    protected abstract void checkin();
    
    final void registerToPool(final int guid, final MemoryObjectPool pool) {
        this.m_objectGUID = guid;
        this.m_pool = pool;
    }
    
    public abstract static class ObjectFactory<T extends MemoryObject>
    {
        private final int m_crc;
        private final Class m_classType;
        private MemoryObjectPool m_pool;
        
        protected ObjectFactory(final Class classType) {
            super();
            this.m_crc = MemoryObject.computeClassCRC(classType);
            this.m_classType = classType;
        }
        
        public final T newInstance() {
            final T object = this.create();
            object.checkout();
            return object;
        }
        
        public final T newPooledInstance() {
            if (this.m_pool == null) {
                this.m_pool = MemoryObjectPoolManager.getInstance().getOrCreateMemoryPool(this.m_crc, this.m_classType);
            }
            return (T)this.m_pool.getMemoryObject();
        }
        
        public abstract T create();
    }
}
