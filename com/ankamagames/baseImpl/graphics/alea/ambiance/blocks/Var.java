package com.ankamagames.baseImpl.graphics.alea.ambiance.blocks;

import com.ankamagames.framework.kernel.core.maths.equations.*;

public abstract class Var
{
    public static final Var[] EMPTY;
    
    public abstract float floatValue();
    
    public abstract String stringValue();
    
    public abstract void set(final EffectBlockParameter p0);
    
    public abstract void reset();
    
    public abstract void setFloat(final float p0);
    
    static {
        EMPTY = new Var[0];
    }
    
    public static final class VByte extends Var
    {
        public byte m_value;
        
        @Override
        public float floatValue() {
            return this.m_value;
        }
        
        @Override
        public String stringValue() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void reset() {
            this.m_value = 0;
        }
        
        @Override
        public void setFloat(final float value) {
            this.m_value = (byte)value;
        }
        
        @Override
        public void set(final EffectBlockParameter effectBlockParameter) {
            this.m_value = (byte)effectBlockParameter.floatValue();
        }
    }
    
    public static final class VShort extends Var
    {
        public short m_value;
        
        @Override
        public float floatValue() {
            return this.m_value;
        }
        
        @Override
        public String stringValue() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void reset() {
            this.m_value = 0;
        }
        
        @Override
        public void setFloat(final float value) {
            this.m_value = (short)value;
        }
        
        @Override
        public void set(final EffectBlockParameter effectBlockParameter) {
            this.m_value = (short)effectBlockParameter.floatValue();
        }
    }
    
    public static final class VFloat extends Var
    {
        public float m_value;
        
        @Override
        public float floatValue() {
            return this.m_value;
        }
        
        @Override
        public String stringValue() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void reset() {
            this.m_value = 0.0f;
        }
        
        @Override
        public void setFloat(final float value) {
            this.m_value = value;
        }
        
        @Override
        public void set(final EffectBlockParameter effectBlockParameter) {
            this.m_value = effectBlockParameter.floatValue();
        }
    }
    
    public static final class VString extends Var
    {
        public String m_value;
        
        @Override
        public float floatValue() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public String stringValue() {
            return this.m_value;
        }
        
        @Override
        public void reset() {
            this.m_value = "";
        }
        
        @Override
        public void setFloat(final float value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void set(final EffectBlockParameter effectBlockParameter) {
            this.m_value = effectBlockParameter.stringValue();
        }
    }
    
    public static final class VSpline extends Var
    {
        public Spline m_value;
        
        @Override
        public float floatValue() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public String stringValue() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setFloat(final float value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void reset() {
            this.m_value = null;
        }
        
        @Override
        public void set(final EffectBlockParameter effectBlockParameter) {
        }
        
        public final Float compute(final float value) {
            return this.m_value.compute(value);
        }
    }
}
