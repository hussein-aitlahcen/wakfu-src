package com.ankamagames.wakfu.common.game.group.member.serialization;

import com.ankamagames.wakfu.common.game.group.member.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

final class IdentificationPart extends PartyMemberSerializationPart
{
    private byte[] m_serializedCharacterName;
    
    public IdentificationPart(final PartyMemberInterface member) {
        super(member);
    }
    
    @Override
    public void serialize(final ByteBuffer buffer) {
        buffer.putLong(this.m_member.getClientId());
        buffer.putLong(this.m_member.getCharacterId());
        buffer.put((byte)this.m_serializedCharacterName.length);
        buffer.put(this.m_serializedCharacterName);
        buffer.putShort(this.m_member.getBreedId());
    }
    
    @Override
    public void unserialize(final ByteBuffer buffer, final int version) {
        this.m_member.setClientId(buffer.getLong());
        this.m_member.setCharacterId(buffer.getLong());
        buffer.get(this.m_serializedCharacterName = new byte[buffer.get() & 0xFF]);
        this.m_member.setName(StringUtils.fromUTF8(this.m_serializedCharacterName));
        this.m_member.setBreedId(buffer.getShort());
    }
    
    @Override
    public int expectedSize() {
        this.m_serializedCharacterName = StringUtils.toUTF8(this.m_member.getName());
        return 21 + this.m_serializedCharacterName.length + 2;
    }
}
