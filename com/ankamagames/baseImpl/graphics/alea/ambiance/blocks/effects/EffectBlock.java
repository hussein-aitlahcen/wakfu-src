package com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.effects;

import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;

public class EffectBlock extends AbstractBlock<AbstractEffectModel>
{
    protected static final Logger m_logger;
    protected EffectBlockParameter m_activator;
    private float m_coeff;
    
    public EffectBlock(final AbstractEffectModel model) {
        super(model);
        this.m_coeff = 1.0f;
        ((AbstractEffectModel)this.m_model).reset(this.m_inputs);
    }
    
    public float getCoeff() {
        return this.m_coeff;
    }
    
    public final void multCoeff(final float c) {
        this.m_coeff *= c;
    }
    
    public void setCoeff(final float coeff) {
        this.m_coeff = coeff;
    }
    
    public void compute() {
        if (!this.isActive()) {
            return;
        }
        for (int i = 0; i < this.m_params.length; ++i) {
            this.m_params[i].set(this.m_parameters[i]);
        }
    }
    
    @Override
    public final void update(final int deltaTime) {
    }
    
    public final void setActivator(final EffectBlockParameter effectBlockParameter) {
        this.m_activator = effectBlockParameter;
    }
    
    public boolean isActive() {
        return this.m_activator == null || this.m_activator.floatValue() == 1.0f;
    }
    
    static {
        m_logger = Logger.getLogger((Class)EffectBlock.class);
    }
}
