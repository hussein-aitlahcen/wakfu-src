package com.ankamagames.xulor2.core.windowStick;

public class WindowStickData
{
    private int m_stickId;
    private WindowStickManager.StickAlignment m_align;
    private final boolean m_isMainWindow;
    
    public WindowStickData() {
        this(false);
    }
    
    public WindowStickData(final boolean mainWindow) {
        super();
        this.m_isMainWindow = mainWindow;
    }
    
    public boolean isMainWindow() {
        return this.m_isMainWindow;
    }
    
    public int getStickId() {
        return this.m_stickId;
    }
    
    public void setStickId(final int stickId) {
        this.m_stickId = stickId;
    }
    
    public WindowStickManager.StickAlignment getAlign() {
        return this.m_align;
    }
    
    public void setAlign(final WindowStickManager.StickAlignment align) {
        this.m_align = align;
    }
}
