package com.ankamagames.framework.graphics.engine.pools;

import java.nio.*;

public interface DirectBufferPool
{
    Buffer getBuffer();
    
    int getId();
    
    void release();
    
    public enum Type
    {
        ByteBuffer, 
        ShortBuffer, 
        FloatBuffer;
    }
}
