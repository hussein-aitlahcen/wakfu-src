package com.ankamagames.xulor2.core;

import com.ankamagames.xulor2.util.sound.*;

public class XulorSoundManager
{
    private static final XulorSoundManager m_instance;
    private XulorSoundManagementDelegate m_delegate;
    
    public static XulorSoundManager getInstance() {
        return XulorSoundManager.m_instance;
    }
    
    public void setDelegate(final XulorSoundManagementDelegate delegate) {
        this.m_delegate = delegate;
    }
    
    public void click() {
        assert this.m_delegate != null : "XulorSoundManagementDelegate non d\u00e9fini";
        if (this.m_delegate != null) {
            this.m_delegate.click();
        }
    }
    
    public void comboBoxClick() {
        assert this.m_delegate != null : "XulorSoundManagementDelegate non d\u00e9fini";
        if (this.m_delegate != null) {
            this.m_delegate.comboBoxClick();
        }
    }
    
    public void comboBoxClose() {
        assert this.m_delegate != null : "XulorSoundManagementDelegate non d\u00e9fini";
        if (this.m_delegate != null) {
            this.m_delegate.comboBoxClose();
        }
    }
    
    public void tabClick() {
        assert this.m_delegate != null : "XulorSoundManagementDelegate non d\u00e9fini";
        if (this.m_delegate != null) {
            this.m_delegate.tabClick();
        }
    }
    
    public void toggleButtonSelect() {
        assert this.m_delegate != null : "XulorSoundManagementDelegate non d\u00e9fini";
        if (this.m_delegate != null) {
            this.m_delegate.toggleButtonSelect();
        }
    }
    
    public void toggleButtonUnselect() {
        assert this.m_delegate != null : "XulorSoundManagementDelegate non d\u00e9fini";
        if (this.m_delegate != null) {
            this.m_delegate.toggleButtonUnselect();
        }
    }
    
    public void rollOver() {
        assert this.m_delegate != null : "XulorSoundManagementDelegate non d\u00e9fini";
        if (this.m_delegate != null) {
            this.m_delegate.rollOver();
        }
    }
    
    public void popup() {
        assert this.m_delegate != null : "XulorSoundManagementDelegate non d\u00e9fini";
        if (this.m_delegate != null) {
            this.m_delegate.popup();
        }
    }
    
    public void alert() {
        assert this.m_delegate != null : "XulorSoundManagementDelegate non d\u00e9fini";
        if (this.m_delegate != null) {
            this.m_delegate.alert();
        }
    }
    
    public void playSound(final long soundId) {
        assert this.m_delegate != null : "XulorSoundManagementDelegate non d\u00e9fini";
        if (this.m_delegate != null) {
            this.m_delegate.playSound(soundId);
        }
    }
    
    public int getIncreaseButtonId() {
        assert this.m_delegate != null : "XulorSoundManagementDelegate non d\u00e9fini";
        if (this.m_delegate != null) {
            return this.m_delegate.getIncreaseButtonId();
        }
        return -1;
    }
    
    public int getDecreaseButtonId() {
        assert this.m_delegate != null : "XulorSoundManagementDelegate non d\u00e9fini";
        if (this.m_delegate != null) {
            return this.m_delegate.getDecreaseButtonId();
        }
        return -1;
    }
    
    public int getMessageBoxYesButtonId() {
        assert this.m_delegate != null : "XulorSoundManagementDelegate non d\u00e9fini";
        if (this.m_delegate != null) {
            return this.m_delegate.getMessageBoxYesButtonId();
        }
        return -1;
    }
    
    public int getMessageBoxNoButtonId() {
        assert this.m_delegate != null : "XulorSoundManagementDelegate non d\u00e9fini";
        if (this.m_delegate != null) {
            return this.m_delegate.getMessageBoxNoButtonId();
        }
        return -1;
    }
    
    static {
        m_instance = new XulorSoundManager();
    }
}
