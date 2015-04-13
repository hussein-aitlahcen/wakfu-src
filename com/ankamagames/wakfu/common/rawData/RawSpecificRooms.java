package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;

public abstract class RawSpecificRooms implements VersionableObject
{
    public static RawSpecificRooms unserializeVirtual(final ByteBuffer buffer) {
        final byte type = buffer.get();
        switch (type) {
            case 0: {
                final RawSpecificRooms result = new RawGemControlledRoom();
                if (!result.unserialize(buffer)) {
                    return null;
                }
                return result;
            }
            default: {
                return null;
            }
        }
    }
    
    public static RawSpecificRooms unserializeVirtualVersion(final ByteBuffer buffer, final int version) {
        final byte type = buffer.get();
        switch (type) {
            case 0: {
                final RawSpecificRooms result = new RawGemControlledRoom();
                if (!result.unserializeVersion(buffer, version)) {
                    return null;
                }
                return result;
            }
            default: {
                return null;
            }
        }
    }
    
    public abstract byte getVirtualId();
    
    public abstract void internalToString(final StringBuilder p0, final String p1);
}
