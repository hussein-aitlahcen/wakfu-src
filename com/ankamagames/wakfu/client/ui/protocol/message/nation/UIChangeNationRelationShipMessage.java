package com.ankamagames.wakfu.client.ui.protocol.message.nation;

import com.ankamagames.wakfu.client.ui.protocol.message.*;

public class UIChangeNationRelationShipMessage extends UIMessage
{
    private byte m_alignmentId;
    private int m_nationId;
    
    public byte getAlignmentId() {
        return this.m_alignmentId;
    }
    
    public void setAlignmentId(final byte alignmentId) {
        this.m_alignmentId = alignmentId;
    }
    
    public int getNationId() {
        return this.m_nationId;
    }
    
    public void setNationId(final int nationId) {
        this.m_nationId = nationId;
    }
    
    @Override
    public int getId() {
        return 18413;
    }
}
