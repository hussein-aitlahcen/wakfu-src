package com.ankamagames.xulor2.component.map;

import com.ankamagames.framework.graphics.image.*;
import org.jetbrains.annotations.*;

public interface MapZoneDescription
{
    Color getZoneColor();
    
    int getId();
    
    byte getMaskIndex();
    
    String getTextDescription();
    
    int getBorderWidth();
    
    String getIconUrl();
    
    @Nullable
    String getAnim1();
    
    @Nullable
    String getAnim2();
    
    boolean isInteractive();
    
    long getHighlightSoundId();
}
