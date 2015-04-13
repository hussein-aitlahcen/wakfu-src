package com.ankamagames.baseImpl.common.clientAndServer.game.part;

import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;

public interface PartLocalisator<P extends Part>
{
    P getPartFromId(int p0);
    
    List<P> getPartsInSightFromPoint(int p0, int p1, short p2);
    
    P getMainPartInSightFromPosition(int p0, int p1, short p2);
    
    P getMainPartInSightFromVector(Vector3 p0);
    
    void reset();
}
