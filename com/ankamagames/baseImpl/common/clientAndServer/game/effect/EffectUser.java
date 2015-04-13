package com.ankamagames.baseImpl.common.clientAndServer.game.effect;

import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.trigger.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.part.*;

public interface EffectUser extends Target, Triggerable<RunningEffect>
{
    byte getEffectUserType();
    
    boolean isValidForEffectExecution();
    
    RunningEffectManager getRunningEffectManager();
    
    byte[] serializeEffectUser();
    
    void unserializeEffectUser(byte[] p0);
    
    void setPosition(int p0, int p1, short p2);
    
    void setPosition(Point3 p0);
    
    void teleport(int p0, int p1, short p2);
    
    boolean hasCharacteristic(CharacteristicType p0);
    
    AbstractCharacteristic getCharacteristic(CharacteristicType p0);
    
    int getCharacteristicValue(CharacteristicType p0) throws UnsupportedOperationException;
    
    boolean isActiveProperty(PropertyType p0);
    
    byte getPropertyValue(PropertyType p0);
    
    void setPropertyValue(PropertyType p0, byte p1);
    
    void addProperty(PropertyType p0);
    
    void substractProperty(PropertyType p0);
    
    void removeProperty(PropertyType p0);
    
    void setDirection(Direction8 p0);
    
    Direction8 getMovementDirection();
    
    void setSpecialMovementDirection(Direction8 p0);
    
    PartLocalisator getPartLocalisator();
    
    void goOffPlay(EffectUser p0);
    
    void goBackInPlay(EffectUser p0);
    
    void goOutOfPlay(EffectUser p0);
    
    boolean mustGoBackInPlay();
    
    boolean mustGoOffPlay();
    
    void onGoesOffPlay();
    
    boolean isInPlay();
    
    boolean isOffPlay();
    
    void onBackInPlay();
    
    boolean mustGoOutOfPlay();
    
    void onGoesOutOfPlay();
    
    boolean isOutOfPlay();
    
    boolean canChangePlayStatus();
    
    void setUnderChange(boolean p0);
    
    Point3 getPosition();
    
    EffectUser getResistanceTarget();
}
