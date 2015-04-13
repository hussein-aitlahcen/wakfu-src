package com.ankamagames.framework.graphics.opengl.events;

public class OpenGLVersionTooOldEvent extends GLRendererEvent
{
    private int m_majorExpected;
    private int m_minorExpected;
    private int m_majorAvailable;
    private int m_minorAvailable;
    
    public OpenGLVersionTooOldEvent(final int majorExpected, final int minorExpected, final int majorAvailable, final int minorAvailable) {
        super(EventType.OPENGL_VERSION_TOO_OLD);
        this.m_majorExpected = majorExpected;
        this.m_minorExpected = minorExpected;
        this.m_majorAvailable = majorAvailable;
        this.m_minorAvailable = minorAvailable;
    }
    
    public int getMajorExpected() {
        return this.m_majorExpected;
    }
    
    public int getMinorExpected() {
        return this.m_minorExpected;
    }
    
    public int getMajorAvailable() {
        return this.m_majorAvailable;
    }
    
    public int getMinorAvailable() {
        return this.m_minorAvailable;
    }
    
    @Override
    public String toString() {
        return '{' + this.getClass().getSimpleName() + " : OpenGL version " + this.m_majorExpected + '.' + this.m_minorExpected + " not available : " + this.m_majorAvailable + '.' + this.m_minorAvailable + " present on system}";
    }
}
