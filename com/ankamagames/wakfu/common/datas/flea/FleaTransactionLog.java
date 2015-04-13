package com.ankamagames.wakfu.common.datas.flea;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.wakfu.common.rawData.*;
import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public final class FleaTransactionLog implements RawConvertible<RawTransactionLog>
{
    protected static final Logger m_logger;
    private static final Comparator<FleaTransaction> m_antichronologicalTransactionSort;
    private final int m_paramLogMaxSize;
    private final int m_paramLogMaxDateDays;
    private final int m_paramLogFusionTimeMinutes;
    private final int m_paramLogFusionMaxItems;
    private final List<FleaTransaction> m_transactions;
    private short m_newTransactionsCount;
    private long m_newTransactionsKamas;
    private long m_lastReadTransactionDate;
    
    public FleaTransactionLog(final int maxSize, final int maxDate, final int fusionTime, final int fusionMaxItems) {
        super();
        this.m_transactions = new ArrayList<FleaTransaction>();
        this.m_paramLogMaxSize = maxSize;
        this.m_paramLogMaxDateDays = maxDate;
        this.m_paramLogFusionTimeMinutes = fusionTime;
        this.m_paramLogFusionMaxItems = fusionMaxItems;
    }
    
    public void addTransaction(final FleaTransaction transaction) {
        boolean fusionned = false;
        for (final FleaTransaction oldTransaction : this.m_transactions) {
            final long transactionAge = Math.abs(transaction.getDate() - oldTransaction.getDate());
            if (transactionAge > this.m_paramLogFusionTimeMinutes * 1000 * 60) {
                break;
            }
            if (oldTransaction.getItemsCount() + transaction.getItemsCount() > this.m_paramLogFusionMaxItems) {
                continue;
            }
            fusionned = oldTransaction.fusionAdd(transaction);
            if (fusionned) {
                break;
            }
        }
        if (!fusionned) {
            this.m_transactions.add(transaction);
        }
        Collections.sort(this.m_transactions, FleaTransactionLog.m_antichronologicalTransactionSort);
        this.trimExcess();
        ++this.m_newTransactionsCount;
        this.m_newTransactionsKamas += transaction.getTotalPrice();
    }
    
    private void trimExcess() {
        while (!this.m_transactions.isEmpty()) {
            if (this.m_transactions.size() <= this.m_paramLogMaxSize && System.currentTimeMillis() - this.m_transactions.get(this.m_transactions.size() - 1).getDate() <= this.m_paramLogMaxDateDays * 1000L * 60L * 60L * 24L) {
                return;
            }
            this.m_transactions.remove(this.m_transactions.size() - 1);
        }
    }
    
    public short getNewTransactionsCount() {
        return this.m_newTransactionsCount;
    }
    
    public long getNewTransactionsKamas() {
        return this.m_newTransactionsKamas;
    }
    
    public long getLastReadTransactionDate() {
        return this.m_lastReadTransactionDate;
    }
    
    public void setLastReadTransactionDate(final long lastReadTransactionDate) {
        this.m_lastReadTransactionDate = lastReadTransactionDate;
    }
    
    public void resetNewTransactions() {
        this.m_newTransactionsCount = 0;
        this.m_newTransactionsKamas = 0L;
    }
    
    public List<FleaTransaction> getTransactions() {
        return this.m_transactions;
    }
    
    public int size() {
        return this.m_transactions.size();
    }
    
    public void clear() {
        this.m_transactions.clear();
    }
    
    @Override
    public boolean toRaw(final RawTransactionLog raw) {
        raw.transactions.clear();
        for (final FleaTransaction transaction : this.m_transactions) {
            final RawTransactionLog.Transaction rawTransaction = new RawTransactionLog.Transaction();
            rawTransaction.transactionDate = transaction.getDate();
            rawTransaction.buyerId = transaction.getBuyerId();
            rawTransaction.buyerName = transaction.getBuyerName();
            final Iterator<ObjectPair<Integer, Short>> it = transaction.itemsIterator();
            while (it.hasNext()) {
                final ObjectPair<Integer, Short> item = it.next();
                final RawTransactionLog.Transaction.SoldItem rawItem = new RawTransactionLog.Transaction.SoldItem();
                rawItem.refId = item.getFirst();
                rawItem.quantity = item.getSecond();
                rawTransaction.soldItems.add(rawItem);
            }
            rawTransaction.totalPrice = transaction.getTotalPrice();
            raw.transactions.add(rawTransaction);
        }
        raw.newTransactionsCount = this.m_newTransactionsCount;
        raw.newTransactionsKamas = this.m_newTransactionsKamas;
        raw.lastReadTransactionDate = this.m_lastReadTransactionDate;
        return true;
    }
    
    @Override
    public boolean fromRaw(final RawTransactionLog raw) {
        this.m_transactions.clear();
        for (final RawTransactionLog.Transaction rawTransaction : raw.transactions) {
            try {
                final FleaTransaction transaction = new FleaTransaction();
                transaction.setBuyerId(rawTransaction.buyerId);
                transaction.setBuyerName(rawTransaction.buyerName);
                transaction.setDate(rawTransaction.transactionDate);
                for (final RawTransactionLog.Transaction.SoldItem soldItem : rawTransaction.soldItems) {
                    transaction.addItem(soldItem.refId, soldItem.quantity);
                }
                transaction.setTotalPrice(rawTransaction.totalPrice);
                this.m_transactions.add(transaction);
            }
            catch (Exception e) {
                FleaTransactionLog.m_logger.error((Object)("Exception lors de la d\u00e9-serialisation du log de transaction " + rawTransaction), (Throwable)e);
            }
        }
        this.m_newTransactionsCount = raw.newTransactionsCount;
        this.m_newTransactionsKamas = raw.newTransactionsKamas;
        this.m_lastReadTransactionDate = raw.lastReadTransactionDate;
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)FleaTransactionLog.class);
        m_antichronologicalTransactionSort = new Comparator<FleaTransaction>() {
            @Override
            public int compare(final FleaTransaction t1, final FleaTransaction t2) {
                final long diffTime = t2.getDate() - t1.getDate();
                if (diffTime > 0L) {
                    return 1;
                }
                if (diffTime < 0L) {
                    return -1;
                }
                return 0;
            }
        };
    }
}
