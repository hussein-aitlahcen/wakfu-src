package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public class NPCSerializedAppearance extends CharacterSerializedPart implements VersionableObject
{
    public boolean show;
    private final BinarSerialPart m_binarPart;
    public static final int SERIALIZED_SIZE = 1;
    
    public NPCSerializedAppearance() {
        super();
        this.show = false;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = NPCSerializedAppearance.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de NPCSerializedAppearance");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de NPCSerializedAppearance", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = NPCSerializedAppearance.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de NPCSerializedAppearance");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de NPCSerializedAppearance", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return NPCSerializedAppearance.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.put((byte)(this.show ? 1 : 0));
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.show = (buffer.get() == 1);
        return true;
    }
    
    @Override
    public void clear() {
        this.show = false;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        return 1;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("show=").append(this.show).append('\n');
    }
}
