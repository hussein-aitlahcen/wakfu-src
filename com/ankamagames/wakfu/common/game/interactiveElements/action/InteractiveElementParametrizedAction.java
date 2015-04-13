package com.ankamagames.wakfu.common.game.interactiveElements.action;

import java.nio.*;

public interface InteractiveElementParametrizedAction
{
    byte[] serialize();
    
    void unserialize(ByteBuffer p0);
    
    short getId();
}
