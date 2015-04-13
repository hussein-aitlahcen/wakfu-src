package com.ankamagames.wakfu.common.game.group.member.serialization;

import com.ankamagames.wakfu.common.game.group.member.*;
import java.nio.*;

final class DeathStatusPart extends PartyMemberSerializationPart
{
    private boolean m_dataChanged;
    
    DeathStatusPart(final PartyMemberInterface member) {
        super(member);
        this.m_dataChanged = false;
    }
    
    @Override
    public void serialize(final ByteBuffer buffer) {
        buffer.put((byte)(this.m_member.isDead() ? 1 : 0));
    }
    
    @Override
    public void unserialize(final ByteBuffer buffer, final int version) {
        final boolean dead = buffer.get() == 1;
        this.m_dataChanged = (this.m_member.isDead() != dead);
        this.m_member.setDead(dead);
    }
    
    public void updateToSerializedPart() {
    }
    
    @Override
    public void onDataChanged() {
        if (this.m_dataChanged) {
            super.onDataChanged();
        }
        this.m_dataChanged = false;
    }
}
