package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public class CharacterSerializedRemoteAccountInformation extends CharacterSerializedPart implements VersionableObject
{
    public int subscriptionLevel;
    public int[] additionalRights;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedRemoteAccountInformation() {
        super();
        this.subscriptionLevel = 0;
        this.additionalRights = null;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedRemoteAccountInformation.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedRemoteAccountInformation");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedRemoteAccountInformation", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedRemoteAccountInformation.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedRemoteAccountInformation");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedRemoteAccountInformation", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedRemoteAccountInformation.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putInt(this.subscriptionLevel);
        if (this.additionalRights != null) {
            if (this.additionalRights.length > 65535) {
                return false;
            }
            buffer.putShort((short)this.additionalRights.length);
            for (int i = 0; i < this.additionalRights.length; ++i) {
                buffer.putInt(this.additionalRights[i]);
            }
        }
        else {
            buffer.putShort((short)0);
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.subscriptionLevel = buffer.getInt();
        final int additionalRights_size = buffer.getShort() & 0xFFFF;
        if (additionalRights_size > 0) {
            this.additionalRights = new int[additionalRights_size];
            for (int i = 0; i < additionalRights_size; ++i) {
                this.additionalRights[i] = buffer.getInt();
            }
        }
        else {
            this.additionalRights = null;
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.subscriptionLevel = 0;
        this.additionalRights = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 4;
        size += 2;
        size += ((this.additionalRights != null) ? (this.additionalRights.length * 4) : 0);
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("subscriptionLevel=").append(this.subscriptionLevel).append('\n');
        repr.append(prefix).append("additionalRights=(").append(this.additionalRights.length).append(" bytes)\n");
    }
}
