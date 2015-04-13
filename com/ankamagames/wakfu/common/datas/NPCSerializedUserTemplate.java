package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public class NPCSerializedUserTemplate extends CharacterSerializedPart implements VersionableObject
{
    public short sightRadius;
    public short aggroRadius;
    private final BinarSerialPart m_binarPart;
    public static final int SERIALIZED_SIZE = 4;
    
    public NPCSerializedUserTemplate() {
        super();
        this.sightRadius = 0;
        this.aggroRadius = 0;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = NPCSerializedUserTemplate.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de NPCSerializedUserTemplate");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de NPCSerializedUserTemplate", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = NPCSerializedUserTemplate.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de NPCSerializedUserTemplate");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de NPCSerializedUserTemplate", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return NPCSerializedUserTemplate.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putShort(this.sightRadius);
        buffer.putShort(this.aggroRadius);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.sightRadius = buffer.getShort();
        this.aggroRadius = buffer.getShort();
        return true;
    }
    
    @Override
    public void clear() {
        this.sightRadius = 0;
        this.aggroRadius = 0;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        return 4;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("sightRadius=").append(this.sightRadius).append('\n');
        repr.append(prefix).append("aggroRadius=").append(this.aggroRadius).append('\n');
    }
}
