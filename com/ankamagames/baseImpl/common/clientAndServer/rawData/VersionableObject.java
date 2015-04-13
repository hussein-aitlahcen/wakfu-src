package com.ankamagames.baseImpl.common.clientAndServer.rawData;

import java.nio.*;

public interface VersionableObject extends SerializableObject
{
    boolean unserializeVersion(ByteBuffer p0, int p1);
}
