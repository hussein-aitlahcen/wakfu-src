package com.ankamagames.framework.graphics.engine.opengl;

import javax.media.opengl.*;

public final class GLStateManager
{
    public static final int EMPTY_ARRAY = 0;
    public static final int POSITION_ARRAY = 1;
    public static final int NORMAL_ARRAY = 2;
    public static final int COLOR_ARRAY = 4;
    public static final int TEXCOORD_ARRAY = 8;
    private GL m_gl;
    private int m_arrayType;
    
    public GLStateManager() {
        super();
        this.m_arrayType = 0;
    }
    
    public void setDevice(final GL gl) {
        this.m_gl = gl;
    }
    
    public void setVertexArrayComponents(final int arrayType) {
        if (arrayType == this.m_arrayType) {
            return;
        }
        if (arrayType == 0) {
            if ((this.m_arrayType & 0x1) != 0x0) {
                this.m_gl.glDisableClientState(32884);
            }
            if ((this.m_arrayType & 0x2) != 0x0) {
                this.m_gl.glDisableClientState(32885);
            }
            if ((this.m_arrayType & 0x4) != 0x0) {
                this.m_gl.glDisableClientState(32886);
            }
            if ((this.m_arrayType & 0x8) != 0x0) {
                this.m_gl.glDisableClientState(32888);
            }
            this.m_arrayType = arrayType;
            return;
        }
        this.m_arrayType = arrayType;
        if ((this.m_arrayType & 0x1) != 0x0) {
            this.m_gl.glEnableClientState(32884);
        }
        if ((this.m_arrayType & 0x2) != 0x0) {
            this.m_gl.glEnableClientState(32885);
        }
        if ((this.m_arrayType & 0x4) != 0x0) {
            this.m_gl.glEnableClientState(32886);
        }
        if ((this.m_arrayType & 0x8) != 0x0) {
            this.m_gl.glEnableClientState(32888);
        }
    }
}
