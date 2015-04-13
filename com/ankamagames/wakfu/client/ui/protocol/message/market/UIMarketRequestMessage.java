package com.ankamagames.wakfu.client.ui.protocol.message.market;

import com.ankamagames.wakfu.client.ui.protocol.message.*;

public class UIMarketRequestMessage extends UIMessage
{
    private short m_minLevel;
    private short m_maxLevel;
    private short m_minPrice;
    private short m_maxPrice;
    private String m_name;
    private boolean m_lowestPrices;
    private byte elementsMask;
    
    public UIMarketRequestMessage() {
        super();
        this.m_minLevel = -1;
        this.m_maxLevel = -1;
        this.m_minPrice = -1;
        this.m_maxPrice = -1;
        this.m_name = "";
        this.m_lowestPrices = true;
    }
    
    public void setMinLevel(final short minLevel) {
        this.m_minLevel = minLevel;
    }
    
    public void setMaxLevel(final short maxLevel) {
        this.m_maxLevel = maxLevel;
    }
    
    public void setMinPrice(final short minPrice) {
        this.m_minPrice = minPrice;
    }
    
    public void setMaxPrice(final short maxPrice) {
        this.m_maxPrice = maxPrice;
    }
    
    public void setName(final String name) {
        this.m_name = name;
    }
    
    public void setLowestPrices(final boolean lowestPrices) {
        this.m_lowestPrices = lowestPrices;
    }
    
    public short getMinLevel() {
        return this.m_minLevel;
    }
    
    public short getMaxLevel() {
        return this.m_maxLevel;
    }
    
    public short getMinPrice() {
        return this.m_minPrice;
    }
    
    public short getMaxPrice() {
        return this.m_maxPrice;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public boolean isLowestPrices() {
        return this.m_lowestPrices;
    }
    
    public byte getElementsMask() {
        return this.elementsMask;
    }
    
    public void setElementsMask(final byte elementsMask) {
        this.elementsMask = elementsMask;
    }
}
