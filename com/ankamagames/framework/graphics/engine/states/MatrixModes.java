package com.ankamagames.framework.graphics.engine.states;

public enum MatrixModes
{
    PROJECTION(5889), 
    TEXTURE(5890), 
    MODEL_VIEW(5888);
    
    public final int m_oglCode;
    
    private MatrixModes(final int oglCode) {
        this.m_oglCode = oglCode;
    }
}
