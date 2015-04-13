package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class CharacterSerializedGameServer extends CharacterSerializedPart implements VersionableObject
{
    public String gameServer;
    public long lastLog;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedGameServer() {
        super();
        this.gameServer = null;
        this.lastLog = 0L;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedGameServer.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedGameServer");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedGameServer", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedGameServer.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedGameServer");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedGameServer", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedGameServer.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.gameServer != null) {
            final byte[] serialized_gameServer = StringUtils.toUTF8(this.gameServer);
            if (serialized_gameServer.length > 255) {
                return false;
            }
            buffer.put((byte)serialized_gameServer.length);
            buffer.put(serialized_gameServer);
        }
        else {
            buffer.put((byte)0);
        }
        buffer.putLong(this.lastLog);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int gameServer_size = buffer.get() & 0xFF;
        final byte[] serialized_gameServer = new byte[gameServer_size];
        buffer.get(serialized_gameServer);
        this.gameServer = StringUtils.fromUTF8(serialized_gameServer);
        this.lastLog = buffer.getLong();
        return true;
    }
    
    @Override
    public void clear() {
        this.gameServer = null;
        this.lastLog = 0L;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size = ++size + ((this.gameServer != null) ? StringUtils.toUTF8(this.gameServer).length : 0);
        size += 8;
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("gameServer=").append(this.gameServer).append('\n');
        repr.append(prefix).append("lastLog=").append(this.lastLog).append('\n');
    }
}
