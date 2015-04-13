package com.ankamagames.wakfu.common.game.group.member.serialization;

import com.ankamagames.wakfu.common.game.group.member.*;
import java.nio.*;
import com.ankamagames.framework.kernel.core.maths.*;

final class PositionPart extends PartyMemberSerializationPart
{
    PositionPart(final PartyMemberInterface member) {
        super(member);
    }
    
    @Override
    public void serialize(final ByteBuffer buffer) {
        buffer.putShort(this.m_member.getInstanceId());
        final Point3 position = this.m_member.getPosition();
        buffer.putInt(position.getX());
        buffer.putInt(position.getY());
        buffer.putShort(position.getZ());
    }
    
    @Override
    public void unserialize(final ByteBuffer buffer, final int version) {
        this.m_member.setInstanceId(buffer.getShort());
        final Point3 newPos = new Point3(buffer.getInt(), buffer.getInt(), buffer.getShort());
        this.m_member.setPosition(newPos);
    }
    
    @Override
    public int expectedSize() {
        return 12;
    }
}
