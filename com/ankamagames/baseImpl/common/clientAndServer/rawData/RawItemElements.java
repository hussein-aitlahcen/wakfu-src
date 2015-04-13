package com.ankamagames.baseImpl.common.clientAndServer.rawData;

import java.nio.*;

public class RawItemElements implements VersionableObject
{
    public byte damageElements;
    public byte resistanceElements;
    public static final int SERIALIZED_SIZE = 2;
    
    public RawItemElements() {
        super();
        this.damageElements = 0;
        this.resistanceElements = 0;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.put(this.damageElements);
        buffer.put(this.resistanceElements);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.damageElements = buffer.get();
        this.resistanceElements = buffer.get();
        return true;
    }
    
    @Override
    public void clear() {
        this.damageElements = 0;
        this.resistanceElements = 0;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        return 2;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("damageElements=").append(this.damageElements).append('\n');
        repr.append(prefix).append("resistanceElements=").append(this.resistanceElements).append('\n');
    }
}
