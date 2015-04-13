package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.util.*;
import java.nio.*;

public class RawAptitudeBonusInventory implements VersionableObject
{
    public final ArrayList<Content> contents;
    public final ArrayList<AvailablePoints> availablePoints;
    
    public RawAptitudeBonusInventory() {
        super();
        this.contents = new ArrayList<Content>(0);
        this.availablePoints = new ArrayList<AvailablePoints>(0);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.contents.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.contents.size());
        for (int i = 0; i < this.contents.size(); ++i) {
            final Content contents_element = this.contents.get(i);
            final boolean contents_element_ok = contents_element.serialize(buffer);
            if (!contents_element_ok) {
                return false;
            }
        }
        if (this.availablePoints.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.availablePoints.size());
        for (int i = 0; i < this.availablePoints.size(); ++i) {
            final AvailablePoints availablePoints_element = this.availablePoints.get(i);
            final boolean availablePoints_element_ok = availablePoints_element.serialize(buffer);
            if (!availablePoints_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int contents_size = buffer.getShort() & 0xFFFF;
        this.contents.clear();
        this.contents.ensureCapacity(contents_size);
        for (int i = 0; i < contents_size; ++i) {
            final Content contents_element = new Content();
            final boolean contents_element_ok = contents_element.unserialize(buffer);
            if (!contents_element_ok) {
                return false;
            }
            this.contents.add(contents_element);
        }
        final int availablePoints_size = buffer.getShort() & 0xFFFF;
        this.availablePoints.clear();
        this.availablePoints.ensureCapacity(availablePoints_size);
        for (int j = 0; j < availablePoints_size; ++j) {
            final AvailablePoints availablePoints_element = new AvailablePoints();
            final boolean availablePoints_element_ok = availablePoints_element.unserialize(buffer);
            if (!availablePoints_element_ok) {
                return false;
            }
            this.availablePoints.add(availablePoints_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.contents.clear();
        this.availablePoints.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 2;
        for (int i = 0; i < this.contents.size(); ++i) {
            final Content contents_element = this.contents.get(i);
            size += contents_element.serializedSize();
        }
        size += 2;
        for (int i = 0; i < this.availablePoints.size(); ++i) {
            final AvailablePoints availablePoints_element = this.availablePoints.get(i);
            size += availablePoints_element.serializedSize();
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
        repr.append(prefix).append("contents=");
        if (this.contents.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.contents.size()).append(" elements)...\n");
            for (int i = 0; i < this.contents.size(); ++i) {
                final Content contents_element = this.contents.get(i);
                contents_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("availablePoints=");
        if (this.availablePoints.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.availablePoints.size()).append(" elements)...\n");
            for (int i = 0; i < this.availablePoints.size(); ++i) {
                final AvailablePoints availablePoints_element = this.availablePoints.get(i);
                availablePoints_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class Content implements VersionableObject
    {
        public final RawBonus bonus;
        
        public Content() {
            super();
            this.bonus = new RawBonus();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean bonus_ok = this.bonus.serialize(buffer);
            return bonus_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean bonus_ok = this.bonus.unserialize(buffer);
            return bonus_ok;
        }
        
        @Override
        public void clear() {
            this.bonus.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            int size = 0;
            size += this.bonus.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("bonus=...\n");
            this.bonus.internalToString(repr, prefix + "  ");
        }
    }
    
    public static class AvailablePoints implements VersionableObject
    {
        public final RawCategoryAvailablePoints categoryAvailablePoints;
        
        public AvailablePoints() {
            super();
            this.categoryAvailablePoints = new RawCategoryAvailablePoints();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean categoryAvailablePoints_ok = this.categoryAvailablePoints.serialize(buffer);
            return categoryAvailablePoints_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean categoryAvailablePoints_ok = this.categoryAvailablePoints.unserialize(buffer);
            return categoryAvailablePoints_ok;
        }
        
        @Override
        public void clear() {
            this.categoryAvailablePoints.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            int size = 0;
            size += this.categoryAvailablePoints.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("categoryAvailablePoints=...\n");
            this.categoryAvailablePoints.internalToString(repr, prefix + "  ");
        }
    }
}
