package com.ankamagames.framework.sound.group;

import com.ankamagames.framework.kernel.core.maths.*;

public interface ObservedListener
{
    Vector3 getListenerPosition();
    
    float getListenerDistance();
    
    int getGroupMaskKey();
}
