package com.ankamagames.wakfu.client.sound;

import com.ankamagames.xulor2.util.sound.*;

class XulorSoundDelegate implements XulorSoundManagementDelegate
{
    final WakfuSoundManager m_soundManager;
    
    XulorSoundDelegate(final WakfuSoundManager soundManager) {
        super();
        this.m_soundManager = soundManager;
    }
    
    @Override
    public void setGUISoundGain(final float gain) {
        if (!this.m_soundManager.isRunning()) {
            return;
        }
        GameSoundGroup.GUI.getDefaultGroup().setGain(gain);
    }
    
    @Override
    public float getGUISoundGain() {
        return GameSoundGroup.GUI.getDefaultGroup().getGain();
    }
    
    @Override
    public void setGUISoundMute(final boolean mute) {
        if (!this.m_soundManager.isRunning()) {
            return;
        }
        GameSoundGroup.GUI.getDefaultGroup().setMute(mute);
    }
    
    @Override
    public boolean getGUISoundMute() {
        return GameSoundGroup.GUI.getDefaultGroup().isMute();
    }
    
    public void playGUISound(final long soundId) {
        if (!this.m_soundManager.isRunning()) {
            return;
        }
        this.m_soundManager.playGUISound(soundId);
    }
    
    @Override
    public void click() {
        if (!this.m_soundManager.isRunning()) {
            return;
        }
        this.m_soundManager.playGUISound(600004L);
    }
    
    @Override
    public void tabClick() {
        if (!this.m_soundManager.isRunning()) {
            return;
        }
        this.m_soundManager.playGUISound(600009L);
    }
    
    @Override
    public void comboBoxClick() {
        if (!this.m_soundManager.isRunning()) {
            return;
        }
        this.m_soundManager.playGUISound(600117L, false, 50);
    }
    
    @Override
    public void comboBoxClose() {
        if (!this.m_soundManager.isRunning()) {
            return;
        }
        this.m_soundManager.playGUISound(600118L, false, 50);
    }
    
    @Override
    public void toggleButtonSelect() {
        if (!this.m_soundManager.isRunning()) {
            return;
        }
        this.m_soundManager.playGUISound(600006L);
    }
    
    @Override
    public void toggleButtonUnselect() {
        if (!this.m_soundManager.isRunning()) {
            return;
        }
        this.m_soundManager.playGUISound(600007L);
    }
    
    @Override
    public void rollOver() {
        if (!this.m_soundManager.isRunning()) {
            return;
        }
        this.m_soundManager.playGUISound(600010L);
    }
    
    @Override
    public void popup() {
        if (!this.m_soundManager.isRunning()) {
            return;
        }
    }
    
    @Override
    public void alert() {
        if (!this.m_soundManager.isRunning()) {
            return;
        }
        this.m_soundManager.playGUISound(600011L);
    }
    
    @Override
    public void playSound(final long soundId) {
        if (!this.m_soundManager.isRunning()) {
            return;
        }
        this.m_soundManager.playGUISound(soundId);
    }
    
    @Override
    public int getDecreaseButtonId() {
        return 600015;
    }
    
    @Override
    public int getIncreaseButtonId() {
        return 600014;
    }
    
    @Override
    public int getMessageBoxYesButtonId() {
        return 600082;
    }
    
    @Override
    public int getMessageBoxNoButtonId() {
        return 600083;
    }
}
