package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;
import java.util.*;

public class RawAptitudeInventory implements VersionableObject
{
    public final ArrayList<Content> contents;
    public final ArrayList<AvailablePoint> availablePointsArray;
    
    public RawAptitudeInventory() {
        super();
        this.contents = new ArrayList<Content>(0);
        this.availablePointsArray = new ArrayList<AvailablePoint>(0);
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
        if (this.availablePointsArray.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.availablePointsArray.size());
        for (int i = 0; i < this.availablePointsArray.size(); ++i) {
            final AvailablePoint availablePointsArray_element = this.availablePointsArray.get(i);
            final boolean availablePointsArray_element_ok = availablePointsArray_element.serialize(buffer);
            if (!availablePointsArray_element_ok) {
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
        final int availablePointsArray_size = buffer.getShort() & 0xFFFF;
        this.availablePointsArray.clear();
        this.availablePointsArray.ensureCapacity(availablePointsArray_size);
        for (int j = 0; j < availablePointsArray_size; ++j) {
            final AvailablePoint availablePointsArray_element = new AvailablePoint();
            final boolean availablePointsArray_element_ok = availablePointsArray_element.unserialize(buffer);
            if (!availablePointsArray_element_ok) {
                return false;
            }
            this.availablePointsArray.add(availablePointsArray_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.contents.clear();
        this.availablePointsArray.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10004) {
            return this.unserialize(buffer);
        }
        final RawAptitudeInventoryConverter converter = new RawAptitudeInventoryConverter();
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
        size += 2;
        for (int i = 0; i < this.availablePointsArray.size(); ++i) {
            final AvailablePoint availablePointsArray_element = this.availablePointsArray.get(i);
            size += availablePointsArray_element.serializedSize();
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
        repr.append(prefix).append("availablePointsArray=");
        if (this.availablePointsArray.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.availablePointsArray.size()).append(" elements)...\n");
            for (int i = 0; i < this.availablePointsArray.size(); ++i) {
                final AvailablePoint availablePointsArray_element = this.availablePointsArray.get(i);
                availablePointsArray_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class Content implements VersionableObject
    {
        public final RawAptitude aptitude;
        
        public Content() {
            super();
            this.aptitude = new RawAptitude();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean aptitude_ok = this.aptitude.serialize(buffer);
            return aptitude_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean aptitude_ok = this.aptitude.unserialize(buffer);
            return aptitude_ok;
        }
        
        @Override
        public void clear() {
            this.aptitude.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10004) {
                return this.unserialize(buffer);
            }
            final ContentConverter converter = new ContentConverter();
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
            size += this.aptitude.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("aptitude=...\n");
            this.aptitude.internalToString(repr, prefix + "  ");
        }
        
        private final class ContentConverter
        {
            private final RawAptitude aptitude;
            
            private ContentConverter() {
                super();
                this.aptitude = new RawAptitude();
            }
            
            public void pushResult() {
                Content.this.aptitude.referenceId = this.aptitude.referenceId;
                Content.this.aptitude.level = this.aptitude.level;
                Content.this.aptitude.wonLevel = this.aptitude.wonLevel;
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                final boolean aptitude_ok = this.aptitude.unserializeVersion(buffer, 1);
                return aptitude_ok;
            }
            
            public void convert_v1_to_v10004() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 1) {
                    return false;
                }
                if (version >= 10004) {
                    return false;
                }
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v10004();
                    return true;
                }
                return false;
            }
        }
    }
    
    public static class AvailablePoint implements VersionableObject
    {
        public byte aptitudeType;
        public int availablePoints;
        public int wonPoints;
        public static final int SERIALIZED_SIZE = 9;
        
        public AvailablePoint() {
            super();
            this.aptitudeType = 0;
            this.availablePoints = 0;
            this.wonPoints = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.put(this.aptitudeType);
            buffer.putInt(this.availablePoints);
            buffer.putInt(this.wonPoints);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.aptitudeType = buffer.get();
            this.availablePoints = buffer.getInt();
            this.wonPoints = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.aptitudeType = 0;
            this.availablePoints = 0;
            this.wonPoints = 0;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10004) {
                return this.unserialize(buffer);
            }
            final AvailablePointConverter converter = new AvailablePointConverter();
            final boolean ok = converter.unserializeVersion(buffer, version);
            if (ok) {
                converter.pushResult();
                return true;
            }
            return false;
        }
        
        @Override
        public int serializedSize() {
            return 9;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("aptitudeType=").append(this.aptitudeType).append('\n');
            repr.append(prefix).append("availablePoints=").append(this.availablePoints).append('\n');
            repr.append(prefix).append("wonPoints=").append(this.wonPoints).append('\n');
        }
        
        private final class AvailablePointConverter
        {
            private byte aptitudeType;
            private int availablePoints;
            private int wonPoints;
            
            private AvailablePointConverter() {
                super();
                this.aptitudeType = 0;
                this.availablePoints = 0;
                this.wonPoints = 0;
            }
            
            public void pushResult() {
                AvailablePoint.this.aptitudeType = this.aptitudeType;
                AvailablePoint.this.availablePoints = this.availablePoints;
                AvailablePoint.this.wonPoints = this.wonPoints;
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                this.aptitudeType = buffer.get();
                this.availablePoints = buffer.getInt();
                return true;
            }
            
            public void convert_v1_to_v10004() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 1) {
                    return false;
                }
                if (version >= 10004) {
                    return false;
                }
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v10004();
                    return true;
                }
                return false;
            }
        }
    }
    
    private final class RawAptitudeInventoryConverter
    {
        private final ArrayList<Content> contents;
        private final ArrayList<AvailablePoint> availablePointsArray;
        
        private RawAptitudeInventoryConverter() {
            super();
            this.contents = new ArrayList<Content>(0);
            this.availablePointsArray = new ArrayList<AvailablePoint>(0);
        }
        
        public void pushResult() {
            RawAptitudeInventory.this.contents.clear();
            RawAptitudeInventory.this.contents.ensureCapacity(this.contents.size());
            RawAptitudeInventory.this.contents.addAll(this.contents);
            RawAptitudeInventory.this.availablePointsArray.clear();
            RawAptitudeInventory.this.availablePointsArray.ensureCapacity(this.availablePointsArray.size());
            RawAptitudeInventory.this.availablePointsArray.addAll(this.availablePointsArray);
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            final int contents_size = buffer.getShort() & 0xFFFF;
            this.contents.clear();
            this.contents.ensureCapacity(contents_size);
            for (int i = 0; i < contents_size; ++i) {
                final Content contents_element = new Content();
                final boolean contents_element_ok = contents_element.unserializeVersion(buffer, 1);
                if (!contents_element_ok) {
                    return false;
                }
                this.contents.add(contents_element);
            }
            final int availablePointsArray_size = buffer.getShort() & 0xFFFF;
            this.availablePointsArray.clear();
            this.availablePointsArray.ensureCapacity(availablePointsArray_size);
            for (int j = 0; j < availablePointsArray_size; ++j) {
                final AvailablePoint availablePointsArray_element = new AvailablePoint();
                final boolean availablePointsArray_element_ok = availablePointsArray_element.unserializeVersion(buffer, 1);
                if (!availablePointsArray_element_ok) {
                    return false;
                }
                this.availablePointsArray.add(availablePointsArray_element);
            }
            return true;
        }
        
        public void convert_v1_to_v10004() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 1) {
                return false;
            }
            if (version >= 10004) {
                return false;
            }
            final boolean ok = this.unserialize_v1(buffer);
            if (ok) {
                this.convert_v1_to_v10004();
                return true;
            }
            return false;
        }
    }
}
