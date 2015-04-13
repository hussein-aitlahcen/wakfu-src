package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public class CharacterSerializedRespawnPoint extends CharacterSerializedPart implements VersionableObject
{
    public int x;
    public int y;
    public short z;
    public short instanceId;
    private final BinarSerialPart m_binarPart;
    public static final int SERIALIZED_SIZE = 12;
    
    public CharacterSerializedRespawnPoint() {
        super();
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.instanceId = 0;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedRespawnPoint.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedRespawnPoint");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedRespawnPoint", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedRespawnPoint.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedRespawnPoint");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedRespawnPoint", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedRespawnPoint.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putInt(this.x);
        buffer.putInt(this.y);
        buffer.putShort(this.z);
        buffer.putShort(this.instanceId);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.x = buffer.getInt();
        this.y = buffer.getInt();
        this.z = buffer.getShort();
        this.instanceId = buffer.getShort();
        return true;
    }
    
    @Override
    public void clear() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.instanceId = 0;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        return 12;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("x=").append(this.x).append('\n');
        repr.append(prefix).append("y=").append(this.y).append('\n');
        repr.append(prefix).append("z=").append(this.z).append('\n');
        repr.append(prefix).append("instanceId=").append(this.instanceId).append('\n');
    }
}
