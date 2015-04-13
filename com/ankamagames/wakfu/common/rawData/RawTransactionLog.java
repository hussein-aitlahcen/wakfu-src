package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.util.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class RawTransactionLog implements VersionableObject
{
    public final ArrayList<Transaction> transactions;
    public short newTransactionsCount;
    public long newTransactionsKamas;
    public long lastReadTransactionDate;
    
    public RawTransactionLog() {
        super();
        this.transactions = new ArrayList<Transaction>(0);
        this.newTransactionsCount = 0;
        this.newTransactionsKamas = 0L;
        this.lastReadTransactionDate = 0L;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.transactions.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.transactions.size());
        for (int i = 0; i < this.transactions.size(); ++i) {
            final Transaction transactions_element = this.transactions.get(i);
            final boolean transactions_element_ok = transactions_element.serialize(buffer);
            if (!transactions_element_ok) {
                return false;
            }
        }
        buffer.putShort(this.newTransactionsCount);
        buffer.putLong(this.newTransactionsKamas);
        buffer.putLong(this.lastReadTransactionDate);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int transactions_size = buffer.getShort() & 0xFFFF;
        this.transactions.clear();
        this.transactions.ensureCapacity(transactions_size);
        for (int i = 0; i < transactions_size; ++i) {
            final Transaction transactions_element = new Transaction();
            final boolean transactions_element_ok = transactions_element.unserialize(buffer);
            if (!transactions_element_ok) {
                return false;
            }
            this.transactions.add(transactions_element);
        }
        this.newTransactionsCount = buffer.getShort();
        this.newTransactionsKamas = buffer.getLong();
        this.lastReadTransactionDate = buffer.getLong();
        return true;
    }
    
    @Override
    public void clear() {
        this.transactions.clear();
        this.newTransactionsCount = 0;
        this.newTransactionsKamas = 0L;
        this.lastReadTransactionDate = 0L;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 2;
        for (int i = 0; i < this.transactions.size(); ++i) {
            final Transaction transactions_element = this.transactions.get(i);
            size += transactions_element.serializedSize();
        }
        size += 2;
        size += 8;
        size += 8;
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("transactions=");
        if (this.transactions.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.transactions.size()).append(" elements)...\n");
            for (int i = 0; i < this.transactions.size(); ++i) {
                final Transaction transactions_element = this.transactions.get(i);
                transactions_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("newTransactionsCount=").append(this.newTransactionsCount).append('\n');
        repr.append(prefix).append("newTransactionsKamas=").append(this.newTransactionsKamas).append('\n');
        repr.append(prefix).append("lastReadTransactionDate=").append(this.lastReadTransactionDate).append('\n');
    }
    
    public static class Transaction implements VersionableObject
    {
        public long transactionDate;
        public long buyerId;
        public String buyerName;
        public final ArrayList<SoldItem> soldItems;
        public long totalPrice;
        
        public Transaction() {
            super();
            this.transactionDate = 0L;
            this.buyerId = 0L;
            this.buyerName = null;
            this.soldItems = new ArrayList<SoldItem>(0);
            this.totalPrice = 0L;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putLong(this.transactionDate);
            buffer.putLong(this.buyerId);
            if (this.buyerName != null) {
                final byte[] serialized_buyerName = StringUtils.toUTF8(this.buyerName);
                if (serialized_buyerName.length > 255) {
                    return false;
                }
                buffer.put((byte)serialized_buyerName.length);
                buffer.put(serialized_buyerName);
            }
            else {
                buffer.put((byte)0);
            }
            if (this.soldItems.size() > 255) {
                return false;
            }
            buffer.put((byte)this.soldItems.size());
            for (int i = 0; i < this.soldItems.size(); ++i) {
                final SoldItem soldItems_element = this.soldItems.get(i);
                final boolean soldItems_element_ok = soldItems_element.serialize(buffer);
                if (!soldItems_element_ok) {
                    return false;
                }
            }
            buffer.putLong(this.totalPrice);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.transactionDate = buffer.getLong();
            this.buyerId = buffer.getLong();
            final int buyerName_size = buffer.get() & 0xFF;
            final byte[] serialized_buyerName = new byte[buyerName_size];
            buffer.get(serialized_buyerName);
            this.buyerName = StringUtils.fromUTF8(serialized_buyerName);
            final int soldItems_size = buffer.get() & 0xFF;
            this.soldItems.clear();
            this.soldItems.ensureCapacity(soldItems_size);
            for (int i = 0; i < soldItems_size; ++i) {
                final SoldItem soldItems_element = new SoldItem();
                final boolean soldItems_element_ok = soldItems_element.unserialize(buffer);
                if (!soldItems_element_ok) {
                    return false;
                }
                this.soldItems.add(soldItems_element);
            }
            this.totalPrice = buffer.getLong();
            return true;
        }
        
        @Override
        public void clear() {
            this.transactionDate = 0L;
            this.buyerId = 0L;
            this.buyerName = null;
            this.soldItems.clear();
            this.totalPrice = 0L;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            int size = 0;
            size += 8;
            size += 8;
            size = ++size + ((this.buyerName != null) ? StringUtils.toUTF8(this.buyerName).length : 0);
            ++size;
            for (int i = 0; i < this.soldItems.size(); ++i) {
                final SoldItem soldItems_element = this.soldItems.get(i);
                size += soldItems_element.serializedSize();
            }
            size += 8;
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("transactionDate=").append(this.transactionDate).append('\n');
            repr.append(prefix).append("buyerId=").append(this.buyerId).append('\n');
            repr.append(prefix).append("buyerName=").append(this.buyerName).append('\n');
            repr.append(prefix).append("soldItems=");
            if (this.soldItems.isEmpty()) {
                repr.append("{}").append('\n');
            }
            else {
                repr.append("(").append(this.soldItems.size()).append(" elements)...\n");
                for (int i = 0; i < this.soldItems.size(); ++i) {
                    final SoldItem soldItems_element = this.soldItems.get(i);
                    soldItems_element.internalToString(repr, prefix + i + "/ ");
                }
            }
            repr.append(prefix).append("totalPrice=").append(this.totalPrice).append('\n');
        }
        
        public static class SoldItem implements VersionableObject
        {
            public int refId;
            public short quantity;
            public static final int SERIALIZED_SIZE = 6;
            
            public SoldItem() {
                super();
                this.refId = 0;
                this.quantity = 0;
            }
            
            @Override
            public boolean serialize(final ByteBuffer buffer) {
                buffer.putInt(this.refId);
                buffer.putShort(this.quantity);
                return true;
            }
            
            @Override
            public boolean unserialize(final ByteBuffer buffer) {
                this.refId = buffer.getInt();
                this.quantity = buffer.getShort();
                return true;
            }
            
            @Override
            public void clear() {
                this.refId = 0;
                this.quantity = 0;
            }
            
            @Override
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                return this.unserialize(buffer);
            }
            
            @Override
            public int serializedSize() {
                return 6;
            }
            
            @Override
            public final String toString() {
                final StringBuilder repr = new StringBuilder();
                this.internalToString(repr, "");
                return repr.toString();
            }
            
            public final void internalToString(final StringBuilder repr, final String prefix) {
                repr.append(prefix).append("refId=").append(this.refId).append('\n');
                repr.append(prefix).append("quantity=").append(this.quantity).append('\n');
            }
        }
    }
}
