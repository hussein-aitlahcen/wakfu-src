package com.ankamagames.framework.kernel.events;

import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;

public interface MessageFrame extends MessageHandler
{
    void onFrameAdd(FrameHandler p0, boolean p1);
    
    void onFrameRemove(FrameHandler p0, boolean p1);
}
