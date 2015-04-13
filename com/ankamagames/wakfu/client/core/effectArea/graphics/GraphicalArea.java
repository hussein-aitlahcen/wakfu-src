package com.ankamagames.wakfu.client.core.effectArea.graphics;

import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import org.jetbrains.annotations.*;

public interface GraphicalArea
{
    long getId();
    
    boolean hasAPS();
    
    String getAPS();
    
    String getSpecificAPSForCaster();
    
    boolean hasSpecificAPSForCaster();
    
    String getSpecificAPSForAllies();
    
    boolean hasSpecificAPSForAllies();
    
    IsoWorldTarget getAPSSpecificTarget();
    
    boolean hasCellAPS();
    
    String getCellAPS();
    
    int getAPSLevel();
    
    void setCellAps(String p0);
    
    void setAPSLevel(int p0);
    
    boolean hasCellTexture();
    
    String getCellTextureFile();
    
    boolean hasAnimation();
    
    String getAnimationFile();
    
    void setAnimatedElement(AnimatedInteractiveElement p0);
    
    long setAnimation(String p0);
    
    AnimatedInteractiveElement getAnimatedElement();
    
    void copy(GraphicalArea p0);
    
    void setApsSpecificTarget(IsoWorldTarget p0);
    
    void setAps(String p0);
    
    @NotNull
    AbstractEffectArea getLinkedArea();
    
    void addAnimatedElementObserver(GraphicalAreaAnimatedElementObserver p0);
    
    void removeAnimatedElementObserver(GraphicalAreaAnimatedElementObserver p0);
    
    short getVisualHeight();
    
    boolean shouldPlayDeathAnimation();
    
    void setShouldPlayDeathAnimation(boolean p0);
    
    String getName();
}
