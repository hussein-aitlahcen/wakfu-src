package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public class CharacterSerializedAchievements extends CharacterSerializedPart implements VersionableObject
{
    public byte[] serializedAchievementsContext;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedAchievements() {
        super();
        this.serializedAchievementsContext = null;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedAchievements.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedAchievements");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedAchievements", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedAchievements.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedAchievements");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedAchievements", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedAchievements.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.serializedAchievementsContext != null) {
            if (this.serializedAchievementsContext.length > 65535) {
                return false;
            }
            buffer.putShort((short)this.serializedAchievementsContext.length);
            buffer.put(this.serializedAchievementsContext);
        }
        else {
            buffer.putShort((short)0);
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int serializedAchievementsContext_size = buffer.getShort() & 0xFFFF;
        if (serializedAchievementsContext_size > 0) {
            buffer.get(this.serializedAchievementsContext = new byte[serializedAchievementsContext_size]);
        }
        else {
            this.serializedAchievementsContext = null;
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.serializedAchievementsContext = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 2;
        size += ((this.serializedAchievementsContext != null) ? this.serializedAchievementsContext.length : 0);
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("serializedAchievementsContext=(").append((this.serializedAchievementsContext != null) ? this.serializedAchievementsContext.length : 0).append(" bytes)\n");
    }
}
