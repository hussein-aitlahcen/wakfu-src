package com.ankamagames.baseImpl.graphics.opengl;

import org.apache.log4j.*;
import java.awt.im.*;
import com.ankamagames.framework.graphics.opengl.*;
import com.sun.opengl.util.*;
import javax.media.opengl.*;
import java.awt.event.*;

public class GLAWTWorkspace extends GLCanvas
{
    private static Logger m_logger;
    private InputMethodRequests m_currentInputMethodRequests;
    private Renderer m_renderer;
    private Animator m_animator;
    
    private static GLCapabilities createCapabilities(final GLCaps caps) {
        final GLCapabilities capabilities = new GLCapabilities();
        capabilities.setHardwareAccelerated(true);
        capabilities.setDoubleBuffered(caps.m_useDoubleBuffer);
        capabilities.setSampleBuffers(false);
        capabilities.setDepthBits(caps.m_numDepthBufferBits);
        capabilities.setStencilBits(caps.m_numStencilBits);
        switch (caps.m_bpp) {
            case 16: {
                capabilities.setRedBits(4);
                capabilities.setGreenBits(4);
                capabilities.setBlueBits(4);
                capabilities.setAlphaBits(4);
                break;
            }
            case 32: {
                capabilities.setAlphaBits(8);
            }
            case 24: {
                capabilities.setRedBits(8);
                capabilities.setGreenBits(8);
                capabilities.setBlueBits(8);
                break;
            }
        }
        return capabilities;
    }
    
    public GLAWTWorkspace(final GLCaps caps) {
        super(createCapabilities(caps));
        this.m_animator = new Animator((GLAutoDrawable)this);
    }
    
    public Animator getAnimator() {
        return this.m_animator;
    }
    
    public Renderer getRenderer() {
        return this.m_renderer;
    }
    
    public void setRenderer(final Renderer renderer) {
        if (renderer != this.m_renderer && renderer != null) {
            if (this.m_renderer != null) {
                this.removeGLEventListener((GLEventListener)this.m_renderer);
                this.removeMouseListener((MouseListener)this.m_renderer);
                this.removeMouseMotionListener((MouseMotionListener)this.m_renderer);
                this.removeKeyListener((KeyListener)this.m_renderer);
                this.removeFocusListener((FocusListener)this.m_renderer);
            }
            this.addGLEventListener((GLEventListener)(this.m_renderer = renderer));
            this.addMouseListener((MouseListener)this.m_renderer);
            this.addMouseMotionListener((MouseMotionListener)this.m_renderer);
            this.addMouseWheelListener((MouseWheelListener)this.m_renderer);
            this.addKeyListener((KeyListener)this.m_renderer);
            this.addFocusListener((FocusListener)this.m_renderer);
        }
    }
    
    public synchronized void addInputMethodListener(final InputMethodListener l) {
        super.addInputMethodListener(l);
        this.enableInputMethods(true);
    }
    
    public synchronized void removeInputMethodListener(final InputMethodListener l) {
        super.removeInputMethodListener(l);
        this.enableInputMethods(false);
    }
    
    public InputMethodRequests getCurrentInputMethodRequests() {
        return this.m_currentInputMethodRequests;
    }
    
    public void setCurrentInputMethodRequests(final InputMethodRequests currentInputMethodRequests) {
        this.m_currentInputMethodRequests = currentInputMethodRequests;
    }
    
    public InputMethodRequests getInputMethodRequests() {
        return this.m_currentInputMethodRequests;
    }
    
    static {
        GLAWTWorkspace.m_logger = Logger.getLogger((Class)GLAWTWorkspace.class);
    }
}
