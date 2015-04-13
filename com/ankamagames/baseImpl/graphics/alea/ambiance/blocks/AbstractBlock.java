package com.ankamagames.baseImpl.graphics.alea.ambiance.blocks;

import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;

public abstract class AbstractBlock<M extends AbstractModel>
{
    protected final Var[] m_inputs;
    protected final Var[] m_params;
    protected EffectBlockParameter[] m_parameters;
    protected final M m_model;
    
    protected AbstractBlock(final M model) {
        super();
        this.m_model = model;
        this.m_inputs = model.createInputs();
        this.m_params = model.createParams();
    }
    
    public final Var getVar(final int index) {
        return this.m_params[index];
    }
    
    public final Var getInput(final int index) {
        return this.m_inputs[index];
    }
    
    public final int getTypeId() {
        return this.m_model.getTypeId();
    }
    
    public final BlockType getBlockType() {
        return this.m_model.getBlockType();
    }
    
    public int getExpectedInputCount() {
        return this.m_model.getExpectedInputCount();
    }
    
    public void setInputs(final EffectBlockParameter[] parameters) {
        assert parameters.length == this.getExpectedInputCount();
        this.m_parameters = parameters;
    }
    
    public abstract void update(final int p0);
}
