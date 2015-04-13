package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.companion;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public final class AddItemToCompanionEquipmentRequestMessage extends OutputOnlyProxyMessage
{
    private final long m_companionId;
    private final byte m_equipmentPosition;
    private final long m_itemUid;
    
    public AddItemToCompanionEquipmentRequestMessage(final long companionId, final byte equipmentPosition, final long itemUid) {
        super();
        this.m_companionId = companionId;
        this.m_equipmentPosition = equipmentPosition;
        this.m_itemUid = itemUid;
    }
    
    @Override
    public byte[] encode() {
        final ByteArray ba = new ByteArray();
        ba.putLong(this.m_companionId);
        ba.put(this.m_equipmentPosition);
        ba.putLong(this.m_itemUid);
        return this.addClientHeader((byte)3, ba.toArray());
    }
    
    @Override
    public int getId() {
        return 5555;
    }
    
    @Override
    public String toString() {
        return "AddItemToCompanionEquipmentRequestMessage{m_companionId=" + this.m_companionId + ", m_equipmentPosition=" + this.m_equipmentPosition + ", m_itemUid=" + this.m_itemUid + '}';
    }
}
