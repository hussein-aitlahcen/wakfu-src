package com.ankamagames.framework.kernel.core.common.message;

public interface MessageRunner<M extends Message>
{
    boolean run(M p0);
    
    int getProtocolId();
}
