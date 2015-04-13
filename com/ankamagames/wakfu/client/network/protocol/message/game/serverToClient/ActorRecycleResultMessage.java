package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.wakfu.*;
import java.nio.*;

public class ActorRecycleResultMessage extends InputOnlyProxyMessage
{
    private float m_depollutionFactor;
    private TLongShortHashMap m_recycledItems;
    private TLongObjectHashMap<CrystalItem> m_crystalItems;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_depollutionFactor = buffer.getFloat();
        final int recycledItemsCount = buffer.getInt();
        this.m_recycledItems = new TLongShortHashMap(recycledItemsCount);
        for (int i = 0; i < recycledItemsCount; ++i) {
            this.m_recycledItems.put(buffer.getLong(), buffer.getShort());
        }
        final int crystalItemsCount = buffer.getInt();
        this.m_crystalItems = new TLongObjectHashMap<CrystalItem>(crystalItemsCount);
        for (int j = 0; j < crystalItemsCount; ++j) {
            this.m_crystalItems.put(buffer.getLong(), new CrystalItem(buffer.getShort(), buffer.get(), buffer.getShort()));
        }
        return true;
    }
    
    public float getDepollutionFactor() {
        return this.m_depollutionFactor;
    }
    
    public TLongShortHashMap getRecycledItems() {
        return this.m_recycledItems;
    }
    
    @Override
    public int getId() {
        return 4184;
    }
    
    public TLongObjectHashMap<CrystalItem> getCrystalItems() {
        return this.m_crystalItems;
    }
}
