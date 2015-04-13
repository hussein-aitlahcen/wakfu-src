package com.ankamagames.framework.graphics.engine.states;

import org.apache.log4j.*;
import java.util.*;
import javax.media.opengl.*;

public class StencilStateManager
{
    private static final Logger m_logger;
    private final StencilParam m_default;
    private final ArrayList<StencilParam> m_stencilStack;
    private static final StencilStateManager m_instance;
    
    public void clear(final GL gl) {
        if (this.m_stencilStack.size() > 0) {
            this.applyStencilParam(gl, this.m_default, this.m_stencilStack.get(this.m_stencilStack.size() - 1));
            this.m_stencilStack.clear();
        }
        gl.glDisable(2960);
        gl.glStencilMask(-1);
        gl.glClearStencil(0);
        gl.glClear(17408);
    }
    
    public static StencilStateManager getInstance() {
        return StencilStateManager.m_instance;
    }
    
    private StencilStateManager() {
        super();
        this.m_default = new StencilParam();
        this.m_stencilStack = new ArrayList<StencilParam>(3);
    }
    
    public void pushStencil(final GL gl, final StencilParam params) {
        final int size = this.m_stencilStack.size();
        if (size == 0) {
            this.applyStencilParam(gl, params, this.m_default);
        }
        else {
            this.applyStencilParam(gl, params, this.m_stencilStack.get(size - 1));
        }
        this.m_stencilStack.add(params);
    }
    
    public void popStencilParam(final GL gl) {
        this.popStencilParam(gl, 1);
    }
    
    public void popStencilParam(final GL gl, final int count) {
        assert count > 0;
        int size = this.m_stencilStack.size();
        if (size == 0) {
            return;
        }
        StencilParam removed = null;
        for (int i = 0; i < count; ++i) {
            removed = this.m_stencilStack.remove(--size);
        }
        assert size == this.m_stencilStack.size();
        if (size == 0) {
            this.applyStencilParam(gl, this.m_default, removed);
        }
        else {
            final StencilParam cur = this.m_stencilStack.get(size - 1);
            this.applyStencilParam(gl, cur, removed);
        }
    }
    
    private void applyStencilParam(final GL gl, final StencilParam cur, final StencilParam last) {
        if (cur.m_mask != last.m_mask) {
            gl.glStencilMask(cur.m_mask);
        }
        if (cur.m_colorMask != last.m_colorMask) {
            gl.glColorMask(cur.m_colorMask, cur.m_colorMask, cur.m_colorMask, cur.m_colorMask);
        }
        if (cur.m_enable != last.m_enable) {
            if (cur.m_enable) {
                gl.glEnable(2960);
            }
            else {
                gl.glDisable(2960);
            }
        }
        if (cur.m_op != last.m_op) {
            gl.glStencilOp(cur.m_op, 7680, 7680);
        }
        if (cur.m_func != last.m_func || cur.m_funcValue != last.m_funcValue || cur.m_funcMask != last.m_funcMask) {
            gl.glStencilFunc(cur.m_func, cur.m_funcValue, cur.m_funcMask);
        }
        gl.glFlush();
    }
    
    static {
        m_logger = Logger.getLogger((Class)StencilStateManager.class);
        m_instance = new StencilStateManager();
    }
    
    public static final class StencilParam
    {
        int m_mask;
        boolean m_enable;
        int m_op;
        int m_func;
        int m_funcValue;
        int m_funcMask;
        boolean m_colorMask;
        
        public StencilParam() {
            super();
            this.m_mask = 0;
            this.m_enable = false;
            this.m_op = 7680;
            this.m_func = 519;
            this.m_funcValue = 0;
            this.m_funcMask = -1;
            this.m_colorMask = true;
        }
        
        public void setMask(final int mask) {
            this.m_mask = mask;
        }
        
        public void setEnable(final boolean enable) {
            this.m_enable = enable;
        }
        
        public void setOp(final int op0) {
            this.m_op = op0;
        }
        
        public void setFunc(final int func, final int value, final int mask) {
            this.m_func = func;
            this.m_funcValue = value;
            this.m_funcMask = mask;
        }
        
        public void setColorMask(final boolean colorMask) {
            this.m_colorMask = colorMask;
        }
    }
}
