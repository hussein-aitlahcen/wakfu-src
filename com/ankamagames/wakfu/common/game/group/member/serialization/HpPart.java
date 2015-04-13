package com.ankamagames.wakfu.common.game.group.member.serialization;

import com.ankamagames.wakfu.common.game.group.member.*;
import java.nio.*;

final class HpPart extends PartyMemberSerializationPart
{
    HpPart(final PartyMemberInterface member) {
        super(member);
    }
    
    @Override
    public void serialize(final ByteBuffer buffer) {
        buffer.putInt(this.m_member.getCurrentHp());
        buffer.putInt(this.m_member.getMaxHp());
        buffer.putInt(this.m_member.getRegen());
    }
    
    @Override
    public void unserialize(final ByteBuffer buffer, final int version) {
        this.m_member.setCurrentHp(buffer.getInt());
        this.m_member.setMaxHp(buffer.getInt());
        this.m_member.setRegen(buffer.getInt());
        this.onDataChanged();
    }
    
    @Override
    public int expectedSize() {
        return 12;
    }
}
