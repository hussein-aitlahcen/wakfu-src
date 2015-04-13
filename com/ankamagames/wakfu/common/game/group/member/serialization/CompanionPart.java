package com.ankamagames.wakfu.common.game.group.member.serialization;

import java.nio.*;
import com.ankamagames.wakfu.common.game.group.member.*;
import com.ankamagames.wakfu.common.game.companion.*;

final class CompanionPart extends PartyMemberSerializationPart
{
    CompanionPart(final PartyMemberInterface member) {
        super(member);
    }
    
    @Override
    public void serialize(final ByteBuffer buffer) {
        if (!(this.m_member instanceof CompanionPartyMemberModel)) {
            return;
        }
        final CompanionModel companionModel = ((CompanionPartyMemberModel)this.m_member).getCompanionModel();
        buffer.put(CompanionModelSerializer.serialize(companionModel));
    }
    
    @Override
    public void unserialize(final ByteBuffer buffer, final int version) {
        if (!(this.m_member instanceof CompanionPartyMemberModel)) {
            return;
        }
        final CompanionModel companionModel = CompanionModelSerializer.unserialize(buffer);
        ((CompanionPartyMemberModel)this.m_member).setCompanionModel(companionModel);
    }
    
    @Override
    public int expectedSize() {
        return CompanionModelSerializer.size(((CompanionPartyMemberModel)this.m_member).getCompanionModel());
    }
}
