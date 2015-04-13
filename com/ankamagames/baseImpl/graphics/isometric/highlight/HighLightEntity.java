package com.ankamagames.baseImpl.graphics.isometric.highlight;

import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.kernel.core.common.*;

public class HighLightEntity extends Entity3D
{
    public static final ObjectFactory Factory;
    boolean m_transformed;
    
    @Override
    protected void checkout() {
        super.checkout();
        this.removeEffectForWorld();
    }
    
    @Override
    public void clear() {
        super.clear();
        this.m_transformed = false;
    }
    
    static {
        Factory = new ObjectFactory();
    }
    
    public static final class ObjectFactory extends MemoryObject.ObjectFactory<HighLightEntity>
    {
        public ObjectFactory() {
            super(HighLightEntity.class);
        }
        
        @Override
        public HighLightEntity create() {
            return new HighLightEntity(null);
        }
    }
}
