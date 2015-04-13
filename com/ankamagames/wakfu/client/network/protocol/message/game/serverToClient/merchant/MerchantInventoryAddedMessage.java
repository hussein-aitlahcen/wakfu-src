package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.merchant;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.nio.*;

public class MerchantInventoryAddedMessage extends InputOnlyProxyMessage
{
    private final RawMerchantItemInventory m_serializedFlea;
    private byte m_fleaSize;
    private int m_refItem;
    
    public MerchantInventoryAddedMessage() {
        super();
        this.m_serializedFlea = new RawMerchantItemInventory();
    }
    
    public RawMerchantItemInventory getSerializedFlea() {
        return this.m_serializedFlea;
    }
    
    public byte getFleaSize() {
        return this.m_fleaSize;
    }
    
    public int getRefItem() {
        return this.m_refItem;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        if (this.m_serializedFlea.unserialize(buffer)) {
            this.m_fleaSize = buffer.get();
            this.m_refItem = buffer.getInt();
            return true;
        }
        MerchantInventoryAddedMessage.m_logger.error((Object)"Erreur durant la d\u00e9-serialisation d'un des inventaires marchands");
        return false;
    }
    
    @Override
    public int getId() {
        return 10118;
    }
}
