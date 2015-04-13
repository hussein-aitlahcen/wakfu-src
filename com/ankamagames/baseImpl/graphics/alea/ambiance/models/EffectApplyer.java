package com.ankamagames.baseImpl.graphics.alea.ambiance.models;

import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.effects.*;

public abstract class EffectApplyer
{
    private final AbstractModel m_model;
    protected final Var[] m_inputs;
    protected IsoCamera m_camera;
    
    public EffectApplyer(final AbstractModel model) {
        super();
        this.m_model = model;
        this.m_inputs = model.createInputs();
    }
    
    protected final String getInputName(final int index) {
        return this.m_model.m_inputs[index].getName();
    }
    
    public abstract void apply();
    
    public abstract void stop();
    
    public abstract void start();
    
    public void init(final AleaWorldScene scene) {
        this.m_camera = scene.getIsoCamera();
    }
    
    public void combine(final ArrayList<EffectBlock> currents) {
        this.m_model.reset(this.m_inputs);
        for (int size = currents.size(), i = 0; i < size; ++i) {
            final EffectBlock block = currents.get(i);
        }
    }
}
