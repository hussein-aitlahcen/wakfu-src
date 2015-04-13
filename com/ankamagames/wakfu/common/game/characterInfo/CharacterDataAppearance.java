package com.ankamagames.wakfu.common.game.characterInfo;

import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;

public class CharacterDataAppearance
{
    private final byte m_skinColorIndex;
    private final byte m_skinColorFactor;
    private final byte m_hairColorIndex;
    private final byte m_hairColorFactor;
    private final byte m_pupilColorIndex;
    private final byte m_clothIndex;
    private final byte m_faceIndex;
    private final int m_headRefId;
    private final int m_shouldersRefId;
    private final int m_chestRefId;
    
    public CharacterDataAppearance(final byte skinColorIndex, final byte skinColorFactor, final byte hairColorIndex, final byte hairColorFactor, final byte pupilColorIndex, final byte clothIndex, final byte faceIndex, final int headRefId, final int shouldersRefId, final int chestRefId) {
        super();
        this.m_skinColorIndex = skinColorIndex;
        this.m_skinColorFactor = skinColorFactor;
        this.m_hairColorIndex = hairColorIndex;
        this.m_hairColorFactor = hairColorFactor;
        this.m_pupilColorIndex = pupilColorIndex;
        this.m_clothIndex = clothIndex;
        this.m_faceIndex = faceIndex;
        this.m_headRefId = headRefId;
        this.m_shouldersRefId = shouldersRefId;
        this.m_chestRefId = chestRefId;
    }
    
    public void serialize(final ByteArray buffer) {
        buffer.put(this.m_skinColorIndex);
        buffer.put(this.m_skinColorFactor);
        buffer.put(this.m_hairColorIndex);
        buffer.put(this.m_hairColorFactor);
        buffer.put(this.m_pupilColorIndex);
        buffer.put(this.m_clothIndex);
        buffer.put(this.m_faceIndex);
        buffer.putInt(this.m_headRefId);
        buffer.putInt(this.m_shouldersRefId);
        buffer.putInt(this.m_chestRefId);
    }
    
    public void serialize(final ByteBuffer buffer) {
        buffer.put(this.m_skinColorIndex);
        buffer.put(this.m_skinColorFactor);
        buffer.put(this.m_hairColorIndex);
        buffer.put(this.m_hairColorFactor);
        buffer.put(this.m_pupilColorIndex);
        buffer.put(this.m_clothIndex);
        buffer.put(this.m_faceIndex);
        buffer.putInt(this.m_headRefId);
        buffer.putInt(this.m_shouldersRefId);
        buffer.putInt(this.m_chestRefId);
    }
    
    public static CharacterDataAppearance fromBuild(final ByteBuffer buffer) {
        return new CharacterDataAppearance(buffer.get(), buffer.get(), buffer.get(), buffer.get(), buffer.get(), buffer.get(), buffer.get(), buffer.getInt(), buffer.getInt(), buffer.getInt());
    }
    
    public int serializedSize() {
        return 19;
    }
    
    public byte getSkinColorIndex() {
        return this.m_skinColorIndex;
    }
    
    public byte getSkinColorFactor() {
        return this.m_skinColorFactor;
    }
    
    public byte getHairColorIndex() {
        return this.m_hairColorIndex;
    }
    
    public byte getHairColorFactor() {
        return this.m_hairColorFactor;
    }
    
    public byte getPupilColorIndex() {
        return this.m_pupilColorIndex;
    }
    
    public int getHeadRefId() {
        return this.m_headRefId;
    }
    
    public int getShouldersRefId() {
        return this.m_shouldersRefId;
    }
    
    public int getChestRefId() {
        return this.m_chestRefId;
    }
    
    public byte getClothIndex() {
        return this.m_clothIndex;
    }
    
    public byte getFaceIndex() {
        return this.m_faceIndex;
    }
}
