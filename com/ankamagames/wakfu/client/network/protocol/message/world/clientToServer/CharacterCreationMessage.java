package com.ankamagames.wakfu.client.network.protocol.message.world.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.framework.kernel.utils.*;

public class CharacterCreationMessage extends OutputOnlyProxyMessage
{
    private long m_characterId;
    private byte m_sex;
    private byte m_skinColorIndex;
    private byte m_hairColorIndex;
    private byte m_pupilColorIndex;
    private byte m_skinColorFactor;
    private byte m_hairColorFactor;
    private byte m_clothIndex;
    private byte m_faceIndex;
    private short m_breed;
    private String m_name;
    
    @Override
    public byte[] encode() {
        final ByteArray bb = new ByteArray();
        bb.putLong(this.m_characterId);
        bb.put(this.m_sex);
        bb.put(this.m_skinColorIndex);
        bb.put(this.m_hairColorIndex);
        bb.put(this.m_pupilColorIndex);
        bb.put(this.m_skinColorFactor);
        bb.put(this.m_hairColorFactor);
        bb.put(this.m_clothIndex);
        bb.put(this.m_faceIndex);
        bb.putShort(this.m_breed);
        final byte[] bytes = StringUtils.toUTF8(this.m_name);
        bb.put((byte)bytes.length);
        bb.put(bytes);
        return this.addClientHeader((byte)2, bb.toArray());
    }
    
    @Override
    public int getId() {
        return 2053;
    }
    
    public void setCharacterId(final long characterId) {
        this.m_characterId = characterId;
    }
    
    public void setSex(final byte sex) {
        this.m_sex = sex;
    }
    
    public void setSkinColorIndex(final byte skinColorIndex) {
        this.m_skinColorIndex = skinColorIndex;
    }
    
    public void setHairColorIndex(final byte hairColorIndex) {
        this.m_hairColorIndex = hairColorIndex;
    }
    
    public void setPupilColorIndex(final byte pupilColorIndex) {
        this.m_pupilColorIndex = pupilColorIndex;
    }
    
    public void setSkinColorFactor(final byte skinColorFactor) {
        this.m_skinColorFactor = skinColorFactor;
    }
    
    public void setHairColorFactor(final byte hairColorFactor) {
        this.m_hairColorFactor = hairColorFactor;
    }
    
    public void setClothIndex(final byte clothIndex) {
        this.m_clothIndex = clothIndex;
    }
    
    public void setFaceIndex(final byte faceIndex) {
        this.m_faceIndex = faceIndex;
    }
    
    public void setBreed(final short breed) {
        this.m_breed = breed;
    }
    
    public void setName(final String name) {
        this.m_name = name;
    }
}
