package com.ankamagames.xulor2.util.sound;

public interface XulorSoundManagementDelegate
{
    public static final int BUTTON_CLICK = -1;
    public static final int TAB_BUTTON_CLICK = -2;
    public static final int UNSET_SOUND = -3;
    
    void setGUISoundGain(float p0);
    
    float getGUISoundGain();
    
    void setGUISoundMute(boolean p0);
    
    boolean getGUISoundMute();
    
    void playSound(long p0);
    
    void click();
    
    void tabClick();
    
    void toggleButtonSelect();
    
    void toggleButtonUnselect();
    
    void comboBoxClick();
    
    void comboBoxClose();
    
    void rollOver();
    
    void popup();
    
    void alert();
    
    int getMessageBoxYesButtonId();
    
    int getMessageBoxNoButtonId();
    
    int getDecreaseButtonId();
    
    int getIncreaseButtonId();
}
