package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public class CharacterSerializedAppearance extends CharacterSerializedPart implements VersionableObject
{
    public byte sex;
    public byte skinColorIndex;
    public byte hairColorIndex;
    public byte pupilColorIndex;
    public byte skinColorFactor;
    public byte hairColorFactor;
    public byte clothIndex;
    public byte faceIndex;
    public short currentTitle;
    private final BinarSerialPart m_binarPart;
    public static final int SERIALIZED_SIZE = 10;
    
    public CharacterSerializedAppearance() {
        super();
        this.sex = 0;
        this.skinColorIndex = 0;
        this.hairColorIndex = 0;
        this.pupilColorIndex = 0;
        this.skinColorFactor = 0;
        this.hairColorFactor = 0;
        this.clothIndex = 0;
        this.faceIndex = 0;
        this.currentTitle = -1;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedAppearance.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedAppearance");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedAppearance", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedAppearance.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedAppearance");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedAppearance", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedAppearance.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.put(this.sex);
        buffer.put(this.skinColorIndex);
        buffer.put(this.hairColorIndex);
        buffer.put(this.pupilColorIndex);
        buffer.put(this.skinColorFactor);
        buffer.put(this.hairColorFactor);
        buffer.put(this.clothIndex);
        buffer.put(this.faceIndex);
        buffer.putShort(this.currentTitle);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.sex = buffer.get();
        this.skinColorIndex = buffer.get();
        this.hairColorIndex = buffer.get();
        this.pupilColorIndex = buffer.get();
        this.skinColorFactor = buffer.get();
        this.hairColorFactor = buffer.get();
        this.clothIndex = buffer.get();
        this.faceIndex = buffer.get();
        this.currentTitle = buffer.getShort();
        return true;
    }
    
    @Override
    public void clear() {
        this.sex = 0;
        this.skinColorIndex = 0;
        this.hairColorIndex = 0;
        this.pupilColorIndex = 0;
        this.skinColorFactor = 0;
        this.hairColorFactor = 0;
        this.clothIndex = 0;
        this.faceIndex = 0;
        this.currentTitle = -1;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        return 10;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("sex=").append(this.sex).append('\n');
        repr.append(prefix).append("skinColorIndex=").append(this.skinColorIndex).append('\n');
        repr.append(prefix).append("hairColorIndex=").append(this.hairColorIndex).append('\n');
        repr.append(prefix).append("pupilColorIndex=").append(this.pupilColorIndex).append('\n');
        repr.append(prefix).append("skinColorFactor=").append(this.skinColorFactor).append('\n');
        repr.append(prefix).append("hairColorFactor=").append(this.hairColorFactor).append('\n');
        repr.append(prefix).append("clothIndex=").append(this.clothIndex).append('\n');
        repr.append(prefix).append("faceIndex=").append(this.faceIndex).append('\n');
        repr.append(prefix).append("currentTitle=").append(this.currentTitle).append('\n');
    }
}
