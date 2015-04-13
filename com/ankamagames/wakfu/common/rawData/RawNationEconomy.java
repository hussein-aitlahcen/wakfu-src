package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.util.*;
import java.nio.*;

public class RawNationEconomy implements VersionableObject
{
    public final ArrayList<Spending> spentCash;
    public final ArrayList<Profit> accumulatedTaxes;
    public TotalCash totalCash;
    
    public RawNationEconomy() {
        super();
        this.spentCash = new ArrayList<Spending>(0);
        this.accumulatedTaxes = new ArrayList<Profit>(0);
        this.totalCash = null;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.spentCash.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.spentCash.size());
        for (int i = 0; i < this.spentCash.size(); ++i) {
            final Spending spentCash_element = this.spentCash.get(i);
            final boolean spentCash_element_ok = spentCash_element.serialize(buffer);
            if (!spentCash_element_ok) {
                return false;
            }
        }
        if (this.accumulatedTaxes.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.accumulatedTaxes.size());
        for (int i = 0; i < this.accumulatedTaxes.size(); ++i) {
            final Profit accumulatedTaxes_element = this.accumulatedTaxes.get(i);
            final boolean accumulatedTaxes_element_ok = accumulatedTaxes_element.serialize(buffer);
            if (!accumulatedTaxes_element_ok) {
                return false;
            }
        }
        if (this.totalCash != null) {
            buffer.put((byte)1);
            final boolean totalCash_ok = this.totalCash.serialize(buffer);
            if (!totalCash_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int spentCash_size = buffer.getShort() & 0xFFFF;
        this.spentCash.clear();
        this.spentCash.ensureCapacity(spentCash_size);
        for (int i = 0; i < spentCash_size; ++i) {
            final Spending spentCash_element = new Spending();
            final boolean spentCash_element_ok = spentCash_element.unserialize(buffer);
            if (!spentCash_element_ok) {
                return false;
            }
            this.spentCash.add(spentCash_element);
        }
        final int accumulatedTaxes_size = buffer.getShort() & 0xFFFF;
        this.accumulatedTaxes.clear();
        this.accumulatedTaxes.ensureCapacity(accumulatedTaxes_size);
        for (int j = 0; j < accumulatedTaxes_size; ++j) {
            final Profit accumulatedTaxes_element = new Profit();
            final boolean accumulatedTaxes_element_ok = accumulatedTaxes_element.unserialize(buffer);
            if (!accumulatedTaxes_element_ok) {
                return false;
            }
            this.accumulatedTaxes.add(accumulatedTaxes_element);
        }
        final boolean totalCash_present = buffer.get() == 1;
        if (totalCash_present) {
            this.totalCash = new TotalCash();
            final boolean totalCash_ok = this.totalCash.unserialize(buffer);
            if (!totalCash_ok) {
                return false;
            }
        }
        else {
            this.totalCash = null;
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.spentCash.clear();
        this.accumulatedTaxes.clear();
        this.totalCash = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 2;
        for (int i = 0; i < this.spentCash.size(); ++i) {
            final Spending spentCash_element = this.spentCash.get(i);
            size += spentCash_element.serializedSize();
        }
        size += 2;
        for (int i = 0; i < this.accumulatedTaxes.size(); ++i) {
            final Profit accumulatedTaxes_element = this.accumulatedTaxes.get(i);
            size += accumulatedTaxes_element.serializedSize();
        }
        ++size;
        if (this.totalCash != null) {
            size += this.totalCash.serializedSize();
        }
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("spentCash=");
        if (this.spentCash.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.spentCash.size()).append(" elements)...\n");
            for (int i = 0; i < this.spentCash.size(); ++i) {
                final Spending spentCash_element = this.spentCash.get(i);
                spentCash_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("accumulatedTaxes=");
        if (this.accumulatedTaxes.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.accumulatedTaxes.size()).append(" elements)...\n");
            for (int i = 0; i < this.accumulatedTaxes.size(); ++i) {
                final Profit accumulatedTaxes_element = this.accumulatedTaxes.get(i);
                accumulatedTaxes_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("totalCash=");
        if (this.totalCash == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.totalCash.internalToString(repr, prefix + "  ");
        }
    }
    
    public static class Spending implements VersionableObject
    {
        public byte itemTypeId;
        public int amount;
        public static final int SERIALIZED_SIZE = 5;
        
        public Spending() {
            super();
            this.itemTypeId = 0;
            this.amount = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.put(this.itemTypeId);
            buffer.putInt(this.amount);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.itemTypeId = buffer.get();
            this.amount = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.itemTypeId = 0;
            this.amount = 0;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 5;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("itemTypeId=").append(this.itemTypeId).append('\n');
            repr.append(prefix).append("amount=").append(this.amount).append('\n');
        }
    }
    
    public static class Profit implements VersionableObject
    {
        public byte taxId;
        public int amount;
        public static final int SERIALIZED_SIZE = 5;
        
        public Profit() {
            super();
            this.taxId = 0;
            this.amount = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.put(this.taxId);
            buffer.putInt(this.amount);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.taxId = buffer.get();
            this.amount = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.taxId = 0;
            this.amount = 0;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 5;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("taxId=").append(this.taxId).append('\n');
            repr.append(prefix).append("amount=").append(this.amount).append('\n');
        }
    }
    
    public static class TotalCash implements VersionableObject
    {
        public int cash;
        public static final int SERIALIZED_SIZE = 4;
        
        public TotalCash() {
            super();
            this.cash = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.cash);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.cash = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.cash = 0;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 4;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("cash=").append(this.cash).append('\n');
        }
    }
}
