package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.market;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.util.*;
import java.nio.*;

public final class MarketConsultRequestMessage extends OutputOnlyProxyMessage
{
    private final ArrayList<Integer> m_itemRefIds;
    private short m_itemType;
    private int m_minPrice;
    private int m_maxPrice;
    private byte m_sortingType;
    private short m_firstIndex;
    private boolean m_lowestMode;
    
    public MarketConsultRequestMessage() {
        super();
        this.m_itemRefIds = new ArrayList<Integer>();
        this.m_itemType = -1;
        this.m_minPrice = -1;
        this.m_maxPrice = -1;
        this.m_sortingType = -1;
        this.m_firstIndex = 0;
        this.m_lowestMode = true;
    }
    
    public void setItemRefIds(final ArrayList<Integer> itemRefIds) {
        this.m_itemRefIds.clear();
        this.m_itemRefIds.addAll(itemRefIds);
    }
    
    public void setItemType(final short itemType) {
        this.m_itemType = itemType;
    }
    
    public void setMinPrice(final int minPrice) {
        this.m_minPrice = minPrice;
    }
    
    public void setMaxPrice(final int maxPrice) {
        this.m_maxPrice = maxPrice;
    }
    
    public void setSortingType(final byte sortingType) {
        this.m_sortingType = sortingType;
    }
    
    public int getRefIdListSize() {
        return this.m_itemRefIds.size();
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(14 + this.m_itemRefIds.size() * 4);
        bb.putShort(this.m_itemType);
        bb.putInt(this.m_minPrice);
        bb.putInt(this.m_maxPrice);
        bb.put(this.m_sortingType);
        bb.putShort(this.m_firstIndex);
        bb.put((byte)(this.m_lowestMode ? 1 : 0));
        for (int i = 0; i < this.m_itemRefIds.size(); ++i) {
            bb.putInt(this.m_itemRefIds.get(i));
        }
        return this.addClientHeader((byte)3, bb.array());
    }
    
    @Override
    public int getId() {
        return 15263;
    }
    
    public void setFirstIndex(final short firstIndex) {
        this.m_firstIndex = firstIndex;
    }
    
    public void setLowestMode(final boolean lowestMode) {
        this.m_lowestMode = lowestMode;
    }
    
    public boolean isLowestMode() {
        return this.m_lowestMode;
    }
    
    public short getItemType() {
        return this.m_itemType;
    }
    
    public int getMinPrice() {
        return this.m_minPrice;
    }
    
    public int getMaxPrice() {
        return this.m_maxPrice;
    }
}
