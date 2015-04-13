package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.companion;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public final class RemoveItemFromCompanionEquipmentRequestMessage extends OutputOnlyProxyMessage
{
    private final long m_companionId;
    private final long m_itemUid;
    private final long m_destBagId;
    private final short m_destPosition;
    
    public RemoveItemFromCompanionEquipmentRequestMessage(final long companionId, final long itemUid, final long destBagId, final short destPosition) {
        super();
        this.m_companionId = companionId;
        this.m_itemUid = itemUid;
        this.m_destBagId = destBagId;
        this.m_destPosition = destPosition;
    }
    
    @Override
    public byte[] encode() {
        final ByteArray ba = new ByteArray();
        ba.putLong(this.m_companionId);
        ba.putLong(this.m_itemUid);
        ba.putLong(this.m_destBagId);
        ba.putShort(this.m_destPosition);
        return this.addClientHeader((byte)3, ba.toArray());
    }
    
    @Override
    public int getId() {
        return 5557;
    }
    
    @Override
    public String toString() {
        return "RemoveItemFromCompanionEquipmentRequestMessage{m_companionId=" + this.m_companionId + ", m_itemUid=" + this.m_itemUid + ", m_destBagId=" + this.m_destBagId + ", m_destPosition=" + this.m_destPosition + '}';
    }
}
