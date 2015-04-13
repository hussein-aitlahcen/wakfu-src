package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.util.*;
import java.nio.*;

public class RawBonusPointCharacteristics implements VersionableObject
{
    public short freePoints;
    public final ArrayList<XpBonusPoint> xpBonusPoints;
    public final ArrayList<CharacteristicBonusPoint> characteristicBonusPoints;
    
    public RawBonusPointCharacteristics() {
        super();
        this.freePoints = 0;
        this.xpBonusPoints = new ArrayList<XpBonusPoint>(0);
        this.characteristicBonusPoints = new ArrayList<CharacteristicBonusPoint>(0);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putShort(this.freePoints);
        if (this.xpBonusPoints.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.xpBonusPoints.size());
        for (int i = 0; i < this.xpBonusPoints.size(); ++i) {
            final XpBonusPoint xpBonusPoints_element = this.xpBonusPoints.get(i);
            final boolean xpBonusPoints_element_ok = xpBonusPoints_element.serialize(buffer);
            if (!xpBonusPoints_element_ok) {
                return false;
            }
        }
        if (this.characteristicBonusPoints.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.characteristicBonusPoints.size());
        for (int i = 0; i < this.characteristicBonusPoints.size(); ++i) {
            final CharacteristicBonusPoint characteristicBonusPoints_element = this.characteristicBonusPoints.get(i);
            final boolean characteristicBonusPoints_element_ok = characteristicBonusPoints_element.serialize(buffer);
            if (!characteristicBonusPoints_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.freePoints = buffer.getShort();
        final int xpBonusPoints_size = buffer.getShort() & 0xFFFF;
        this.xpBonusPoints.clear();
        this.xpBonusPoints.ensureCapacity(xpBonusPoints_size);
        for (int i = 0; i < xpBonusPoints_size; ++i) {
            final XpBonusPoint xpBonusPoints_element = new XpBonusPoint();
            final boolean xpBonusPoints_element_ok = xpBonusPoints_element.unserialize(buffer);
            if (!xpBonusPoints_element_ok) {
                return false;
            }
            this.xpBonusPoints.add(xpBonusPoints_element);
        }
        final int characteristicBonusPoints_size = buffer.getShort() & 0xFFFF;
        this.characteristicBonusPoints.clear();
        this.characteristicBonusPoints.ensureCapacity(characteristicBonusPoints_size);
        for (int j = 0; j < characteristicBonusPoints_size; ++j) {
            final CharacteristicBonusPoint characteristicBonusPoints_element = new CharacteristicBonusPoint();
            final boolean characteristicBonusPoints_element_ok = characteristicBonusPoints_element.unserialize(buffer);
            if (!characteristicBonusPoints_element_ok) {
                return false;
            }
            this.characteristicBonusPoints.add(characteristicBonusPoints_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.freePoints = 0;
        this.xpBonusPoints.clear();
        this.characteristicBonusPoints.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 2;
        size += 2;
        for (int i = 0; i < this.xpBonusPoints.size(); ++i) {
            final XpBonusPoint xpBonusPoints_element = this.xpBonusPoints.get(i);
            size += xpBonusPoints_element.serializedSize();
        }
        size += 2;
        for (int i = 0; i < this.characteristicBonusPoints.size(); ++i) {
            final CharacteristicBonusPoint characteristicBonusPoints_element = this.characteristicBonusPoints.get(i);
            size += characteristicBonusPoints_element.serializedSize();
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
        repr.append(prefix).append("freePoints=").append(this.freePoints).append('\n');
        repr.append(prefix).append("xpBonusPoints=");
        if (this.xpBonusPoints.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.xpBonusPoints.size()).append(" elements)...\n");
            for (int i = 0; i < this.xpBonusPoints.size(); ++i) {
                final XpBonusPoint xpBonusPoints_element = this.xpBonusPoints.get(i);
                xpBonusPoints_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("characteristicBonusPoints=");
        if (this.characteristicBonusPoints.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.characteristicBonusPoints.size()).append(" elements)...\n");
            for (int i = 0; i < this.characteristicBonusPoints.size(); ++i) {
                final CharacteristicBonusPoint characteristicBonusPoints_element = this.characteristicBonusPoints.get(i);
                characteristicBonusPoints_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class XpBonusPoint implements VersionableObject
    {
        public byte characId;
        public short nbPoint;
        public static final int SERIALIZED_SIZE = 3;
        
        public XpBonusPoint() {
            super();
            this.characId = 0;
            this.nbPoint = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.put(this.characId);
            buffer.putShort(this.nbPoint);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.characId = buffer.get();
            this.nbPoint = buffer.getShort();
            return true;
        }
        
        @Override
        public void clear() {
            this.characId = 0;
            this.nbPoint = 0;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 3;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("characId=").append(this.characId).append('\n');
            repr.append(prefix).append("nbPoint=").append(this.nbPoint).append('\n');
        }
    }
    
    public static class CharacteristicBonusPoint implements VersionableObject
    {
        public byte characId;
        public short value;
        public static final int SERIALIZED_SIZE = 3;
        
        public CharacteristicBonusPoint() {
            super();
            this.characId = 0;
            this.value = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.put(this.characId);
            buffer.putShort(this.value);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.characId = buffer.get();
            this.value = buffer.getShort();
            return true;
        }
        
        @Override
        public void clear() {
            this.characId = 0;
            this.value = 0;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 3;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("characId=").append(this.characId).append('\n');
            repr.append(prefix).append("value=").append(this.value).append('\n');
        }
    }
}
