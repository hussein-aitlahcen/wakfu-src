package com.ankamagames.wakfu.common.game.group.party;

import java.nio.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public final class PartySerializer
{
    private final PartyModelInterface m_partyModel;
    
    public PartySerializer(final PartyModelInterface partyModel) {
        super();
        this.m_partyModel = partyModel;
    }
    
    public void unserializeGroupData(final byte[] groupData) {
        final ByteBuffer bb = ByteBuffer.wrap(groupData);
        this.m_partyModel.setLeaderId(bb.getLong());
    }
    
    public byte[] serialize() {
        final ByteArray ba = new ByteArray();
        ba.putLong(this.m_partyModel.getLeaderId());
        return ba.toArray();
    }
}
