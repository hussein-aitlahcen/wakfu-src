package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public class NPCSerializedCharacteristics extends CharacterSerializedPart implements VersionableObject
{
    public short level;
    public byte[] states;
    private final BinarSerialPart m_binarPart;
    
    public NPCSerializedCharacteristics() {
        super();
        this.level = 0;
        this.states = null;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = NPCSerializedCharacteristics.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de NPCSerializedCharacteristics");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de NPCSerializedCharacteristics", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = NPCSerializedCharacteristics.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de NPCSerializedCharacteristics");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de NPCSerializedCharacteristics", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return NPCSerializedCharacteristics.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putShort(this.level);
        if (this.states != null) {
            if (this.states.length > 65535) {
                return false;
            }
            buffer.putShort((short)this.states.length);
            buffer.put(this.states);
        }
        else {
            buffer.putShort((short)0);
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.level = buffer.getShort();
        final int states_size = buffer.getShort() & 0xFFFF;
        if (states_size > 0) {
            buffer.get(this.states = new byte[states_size]);
        }
        else {
            this.states = null;
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.level = 0;
        this.states = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 2;
        size += 2;
        size += ((this.states != null) ? this.states.length : 0);
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("level=").append(this.level).append('\n');
        repr.append(prefix).append("states=(").append((this.states != null) ? this.states.length : 0).append(" bytes)\n");
    }
}
