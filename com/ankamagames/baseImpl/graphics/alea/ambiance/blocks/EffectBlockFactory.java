package com.ankamagames.baseImpl.graphics.alea.ambiance.blocks;

import gnu.trove.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;
import java.nio.*;

public abstract class EffectBlockFactory
{
    private final TIntObjectHashMap<AbstractModel> m_models;
    
    protected EffectBlockFactory() {
        super();
        this.m_models = new TIntObjectHashMap<AbstractModel>();
        this.fill();
    }
    
    public final AbstractModel[] getAllModels() {
        final AbstractModel[] models = new AbstractModel[this.m_models.size()];
        this.m_models.getValues(models);
        return models;
    }
    
    public final AbstractModel getModelFromId(final int typeId) {
        return this.m_models.get(typeId);
    }
    
    public final AbstractBlock createBlock(final int typeId, final ByteBuffer buffer) throws Exception {
        final AbstractModel model = this.getModelFromId(typeId);
        if (model == null) {
            return null;
        }
        final AbstractBlock block = model.createInstance();
        model.readParams(block.m_params, buffer);
        return block;
    }
    
    protected abstract void fill();
    
    protected final void add(final AbstractModel model) {
        this.m_models.put(model.getTypeId(), model);
    }
    
    public interface ModelFactory<T extends BlockEnum>
    {
        AbstractModel createModel(T p0);
        
        T getBlockEnumFromTypeId(int p0);
        
        T getBlockEnum(String p0);
        
        String getBlockName(int p0);
    }
    
    public interface BlockEnum
    {
        int getTypeId();
        
        BlockType getBlockType();
        
        String name();
    }
}
