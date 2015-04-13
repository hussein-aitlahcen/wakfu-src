package com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.movementSelector;

import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.*;

public interface MovementSelector
{
    PathMovementStyle selectMovementStyle(StyleMobile p0, int p1);
    
    void onMovementEnded(StyleMobile p0);
    
    void resetMovementSelector(StyleMobile p0);
}
