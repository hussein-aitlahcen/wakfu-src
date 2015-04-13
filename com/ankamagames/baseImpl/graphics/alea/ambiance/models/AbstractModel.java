package com.ankamagames.baseImpl.graphics.alea.ambiance.models;

import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;
import java.nio.*;
import com.ankamagames.framework.fileFormat.io.*;
import gnu.trove.*;

public abstract class AbstractModel<T extends AbstractBlock>
{
    protected static final ModelVar[] EMPTY_VARS;
    private final int m_typeId;
    protected final ModelVar[] m_inputs;
    protected final ModelVar[] m_params;
    
    public AbstractModel(final int typeId, final ModelVar[] inputs, final ModelVar[] params) {
        super();
        this.m_typeId = typeId;
        this.m_inputs = inputs;
        this.m_params = params;
    }
    
    public AbstractModel(final int typeId) {
        this(typeId, AbstractModel.EMPTY_VARS, AbstractModel.EMPTY_VARS);
    }
    
    public final int getTypeId() {
        return this.m_typeId;
    }
    
    public abstract BlockType getBlockType();
    
    public abstract T createInstance();
    
    public final int getExpectedInputCount() {
        return this.getInputs().length;
    }
    
    public final Var[] createParams() {
        return createVars(this.m_params);
    }
    
    public final Var[] createInputs() {
        return createVars(this.m_inputs);
    }
    
    private static Var[] createVars(final ModelVar[] models) {
        if (models == AbstractModel.EMPTY_VARS) {
            return Var.EMPTY;
        }
        final int count = models.length;
        final Var[] vars = new Var[count];
        for (int i = 0; i < count; ++i) {
            vars[i] = models[i].createVariable();
        }
        return vars;
    }
    
    public final void readParams(final Var[] params, final ByteBuffer buffer) throws Exception {
        for (int i = 0; i < this.m_params.length; ++i) {
            this.m_params[i].read(params[i], buffer);
        }
    }
    
    public final void writeParams(final OutputBitStream ostream, final THashMap<String, String> params) throws Exception {
        for (int i = 0; i < this.m_params.length; ++i) {
            this.m_params[i].write(ostream, params.get(this.m_params[i].getName()));
        }
    }
    
    public final ModelVar[] getInputs() {
        return this.m_inputs;
    }
    
    public final int getInputIndex(final String name) {
        final ModelVar[] inputs = this.getInputs();
        for (int i = 0; i < inputs.length; ++i) {
            if (inputs[i].getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }
    
    public void reset(final Var[] inputs) {
        for (int i = 0; i < inputs.length; ++i) {
            inputs[i].reset();
        }
    }
    
    static {
        EMPTY_VARS = new ModelVar[0];
    }
}
