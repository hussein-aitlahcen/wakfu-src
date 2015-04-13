package com.ankamagames.baseImpl.common.clientAndServer.rawData;

import java.nio.*;

public interface SerializableObject
{
    boolean serialize(ByteBuffer p0);
    
    boolean unserialize(ByteBuffer p0);
    
    int serializedSize();
    
    void clear();
}
