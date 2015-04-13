package com.ankamagames.baseImpl.common.clientAndServer.rawData;

public interface RawConvertible<RawType>
{
    boolean toRaw(RawType p0);
    
    boolean fromRaw(RawType p0);
}
