package com.ankamagames.wakfu.common.game.nation.protector;

import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.wakfu.common.game.tax.*;
import com.ankamagames.wakfu.common.rawData.*;
import gnu.trove.*;

public class NationEconomyHandler
{
    private final Nation m_nation;
    private NationEconomyListener m_listener;
    private final TByteIntHashMap m_spentCash;
    private final TByteIntHashMap m_accumulatedTaxes;
    private int m_totalCash;
    
    NationEconomyHandler(final Nation nation) {
        super();
        this.m_spentCash = new TByteIntHashMap();
        this.m_accumulatedTaxes = new TByteIntHashMap();
        this.m_totalCash = 0;
        this.m_nation = nation;
    }
    
    public void setListener(final NationEconomyListener listener) {
        this.m_listener = listener;
    }
    
    void adjustTotalCash(final int delta) {
        this.m_totalCash += delta;
    }
    
    void onMerchantItemSold(final ProtectorMerchantItemType itemType, final int itemPrice) {
        this.m_spentCash.adjustOrPutValue(itemType.getTypeId(), itemPrice, itemPrice);
        if (this.m_listener != null) {
            this.m_listener.onSpentCashAdjusted(this.m_nation, itemType.getTypeId(), itemPrice);
        }
    }
    
    void onCheckInTax(final TaxContext taxContext, final int taxAmount) {
        this.m_accumulatedTaxes.adjustOrPutValue(taxContext.id, taxAmount, taxAmount);
        if (this.m_listener != null) {
            this.m_listener.onTaxCashAdjusted(this.m_nation, taxContext, taxAmount);
        }
    }
    
    public void clear() {
        this.m_spentCash.clear();
        this.m_accumulatedTaxes.clear();
        this.m_totalCash = 0;
    }
    
    public void toRaw(final RawNationEconomy raw, final Boolean serializeTotalCash) {
        this.m_spentCash.forEachEntry(new SetSpending(raw));
        this.m_accumulatedTaxes.forEachEntry(new SetProfit(raw));
        if (serializeTotalCash) {
            raw.totalCash = new RawNationEconomy.TotalCash();
            raw.totalCash.cash = this.m_totalCash;
        }
    }
    
    public void fromRaw(final RawNationEconomy raw) {
        for (int i = 0, size = raw.spentCash.size(); i < size; ++i) {
            final RawNationEconomy.Spending rawSpending = raw.spentCash.get(i);
            this.m_spentCash.put(rawSpending.itemTypeId, rawSpending.amount);
        }
        for (int i = 0, size = raw.accumulatedTaxes.size(); i < size; ++i) {
            final RawNationEconomy.Profit rawProfit = raw.accumulatedTaxes.get(i);
            this.m_accumulatedTaxes.put(rawProfit.taxId, rawProfit.amount);
        }
        if (raw.totalCash != null) {
            this.m_totalCash = raw.totalCash.cash;
        }
    }
    
    public TByteIntHashMap getSpentCash() {
        return this.m_spentCash;
    }
    
    public TByteIntHashMap getAccumulatedTaxes() {
        return this.m_accumulatedTaxes;
    }
    
    public int getTotalCash() {
        return this.m_totalCash;
    }
    
    private static class SetProfit implements TByteIntProcedure
    {
        private final RawNationEconomy m_raw;
        
        SetProfit(final RawNationEconomy raw) {
            super();
            this.m_raw = raw;
        }
        
        @Override
        public boolean execute(final byte a, final int b) {
            final RawNationEconomy.Profit raw = new RawNationEconomy.Profit();
            raw.taxId = a;
            raw.amount = b;
            this.m_raw.accumulatedTaxes.add(raw);
            return true;
        }
        
        @Override
        public String toString() {
            return "SetProfit{m_raw=" + this.m_raw + '}';
        }
    }
    
    private static class SetSpending implements TByteIntProcedure
    {
        private final RawNationEconomy m_raw;
        
        SetSpending(final RawNationEconomy raw) {
            super();
            this.m_raw = raw;
        }
        
        @Override
        public boolean execute(final byte a, final int b) {
            final RawNationEconomy.Spending raw = new RawNationEconomy.Spending();
            raw.itemTypeId = a;
            raw.amount = b;
            this.m_raw.spentCash.add(raw);
            return true;
        }
        
        @Override
        public String toString() {
            return "SetSpending{m_raw=" + this.m_raw + '}';
        }
    }
}
