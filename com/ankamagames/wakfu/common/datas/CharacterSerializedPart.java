package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public abstract class CharacterSerializedPart implements VersionableObject
{
    public static final CharacterSerializedPart EMPTY;
    protected static final Logger m_logger;
    
    public abstract BinarSerialPart getBinarPart();
    
    static {
        EMPTY = new CharacterSerializedPart() {
            @Override
            public BinarSerialPart getBinarPart() {
                return BinarSerialPart.EMPTY;
            }
            
            @Override
            public boolean serialize(final ByteBuffer buffer) {
                return true;
            }
            
            @Override
            public boolean unserialize(final ByteBuffer buffer) {
                return true;
            }
            
            @Override
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                return true;
            }
            
            @Override
            public int serializedSize() {
                return 0;
            }
            
            @Override
            public void clear() {
            }
        };
        m_logger = Logger.getLogger((Class)CharacterSerializedPart.class);
    }
}
