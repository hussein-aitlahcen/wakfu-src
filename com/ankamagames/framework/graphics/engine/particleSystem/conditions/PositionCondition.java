package com.ankamagames.framework.graphics.engine.particleSystem.conditions;

import com.ankamagames.framework.graphics.engine.particleSystem.*;

public class PositionCondition implements AffectorCondition
{
    public final int m_value;
    public final PositionConditionMode m_condition;
    public final boolean m_useSystemAsReference;
    
    public PositionCondition(final int value, final PositionConditionMode condition, final boolean useSystemAsReference) {
        super();
        this.m_value = value;
        this.m_condition = condition;
        this.m_useSystemAsReference = useSystemAsReference;
    }
    
    @Override
    public boolean validate(final Particle reference, final Particle target, final float timeIncrement, final ParticleSystem particleSystem) {
        switch (this.m_condition) {
            case X_GREATER: {
                return target.m_x >= this.m_value + (this.m_useSystemAsReference ? particleSystem.m_cellX : reference.m_x);
            }
            case X_LESS: {
                return target.m_x <= this.m_value + (this.m_useSystemAsReference ? particleSystem.m_cellX : reference.m_x);
            }
            case Y_GREATER: {
                return target.m_y >= this.m_value + (this.m_useSystemAsReference ? particleSystem.m_cellY : reference.m_y);
            }
            case Y_LESS: {
                return target.m_y <= this.m_value + (this.m_useSystemAsReference ? particleSystem.m_cellY : reference.m_y);
            }
            case Z_GREATER: {
                return target.m_z >= this.m_value + (this.m_useSystemAsReference ? particleSystem.m_cellZ : reference.m_z);
            }
            case Z_LESS: {
                return target.m_z <= this.m_value + (this.m_useSystemAsReference ? particleSystem.m_cellZ : reference.m_z);
            }
            default: {
                return true;
            }
        }
    }
    
    public enum PositionConditionMode
    {
        X_GREATER, 
        X_LESS, 
        Y_GREATER, 
        Y_LESS, 
        Z_GREATER, 
        Z_LESS;
    }
}
