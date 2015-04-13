package com.ankamagames.baseImpl.common.clientAndServer.game.dialog;

import com.ankamagames.baseImpl.common.clientAndServer.game.gameAction.*;
import com.ankamagames.framework.kernel.core.maths.*;

public interface DialogSource extends ActionSource
{
    Point3 getPosition();
    
    long getDialogSourceId();
    
    AbstractDialogSourceType getDialogSourceType();
}
