package com.ankamagames.wakfu.client.core.game.market;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.market.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;

public final class MarketRequestView extends ImmutableFieldProvider
{
    public static final String NAME_FIELD = "name";
    public static final String LEVEL_MIN_FIELD = "levelMin";
    public static final String LEVEL_MAX_FIELD = "levelMax";
    public static final String PRICE_MIN_FIELD = "priceMin";
    public static final String PRICE_MAX_FIELD = "priceMax";
    public static final String IS_LOWEST_PRICE_FIELD = "isLowestPrice";
    private short m_minLevel;
    private short m_maxLevel;
    private String m_name;
    private MarketConsultRequestMessage m_lastRequestMessage;
    public String[] FIELDS;
    
    public MarketRequestView() {
        super();
        this.m_minLevel = -1;
        this.m_maxLevel = -1;
        this.m_lastRequestMessage = new MarketConsultRequestMessage();
        this.FIELDS = new String[] { "name", "levelMin", "levelMax", "priceMin", "priceMax", "isLowestPrice" };
    }
    
    @Override
    public String[] getFields() {
        return this.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            if (this.m_name == null || this.m_name.length() == 0) {
                final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("marketDialog");
                if (map != null) {
                    final TextEditor te = (TextEditor)map.getElement("textEditor");
                    if (te != null) {
                        return te.getGhostText();
                    }
                }
            }
            return this.m_name;
        }
        if (fieldName.equals("levelMin")) {
            return (this.m_minLevel == -1) ? "" : this.m_minLevel;
        }
        if (fieldName.equals("levelMax")) {
            return (this.m_maxLevel == -1) ? "" : this.m_maxLevel;
        }
        if (fieldName.equals("priceMin")) {
            final int minPrice = this.m_lastRequestMessage.getMinPrice();
            return (minPrice == -1) ? "" : minPrice;
        }
        if (fieldName.equals("priceMax")) {
            final int maxPrice = this.m_lastRequestMessage.getMaxPrice();
            return (maxPrice == -1) ? "" : maxPrice;
        }
        if (fieldName.equals("isLowestPrice")) {
            return this.m_lastRequestMessage.isLowestMode();
        }
        return null;
    }
    
    public short getMinLevel() {
        return this.m_minLevel;
    }
    
    public void setMinLevel(final short minLevel) {
        this.m_minLevel = minLevel;
    }
    
    public short getMaxLevel() {
        return this.m_maxLevel;
    }
    
    public void setMaxLevel(final short maxLevel) {
        this.m_maxLevel = maxLevel;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public void setName(final String name) {
        this.m_name = name;
    }
    
    public MarketConsultRequestMessage getLastRequestMessage() {
        return this.m_lastRequestMessage;
    }
    
    public void setLastRequestMessage(final MarketConsultRequestMessage lastRequestMessage) {
        this.m_lastRequestMessage = lastRequestMessage;
    }
}
