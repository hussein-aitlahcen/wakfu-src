package com.ankamagames.framework.kernel.core.common.message;

import com.ankamagames.framework.kernel.core.common.*;

public interface MessageHandler extends Validable
{
    boolean onMessage(Message p0);
}
