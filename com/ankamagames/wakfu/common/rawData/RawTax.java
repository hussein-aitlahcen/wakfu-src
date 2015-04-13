package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;

public class RawTax implements VersionableObject
{
    public byte taxContext;
    public float taxValue;
    public static final int SERIALIZED_SIZE = 5;
    
    public RawTax() {
        super();
        this.taxContext = 0;
        this.taxValue = 0.0f;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.put(this.taxContext);
        buffer.putFloat(this.taxValue);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.taxContext = buffer.get();
        this.taxValue = buffer.getFloat();
        return true;
    }
    
    @Override
    public void clear() {
        this.taxContext = 0;
        this.taxValue = 0.0f;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        return 5;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("taxContext=").append(this.taxContext).append('\n');
        repr.append(prefix).append("taxValue=").append(this.taxValue).append('\n');
    }
}
