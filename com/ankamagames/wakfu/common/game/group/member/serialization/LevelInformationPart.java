package com.ankamagames.wakfu.common.game.group.member.serialization;

import com.ankamagames.wakfu.common.game.group.member.*;
import java.nio.*;

final class LevelInformationPart extends PartyMemberSerializationPart
{
    private boolean m_dataChanged;
    
    LevelInformationPart(final PartyMemberInterface member) {
        super(member);
        this.m_dataChanged = false;
    }
    
    @Override
    public void serialize(final ByteBuffer buffer) {
        buffer.putShort(this.m_member.getLevel());
    }
    
    @Override
    public void unserialize(final ByteBuffer buffer, final int version) {
        final short newlevel = buffer.getShort();
        this.m_dataChanged = (this.m_member.getLevel() != 0 && this.m_member.getLevel() != newlevel);
        this.m_member.setLevel(newlevel);
    }
    
    @Override
    public void onDataChanged() {
        if (this.m_dataChanged) {
            super.onDataChanged();
        }
        this.m_dataChanged = false;
    }
}
