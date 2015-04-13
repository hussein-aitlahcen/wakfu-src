package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public class NPCCompanionInfo extends CharacterSerializedPart implements VersionableObject
{
    public long controllerId;
    public long companionId;
    private final BinarSerialPart m_binarPart;
    public static final int SERIALIZED_SIZE = 16;
    
    public NPCCompanionInfo() {
        super();
        this.controllerId = 0L;
        this.companionId = 0L;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = NPCCompanionInfo.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de NPCCompanionInfo");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de NPCCompanionInfo", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = NPCCompanionInfo.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de NPCCompanionInfo");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de NPCCompanionInfo", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return NPCCompanionInfo.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putLong(this.controllerId);
        buffer.putLong(this.companionId);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.controllerId = buffer.getLong();
        this.companionId = buffer.getLong();
        return true;
    }
    
    @Override
    public void clear() {
        this.controllerId = 0L;
        this.companionId = 0L;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        return 16;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("controllerId=").append(this.controllerId).append('\n');
        repr.append(prefix).append("companionId=").append(this.companionId).append('\n');
    }
}
