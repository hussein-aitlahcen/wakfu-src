package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;
import java.util.*;

public class RawProtectorReferenceInventory implements VersionableObject
{
    public final ArrayList<Content> contents;
    public ContentsSelection contentsSelection;
    public final ArrayList<BuyableContent> buyableContents;
    
    public RawProtectorReferenceInventory() {
        super();
        this.contents = new ArrayList<Content>(0);
        this.contentsSelection = null;
        this.buyableContents = new ArrayList<BuyableContent>(0);
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
        if (this.contentsSelection != null) {
            buffer.put((byte)1);
            final boolean contentsSelection_ok = this.contentsSelection.serialize(buffer);
            if (!contentsSelection_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        if (this.buyableContents.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.buyableContents.size());
        for (int i = 0; i < this.buyableContents.size(); ++i) {
            final BuyableContent buyableContents_element = this.buyableContents.get(i);
            final boolean buyableContents_element_ok = buyableContents_element.serialize(buffer);
            if (!buyableContents_element_ok) {
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
        final boolean contentsSelection_present = buffer.get() == 1;
        if (contentsSelection_present) {
            this.contentsSelection = new ContentsSelection();
            final boolean contentsSelection_ok = this.contentsSelection.unserialize(buffer);
            if (!contentsSelection_ok) {
                return false;
            }
        }
        else {
            this.contentsSelection = null;
        }
        final int buyableContents_size = buffer.getShort() & 0xFFFF;
        this.buyableContents.clear();
        this.buyableContents.ensureCapacity(buyableContents_size);
        for (int j = 0; j < buyableContents_size; ++j) {
            final BuyableContent buyableContents_element = new BuyableContent();
            final boolean buyableContents_element_ok = buyableContents_element.unserialize(buffer);
            if (!buyableContents_element_ok) {
                return false;
            }
            this.buyableContents.add(buyableContents_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.contents.clear();
        this.contentsSelection = null;
        this.buyableContents.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 1) {
            return this.unserialize(buffer);
        }
        final RawProtectorReferenceInventoryConverter converter = new RawProtectorReferenceInventoryConverter();
        final boolean ok = converter.unserializeVersion(buffer, version);
        if (ok) {
            converter.pushResult();
            return true;
        }
        return false;
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 2;
        for (int i = 0; i < this.contents.size(); ++i) {
            final Content contents_element = this.contents.get(i);
            size += contents_element.serializedSize();
        }
        ++size;
        if (this.contentsSelection != null) {
            size += this.contentsSelection.serializedSize();
        }
        size += 2;
        for (int i = 0; i < this.buyableContents.size(); ++i) {
            final BuyableContent buyableContents_element = this.buyableContents.get(i);
            size += buyableContents_element.serializedSize();
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
        repr.append(prefix).append("contentsSelection=");
        if (this.contentsSelection == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.contentsSelection.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("buyableContents=");
        if (this.buyableContents.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.buyableContents.size()).append(" elements)...\n");
            for (int i = 0; i < this.buyableContents.size(); ++i) {
                final BuyableContent buyableContents_element = this.buyableContents.get(i);
                buyableContents_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class Content implements VersionableObject
    {
        public int referenceId;
        public int remainingDuration;
        public static final int SERIALIZED_SIZE = 8;
        
        public Content() {
            super();
            this.referenceId = 0;
            this.remainingDuration = -1;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.referenceId);
            buffer.putInt(this.remainingDuration);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.referenceId = buffer.getInt();
            this.remainingDuration = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.referenceId = 0;
            this.remainingDuration = -1;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 8;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("referenceId=").append(this.referenceId).append('\n');
            repr.append(prefix).append("remainingDuration=").append(this.remainingDuration).append('\n');
        }
    }
    
    public static class ContentsSelection implements VersionableObject
    {
        public final ArrayList<ContentSelection> contentsSelection;
        
        public ContentsSelection() {
            super();
            this.contentsSelection = new ArrayList<ContentSelection>(0);
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            if (this.contentsSelection.size() > 65535) {
                return false;
            }
            buffer.putShort((short)this.contentsSelection.size());
            for (int i = 0; i < this.contentsSelection.size(); ++i) {
                final ContentSelection contentsSelection_element = this.contentsSelection.get(i);
                final boolean contentsSelection_element_ok = contentsSelection_element.serialize(buffer);
                if (!contentsSelection_element_ok) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final int contentsSelection_size = buffer.getShort() & 0xFFFF;
            this.contentsSelection.clear();
            this.contentsSelection.ensureCapacity(contentsSelection_size);
            for (int i = 0; i < contentsSelection_size; ++i) {
                final ContentSelection contentsSelection_element = new ContentSelection();
                final boolean contentsSelection_element_ok = contentsSelection_element.unserialize(buffer);
                if (!contentsSelection_element_ok) {
                    return false;
                }
                this.contentsSelection.add(contentsSelection_element);
            }
            return true;
        }
        
        @Override
        public void clear() {
            this.contentsSelection.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 1) {
                return this.unserialize(buffer);
            }
            final ContentsSelectionConverter converter = new ContentsSelectionConverter();
            final boolean ok = converter.unserializeVersion(buffer, version);
            if (ok) {
                converter.pushResult();
                return true;
            }
            return false;
        }
        
        @Override
        public int serializedSize() {
            int size = 0;
            size += 2;
            for (int i = 0; i < this.contentsSelection.size(); ++i) {
                final ContentSelection contentsSelection_element = this.contentsSelection.get(i);
                size += contentsSelection_element.serializedSize();
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
            repr.append(prefix).append("contentsSelection=");
            if (this.contentsSelection.isEmpty()) {
                repr.append("{}").append('\n');
            }
            else {
                repr.append("(").append(this.contentsSelection.size()).append(" elements)...\n");
                for (int i = 0; i < this.contentsSelection.size(); ++i) {
                    final ContentSelection contentsSelection_element = this.contentsSelection.get(i);
                    contentsSelection_element.internalToString(repr, prefix + i + "/ ");
                }
            }
        }
        
        public static class ContentSelection implements VersionableObject
        {
            public int referenceId;
            public static final int SERIALIZED_SIZE = 4;
            
            public ContentSelection() {
                super();
                this.referenceId = 0;
            }
            
            @Override
            public boolean serialize(final ByteBuffer buffer) {
                buffer.putInt(this.referenceId);
                return true;
            }
            
            @Override
            public boolean unserialize(final ByteBuffer buffer) {
                this.referenceId = buffer.getInt();
                return true;
            }
            
            @Override
            public void clear() {
                this.referenceId = 0;
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
                repr.append(prefix).append("referenceId=").append(this.referenceId).append('\n');
            }
        }
        
        private final class ContentsSelectionConverter
        {
            private final ArrayList<ContentSelection> contentsSelection;
            
            private ContentsSelectionConverter() {
                super();
                this.contentsSelection = new ArrayList<ContentSelection>(0);
            }
            
            public void pushResult() {
                ContentsSelection.this.contentsSelection.clear();
                ContentsSelection.this.contentsSelection.ensureCapacity(this.contentsSelection.size());
                ContentsSelection.this.contentsSelection.addAll(this.contentsSelection);
            }
            
            private boolean unserialize_v0(final ByteBuffer buffer) {
                final int contentsSelection_size = buffer.getShort() & 0xFFFF;
                this.contentsSelection.clear();
                this.contentsSelection.ensureCapacity(contentsSelection_size);
                for (int i = 0; i < contentsSelection_size; ++i) {
                    final ContentSelection contentsSelection_element = new ContentSelection();
                    final boolean contentsSelection_element_ok = contentsSelection_element.unserializeVersion(buffer, 0);
                    if (!contentsSelection_element_ok) {
                        return false;
                    }
                    this.contentsSelection.add(contentsSelection_element);
                }
                return true;
            }
            
            public void convert_v0_to_v1() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 0) {
                    return false;
                }
                if (version >= 1) {
                    return false;
                }
                final boolean ok = this.unserialize_v0(buffer);
                if (ok) {
                    this.convert_v0_to_v1();
                    return true;
                }
                return false;
            }
        }
    }
    
    public static class BuyableContent implements VersionableObject
    {
        public int referenceId;
        public int price;
        public static final int SERIALIZED_SIZE = 8;
        
        public BuyableContent() {
            super();
            this.referenceId = 0;
            this.price = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.referenceId);
            buffer.putInt(this.price);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.referenceId = buffer.getInt();
            this.price = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.referenceId = 0;
            this.price = 0;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 8;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("referenceId=").append(this.referenceId).append('\n');
            repr.append(prefix).append("price=").append(this.price).append('\n');
        }
    }
    
    private final class RawProtectorReferenceInventoryConverter
    {
        private final ArrayList<Content> contents;
        private ContentsSelection contentsSelection;
        private final ArrayList<BuyableContent> buyableContents;
        
        private RawProtectorReferenceInventoryConverter() {
            super();
            this.contents = new ArrayList<Content>(0);
            this.contentsSelection = null;
            this.buyableContents = new ArrayList<BuyableContent>(0);
        }
        
        public void pushResult() {
            RawProtectorReferenceInventory.this.contents.clear();
            RawProtectorReferenceInventory.this.contents.ensureCapacity(this.contents.size());
            RawProtectorReferenceInventory.this.contents.addAll(this.contents);
            RawProtectorReferenceInventory.this.contentsSelection = this.contentsSelection;
            RawProtectorReferenceInventory.this.buyableContents.clear();
            RawProtectorReferenceInventory.this.buyableContents.ensureCapacity(this.buyableContents.size());
            RawProtectorReferenceInventory.this.buyableContents.addAll(this.buyableContents);
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        public void convert_v0_to_v1() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 0) {
                return false;
            }
            if (version >= 1) {
                return false;
            }
            final boolean ok = this.unserialize_v0(buffer);
            if (ok) {
                this.convert_v0_to_v1();
                return true;
            }
            return false;
        }
    }
}
