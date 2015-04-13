package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.util.*;
import java.nio.*;

public class RawNationDiplomacy implements VersionableObject
{
    public final ArrayList<Alignment> alignments;
    public final ArrayList<Request> alignmentRequests;
    
    public RawNationDiplomacy() {
        super();
        this.alignments = new ArrayList<Alignment>(0);
        this.alignmentRequests = new ArrayList<Request>(0);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.alignments.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.alignments.size());
        for (int i = 0; i < this.alignments.size(); ++i) {
            final Alignment alignments_element = this.alignments.get(i);
            final boolean alignments_element_ok = alignments_element.serialize(buffer);
            if (!alignments_element_ok) {
                return false;
            }
        }
        if (this.alignmentRequests.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.alignmentRequests.size());
        for (int i = 0; i < this.alignmentRequests.size(); ++i) {
            final Request alignmentRequests_element = this.alignmentRequests.get(i);
            final boolean alignmentRequests_element_ok = alignmentRequests_element.serialize(buffer);
            if (!alignmentRequests_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int alignments_size = buffer.getShort() & 0xFFFF;
        this.alignments.clear();
        this.alignments.ensureCapacity(alignments_size);
        for (int i = 0; i < alignments_size; ++i) {
            final Alignment alignments_element = new Alignment();
            final boolean alignments_element_ok = alignments_element.unserialize(buffer);
            if (!alignments_element_ok) {
                return false;
            }
            this.alignments.add(alignments_element);
        }
        final int alignmentRequests_size = buffer.getShort() & 0xFFFF;
        this.alignmentRequests.clear();
        this.alignmentRequests.ensureCapacity(alignmentRequests_size);
        for (int j = 0; j < alignmentRequests_size; ++j) {
            final Request alignmentRequests_element = new Request();
            final boolean alignmentRequests_element_ok = alignmentRequests_element.unserialize(buffer);
            if (!alignmentRequests_element_ok) {
                return false;
            }
            this.alignmentRequests.add(alignmentRequests_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.alignments.clear();
        this.alignmentRequests.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 2;
        for (int i = 0; i < this.alignments.size(); ++i) {
            final Alignment alignments_element = this.alignments.get(i);
            size += alignments_element.serializedSize();
        }
        size += 2;
        for (int i = 0; i < this.alignmentRequests.size(); ++i) {
            final Request alignmentRequests_element = this.alignmentRequests.get(i);
            size += alignmentRequests_element.serializedSize();
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
        repr.append(prefix).append("alignments=");
        if (this.alignments.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.alignments.size()).append(" elements)...\n");
            for (int i = 0; i < this.alignments.size(); ++i) {
                final Alignment alignments_element = this.alignments.get(i);
                alignments_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("alignmentRequests=");
        if (this.alignmentRequests.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.alignmentRequests.size()).append(" elements)...\n");
            for (int i = 0; i < this.alignmentRequests.size(); ++i) {
                final Request alignmentRequests_element = this.alignmentRequests.get(i);
                alignmentRequests_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class Alignment implements VersionableObject
    {
        public int nationId;
        public byte alignment;
        public static final int SERIALIZED_SIZE = 5;
        
        public Alignment() {
            super();
            this.nationId = 0;
            this.alignment = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.nationId);
            buffer.put(this.alignment);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.nationId = buffer.getInt();
            this.alignment = buffer.get();
            return true;
        }
        
        @Override
        public void clear() {
            this.nationId = 0;
            this.alignment = 0;
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
            repr.append(prefix).append("nationId=").append(this.nationId).append('\n');
            repr.append(prefix).append("alignment=").append(this.alignment).append('\n');
        }
    }
    
    public static class Request implements VersionableObject
    {
        public int nationId;
        public byte alignment;
        public static final int SERIALIZED_SIZE = 5;
        
        public Request() {
            super();
            this.nationId = 0;
            this.alignment = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.nationId);
            buffer.put(this.alignment);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.nationId = buffer.getInt();
            this.alignment = buffer.get();
            return true;
        }
        
        @Override
        public void clear() {
            this.nationId = 0;
            this.alignment = 0;
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
            repr.append(prefix).append("nationId=").append(this.nationId).append('\n');
            repr.append(prefix).append("alignment=").append(this.alignment).append('\n');
        }
    }
}
