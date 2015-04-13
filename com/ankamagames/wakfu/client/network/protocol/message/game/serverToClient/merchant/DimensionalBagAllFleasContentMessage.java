package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.merchant;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.rawData.*;
import gnu.trove.*;
import java.util.*;
import java.nio.*;

public class DimensionalBagAllFleasContentMessage extends InputOnlyProxyMessage
{
    private final ArrayList<RawMerchantItemInventory> m_serializedFleas;
    private final TByteArrayList m_fleaSize;
    private final TIntArrayList m_refItems;
    
    public DimensionalBagAllFleasContentMessage() {
        super();
        this.m_serializedFleas = new ArrayList<RawMerchantItemInventory>();
        this.m_fleaSize = new TByteArrayList();
        this.m_refItems = new TIntArrayList();
    }
    
    public List<RawMerchantItemInventory> getSerializedFleas() {
        return this.m_serializedFleas;
    }
    
    public TByteArrayList getFleaSize() {
        return this.m_fleaSize;
    }
    
    public TIntArrayList getRefItems() {
        return this.m_refItems;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        for (int count = buffer.get() & 0xFF, i = 0; i < count; ++i) {
            final RawMerchantItemInventory rawMerchantItemInventory = new RawMerchantItemInventory();
            if (!rawMerchantItemInventory.unserialize(buffer)) {
                DimensionalBagAllFleasContentMessage.m_logger.error((Object)"Erreur durant la d\u00e9-serialisation d'un des inventaires marchands");
                return false;
            }
            this.m_serializedFleas.add(rawMerchantItemInventory);
            this.m_fleaSize.add(buffer.get());
            this.m_refItems.add(buffer.getInt());
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 10114;
    }
}
