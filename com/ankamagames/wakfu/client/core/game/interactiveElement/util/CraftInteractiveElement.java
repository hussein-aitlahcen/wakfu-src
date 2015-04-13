package com.ankamagames.wakfu.client.core.game.interactiveElement.util;

import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;

public interface CraftInteractiveElement
{
    boolean isRecipeAllowed(int p0, byte p1);
    
    void notifyViews();
    
    int getWorldCellX();
    
    int getWorldCellY();
    
    int getVisualId();
    
    long getId();
    
    int getCraftId();
    
    String getName();
    
    void setState(short p0);
    
    boolean containsRecipe(int p0);
    
    List<Point3> getApproachPoints();
}
