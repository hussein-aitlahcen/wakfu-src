package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class ExchangeInteractiveElementParamBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_apsId;
    protected int m_visualMruId;
    protected byte m_sortTypeId;
    protected Exchange[] m_exchanges;
    protected ChaosParamBinaryData m_chaosParams;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getApsId() {
        return this.m_apsId;
    }
    
    public int getVisualMruId() {
        return this.m_visualMruId;
    }
    
    public byte getSortTypeId() {
        return this.m_sortTypeId;
    }
    
    public Exchange[] getExchanges() {
        return this.m_exchanges;
    }
    
    public ChaosParamBinaryData getChaosParams() {
        return this.m_chaosParams;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_apsId = 0;
        this.m_visualMruId = 0;
        this.m_sortTypeId = 0;
        this.m_exchanges = null;
        this.m_chaosParams = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_apsId = buffer.getInt();
        this.m_visualMruId = buffer.getInt();
        this.m_sortTypeId = buffer.get();
        final int exchangeCount = buffer.getInt();
        this.m_exchanges = new Exchange[exchangeCount];
        for (int iExchange = 0; iExchange < exchangeCount; ++iExchange) {
            (this.m_exchanges[iExchange] = new Exchange()).read(buffer);
        }
        if (buffer.get() != 0) {
            (this.m_chaosParams = new ChaosParamBinaryData()).read(buffer);
        }
        else {
            this.m_chaosParams = null;
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.EXCHANGE_IE_PARAM.getId();
    }
    
    public static class Consumable
    {
        protected int m_itemId;
        protected short m_quantity;
        
        public int getItemId() {
            return this.m_itemId;
        }
        
        public short getQuantity() {
            return this.m_quantity;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_itemId = buffer.getInt();
            this.m_quantity = buffer.getShort();
        }
    }
    
    public static class Resulting
    {
        protected int m_itemId;
        protected short m_quantity;
        protected byte m_forcedBindType;
        
        public int getItemId() {
            return this.m_itemId;
        }
        
        public short getQuantity() {
            return this.m_quantity;
        }
        
        public byte getForcedBindType() {
            return this.m_forcedBindType;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_itemId = buffer.getInt();
            this.m_quantity = buffer.getShort();
            this.m_forcedBindType = buffer.get();
        }
    }
    
    public static class Exchange
    {
        protected int m_exchangeId;
        protected String m_criteria;
        protected Consumable[] m_consumables;
        protected int m_consumableKama;
        protected int m_consumablePvpMoney;
        protected Resulting[] m_resultings;
        protected int m_resultingKama;
        
        public int getExchangeId() {
            return this.m_exchangeId;
        }
        
        public String getCriteria() {
            return this.m_criteria;
        }
        
        public Consumable[] getConsumables() {
            return this.m_consumables;
        }
        
        public int getConsumableKama() {
            return this.m_consumableKama;
        }
        
        public int getConsumablePvpMoney() {
            return this.m_consumablePvpMoney;
        }
        
        public Resulting[] getResultings() {
            return this.m_resultings;
        }
        
        public int getResultingKama() {
            return this.m_resultingKama;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_exchangeId = buffer.getInt();
            this.m_criteria = buffer.readUTF8().intern();
            final int consumableCount = buffer.getInt();
            this.m_consumables = new Consumable[consumableCount];
            for (int iConsumable = 0; iConsumable < consumableCount; ++iConsumable) {
                (this.m_consumables[iConsumable] = new Consumable()).read(buffer);
            }
            this.m_consumableKama = buffer.getInt();
            this.m_consumablePvpMoney = buffer.getInt();
            final int resultingCount = buffer.getInt();
            this.m_resultings = new Resulting[resultingCount];
            for (int iResulting = 0; iResulting < resultingCount; ++iResulting) {
                (this.m_resultings[iResulting] = new Resulting()).read(buffer);
            }
            this.m_resultingKama = buffer.getInt();
        }
    }
}
