package com.ankamagames.framework.graphics.engine.fx.effets;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.fx.*;

public class EffectParams
{
    private static final Logger m_logger;
    public static final String COLOR_SCALE_PARAM_NAME = "gColorScale";
    private final Variable[] m_variables;
    private final int[] m_index;
    private final float[] m_values;
    
    public static EffectParams clone(final EffectParams effectParams) {
        if (effectParams == null) {
            return null;
        }
        return new EffectParams(effectParams);
    }
    
    public float getFloat(final String name) {
        final int index = this.getIndex(name);
        if (index >= 0) {
            return this.m_values[index];
        }
        return 0.0f;
    }
    
    public EffectParams(final Variable... variables) {
        super();
        (this.m_variables = new Variable[variables.length + 1])[0] = new Variable("gColorScale", Variable.VariableType.Float);
        System.arraycopy(variables, 0, this.m_variables, 1, variables.length);
        final int size = this.m_variables.length;
        this.m_index = new int[size];
        int index = 0;
        for (int i = 0; i < size; ++i) {
            this.m_index[i] = index;
            index += this.m_variables[i].m_type.getSize();
        }
        this.m_values = new float[index];
        this.setFloat("gColorScale", 2.0f);
    }
    
    private EffectParams(final EffectParams effectParams) {
        super();
        this.m_variables = effectParams.m_variables.clone();
        this.m_index = effectParams.m_index.clone();
        this.m_values = effectParams.m_values.clone();
    }
    
    public final void setFloat(final String name, final float value) {
        final int index = this.getIndex(name);
        assert this.getType(name) == Variable.VariableType.Float;
        if (index >= 0) {
            this.m_values[index] = value;
        }
    }
    
    public final void setVector2(final String name, final float x, final float y) {
        final int index = this.getIndex(name);
        assert this.getType(name) == Variable.VariableType.Vector2;
        if (index >= 0) {
            this.m_values[index] = x;
            this.m_values[index + 1] = y;
        }
    }
    
    public final void setVector3(final String name, final float x, final float y, final float z) {
        final int index = this.getIndex(name);
        assert this.getType(name) == Variable.VariableType.Vector3;
        if (index >= 0) {
            this.m_values[index] = x;
            this.m_values[index + 1] = y;
            this.m_values[index + 2] = z;
        }
    }
    
    public final void setVector4(final String name, final float x, final float y, final float z, final float w) {
        final int index = this.getIndex(name);
        assert this.getType(name) == Variable.VariableType.Vector4;
        if (index >= 0) {
            this.m_values[index] = x;
            this.m_values[index + 1] = y;
            this.m_values[index + 2] = z;
            this.m_values[index + 3] = w;
        }
    }
    
    public final void setVector3(final String name, final float[] value) {
        this.set(name, value, Variable.VariableType.Vector3);
    }
    
    public final void setVector4(final String name, final float[] value) {
        this.set(name, value, Variable.VariableType.Vector4);
    }
    
    public final void setMatrix44(final String name, final float[] value) {
        this.set(name, value, Variable.VariableType.Matrix44);
    }
    
    public final void foreach(final Procedure procedure) {
        for (int i = 0, size = this.m_variables.length; i < size; ++i) {
            procedure.execute(this.m_variables[i], this.m_index[i], this.m_values);
        }
    }
    
    private void set(final String name, final float[] value, final Variable.VariableType type) {
        final int index = this.getIndex(name);
        if (index >= 0) {
            assert this.getType(name) == type;
            assert value.length == type.getSize();
            for (int i = 0, size = value.length; i < size; ++i) {
                this.m_values[index + i] = value[i];
            }
        }
    }
    
    private int getIndex(final String name) {
        for (int i = 0, size = this.m_variables.length; i < size; ++i) {
            if (this.m_variables[i].m_name.equals(name)) {
                return this.m_index[i];
            }
        }
        EffectParams.m_logger.error((Object)("pas de variable nomm\u00e9 " + name));
        return -1;
    }
    
    private Variable.VariableType getType(final String name) {
        for (int i = 0, size = this.m_variables.length; i < size; ++i) {
            if (this.m_variables[i].m_name.equals(name)) {
                return this.m_variables[i].m_type;
            }
        }
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)EffectParams.class);
    }
    
    public interface Procedure
    {
        void execute(Variable p0, int p1, float[] p2);
    }
}
