package com.ankamagames.wakfu.common.datas.flea;

import org.apache.log4j.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public final class FleaTransaction
{
    protected static final Logger m_logger;
    private long m_date;
    private long m_buyerId;
    private String m_buyerName;
    private final TIntArrayList m_itemsRefId;
    private final TShortArrayList m_itemsQuantity;
    private long m_totalPrice;
    
    public FleaTransaction() {
        super();
        this.m_itemsRefId = new TIntArrayList(1);
        this.m_itemsQuantity = new TShortArrayList(1);
    }
    
    public long getDate() {
        return this.m_date;
    }
    
    public void setDate(final long date) {
        this.m_date = date;
    }
    
    public long getBuyerId() {
        return this.m_buyerId;
    }
    
    public void setBuyerId(final long buyerId) {
        this.m_buyerId = buyerId;
    }
    
    public String getBuyerName() {
        return this.m_buyerName;
    }
    
    public void setBuyerName(final String buyerName) {
        this.m_buyerName = buyerName;
    }
    
    public void addItem(final int refId, final short quantity) {
        for (int i = this.m_itemsRefId.size() - 1; i >= 0; --i) {
            if (this.m_itemsRefId.getQuick(i) == refId) {
                this.m_itemsQuantity.setQuick(i, (short)(this.m_itemsQuantity.getQuick(i) + quantity));
                return;
            }
        }
        this.m_itemsRefId.add(refId);
        this.m_itemsQuantity.add(quantity);
    }
    
    public Iterator<ObjectPair<Integer, Short>> itemsIterator() {
        return new Iterator<ObjectPair<Integer, Short>>() {
            private int m_i = -1;
            
            @Override
            public boolean hasNext() {
                return this.m_i < FleaTransaction.this.m_itemsRefId.size() - 1;
            }
            
            @Override
            public ObjectPair<Integer, Short> next() {
                ++this.m_i;
                return new ObjectPair<Integer, Short>(FleaTransaction.this.m_itemsRefId.getQuick(this.m_i), FleaTransaction.this.m_itemsQuantity.getQuick(this.m_i));
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
    
    public int getItemsCount() {
        return this.m_itemsRefId.size();
    }
    
    public long getTotalPrice() {
        return this.m_totalPrice;
    }
    
    public void setTotalPrice(final long totalPrice) {
        this.m_totalPrice = totalPrice;
    }
    
    public boolean fusionAdd(final FleaTransaction transaction) {
        if (transaction.getBuyerId() != this.m_buyerId || !transaction.getBuyerName().equals(this.m_buyerName)) {
            return false;
        }
        final Iterator<ObjectPair<Integer, Short>> it = transaction.itemsIterator();
        while (it.hasNext()) {
            final ObjectPair<Integer, Short> item = it.next();
            this.addItem(item.getFirst(), item.getSecond());
        }
        this.m_totalPrice += transaction.getTotalPrice();
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)FleaTransaction.class);
    }
}
