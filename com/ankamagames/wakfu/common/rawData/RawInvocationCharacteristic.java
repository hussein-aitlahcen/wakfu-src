package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;
import java.util.*;

public class RawInvocationCharacteristic implements VersionableObject
{
    public short typeid;
    public String name;
    public int currentHp;
    public long summonId;
    public long currentXP;
    public short cappedLevel;
    public short forcedLevel;
    public byte obstacleId;
    public DOUBLEINVOC DOUBLEINVOC;
    public IMAGEINVOC IMAGEINVOC;
    public int direction;
    public long summonerId;
    
    public RawInvocationCharacteristic() {
        super();
        this.typeid = 0;
        this.name = null;
        this.currentHp = 0;
        this.summonId = 0L;
        this.currentXP = 0L;
        this.cappedLevel = 0;
        this.forcedLevel = 0;
        this.obstacleId = 0;
        this.DOUBLEINVOC = null;
        this.IMAGEINVOC = null;
        this.direction = 0;
        this.summonerId = 0L;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putShort(this.typeid);
        if (this.name != null) {
            final byte[] serialized_name = StringUtils.toUTF8(this.name);
            if (serialized_name.length > 65535) {
                return false;
            }
            buffer.putShort((short)serialized_name.length);
            buffer.put(serialized_name);
        }
        else {
            buffer.putShort((short)0);
        }
        buffer.putInt(this.currentHp);
        buffer.putLong(this.summonId);
        buffer.putLong(this.currentXP);
        buffer.putShort(this.cappedLevel);
        buffer.putShort(this.forcedLevel);
        buffer.put(this.obstacleId);
        if (this.DOUBLEINVOC != null) {
            buffer.put((byte)1);
            final boolean DOUBLEINVOC_ok = this.DOUBLEINVOC.serialize(buffer);
            if (!DOUBLEINVOC_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        if (this.IMAGEINVOC != null) {
            buffer.put((byte)1);
            final boolean IMAGEINVOC_ok = this.IMAGEINVOC.serialize(buffer);
            if (!IMAGEINVOC_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        buffer.putInt(this.direction);
        buffer.putLong(this.summonerId);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.typeid = buffer.getShort();
        final int name_size = buffer.getShort() & 0xFFFF;
        final byte[] serialized_name = new byte[name_size];
        buffer.get(serialized_name);
        this.name = StringUtils.fromUTF8(serialized_name);
        this.currentHp = buffer.getInt();
        this.summonId = buffer.getLong();
        this.currentXP = buffer.getLong();
        this.cappedLevel = buffer.getShort();
        this.forcedLevel = buffer.getShort();
        this.obstacleId = buffer.get();
        final boolean DOUBLEINVOC_present = buffer.get() == 1;
        if (DOUBLEINVOC_present) {
            this.DOUBLEINVOC = new DOUBLEINVOC();
            final boolean DOUBLEINVOC_ok = this.DOUBLEINVOC.unserialize(buffer);
            if (!DOUBLEINVOC_ok) {
                return false;
            }
        }
        else {
            this.DOUBLEINVOC = null;
        }
        final boolean IMAGEINVOC_present = buffer.get() == 1;
        if (IMAGEINVOC_present) {
            this.IMAGEINVOC = new IMAGEINVOC();
            final boolean IMAGEINVOC_ok = this.IMAGEINVOC.unserialize(buffer);
            if (!IMAGEINVOC_ok) {
                return false;
            }
        }
        else {
            this.IMAGEINVOC = null;
        }
        this.direction = buffer.getInt();
        this.summonerId = buffer.getLong();
        return true;
    }
    
    @Override
    public void clear() {
        this.typeid = 0;
        this.name = null;
        this.currentHp = 0;
        this.summonId = 0L;
        this.currentXP = 0L;
        this.cappedLevel = 0;
        this.forcedLevel = 0;
        this.obstacleId = 0;
        this.DOUBLEINVOC = null;
        this.IMAGEINVOC = null;
        this.direction = 0;
        this.summonerId = 0L;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10036003) {
            return this.unserialize(buffer);
        }
        final RawInvocationCharacteristicConverter converter = new RawInvocationCharacteristicConverter();
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
        size += 2;
        size += ((this.name != null) ? StringUtils.toUTF8(this.name).length : 0);
        size += 4;
        size += 8;
        size += 8;
        size += 2;
        size += 2;
        ++size;
        ++size;
        if (this.DOUBLEINVOC != null) {
            size += this.DOUBLEINVOC.serializedSize();
        }
        ++size;
        if (this.IMAGEINVOC != null) {
            size += this.IMAGEINVOC.serializedSize();
        }
        size += 4;
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
        repr.append(prefix).append("typeid=").append(this.typeid).append('\n');
        repr.append(prefix).append("name=").append(this.name).append('\n');
        repr.append(prefix).append("currentHp=").append(this.currentHp).append('\n');
        repr.append(prefix).append("summonId=").append(this.summonId).append('\n');
        repr.append(prefix).append("currentXP=").append(this.currentXP).append('\n');
        repr.append(prefix).append("cappedLevel=").append(this.cappedLevel).append('\n');
        repr.append(prefix).append("forcedLevel=").append(this.forcedLevel).append('\n');
        repr.append(prefix).append("obstacleId=").append(this.obstacleId).append('\n');
        repr.append(prefix).append("DOUBLEINVOC=");
        if (this.DOUBLEINVOC == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.DOUBLEINVOC.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("IMAGEINVOC=");
        if (this.IMAGEINVOC == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.IMAGEINVOC.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("direction=").append(this.direction).append('\n');
        repr.append(prefix).append("summonerId=").append(this.summonerId).append('\n');
    }
    
    public static class DOUBLEINVOC implements VersionableObject
    {
        public final RawDoubleInvocationCharacteristic doubledata;
        
        public DOUBLEINVOC() {
            super();
            this.doubledata = new RawDoubleInvocationCharacteristic();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean doubledata_ok = this.doubledata.serialize(buffer);
            return doubledata_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean doubledata_ok = this.doubledata.unserialize(buffer);
            return doubledata_ok;
        }
        
        @Override
        public void clear() {
            this.doubledata.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10036003) {
                return this.unserialize(buffer);
            }
            final DOUBLEINVOCConverter converter = new DOUBLEINVOCConverter();
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
            size += this.doubledata.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("doubledata=...\n");
            this.doubledata.internalToString(repr, prefix + "  ");
        }
        
        private final class DOUBLEINVOCConverter
        {
            private final RawDoubleInvocationCharacteristic doubledata;
            
            private DOUBLEINVOCConverter() {
                super();
                this.doubledata = new RawDoubleInvocationCharacteristic();
            }
            
            public void pushResult() {
                DOUBLEINVOC.this.doubledata.power = this.doubledata.power;
                DOUBLEINVOC.this.doubledata.gfxId = this.doubledata.gfxId;
                DOUBLEINVOC.this.doubledata.sex = this.doubledata.sex;
                DOUBLEINVOC.this.doubledata.haircolorindex = this.doubledata.haircolorindex;
                DOUBLEINVOC.this.doubledata.haircolorfactor = this.doubledata.haircolorfactor;
                DOUBLEINVOC.this.doubledata.skincolorindex = this.doubledata.skincolorindex;
                DOUBLEINVOC.this.doubledata.skincolorfactor = this.doubledata.skincolorfactor;
                DOUBLEINVOC.this.doubledata.pupilcolorindex = this.doubledata.pupilcolorindex;
                DOUBLEINVOC.this.doubledata.clothIndex = this.doubledata.clothIndex;
                DOUBLEINVOC.this.doubledata.faceIndex = this.doubledata.faceIndex;
                DOUBLEINVOC.this.doubledata.doubleType = this.doubledata.doubleType;
                DOUBLEINVOC.this.doubledata.doublespells.contents.clear();
                DOUBLEINVOC.this.doubledata.doublespells.contents.ensureCapacity(this.doubledata.doublespells.contents.size());
                DOUBLEINVOC.this.doubledata.doublespells.contents.addAll(this.doubledata.doublespells.contents);
                DOUBLEINVOC.this.doubledata.doubleCharac.characteristics.clear();
                DOUBLEINVOC.this.doubledata.doubleCharac.characteristics.ensureCapacity(this.doubledata.doubleCharac.characteristics.size());
                DOUBLEINVOC.this.doubledata.doubleCharac.characteristics.addAll(this.doubledata.doubleCharac.characteristics);
                DOUBLEINVOC.this.doubledata.equipmentAppareances.clear();
                DOUBLEINVOC.this.doubledata.equipmentAppareances.ensureCapacity(this.doubledata.equipmentAppareances.size());
                DOUBLEINVOC.this.doubledata.equipmentAppareances.addAll(this.doubledata.equipmentAppareances);
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                final boolean doubledata_ok = this.doubledata.unserializeVersion(buffer, 1);
                return doubledata_ok;
            }
            
            public void convert_v1_to_v10036003() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 1) {
                    return false;
                }
                if (version >= 10036003) {
                    return false;
                }
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v10036003();
                    return true;
                }
                return false;
            }
        }
    }
    
    public static class IMAGEINVOC implements VersionableObject
    {
        public final RawImageCharacteristic imagedata;
        
        public IMAGEINVOC() {
            super();
            this.imagedata = new RawImageCharacteristic();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean imagedata_ok = this.imagedata.serialize(buffer);
            return imagedata_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean imagedata_ok = this.imagedata.unserialize(buffer);
            return imagedata_ok;
        }
        
        @Override
        public void clear() {
            this.imagedata.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10036003) {
                return this.unserialize(buffer);
            }
            final IMAGEINVOCConverter converter = new IMAGEINVOCConverter();
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
            size += this.imagedata.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("imagedata=...\n");
            this.imagedata.internalToString(repr, prefix + "  ");
        }
        
        private final class IMAGEINVOCConverter
        {
            private final RawImageCharacteristic imagedata;
            
            private IMAGEINVOCConverter() {
                super();
                this.imagedata = new RawImageCharacteristic();
            }
            
            public void pushResult() {
                IMAGEINVOC.this.imagedata.gfxId = this.imagedata.gfxId;
                IMAGEINVOC.this.imagedata.sex = this.imagedata.sex;
                IMAGEINVOC.this.imagedata.imageCharac.characteristics.clear();
                IMAGEINVOC.this.imagedata.imageCharac.characteristics.ensureCapacity(this.imagedata.imageCharac.characteristics.size());
                IMAGEINVOC.this.imagedata.imageCharac.characteristics.addAll(this.imagedata.imageCharac.characteristics);
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                final boolean imagedata_ok = this.imagedata.unserializeVersion(buffer, 1);
                return imagedata_ok;
            }
            
            public void convert_v1_to_v10036003() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 1) {
                    return false;
                }
                if (version >= 10036003) {
                    return false;
                }
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v10036003();
                    return true;
                }
                return false;
            }
        }
    }
    
    private final class RawInvocationCharacteristicConverter
    {
        private short typeid;
        private String name;
        private int currentHp;
        private long summonId;
        private long currentXP;
        private short cappedLevel;
        private short forcedLevel;
        private byte obstacleId;
        private DOUBLEINVOC DOUBLEINVOC;
        private IMAGEINVOC IMAGEINVOC;
        private int direction;
        private long summonerId;
        
        private RawInvocationCharacteristicConverter() {
            super();
            this.typeid = 0;
            this.name = null;
            this.currentHp = 0;
            this.summonId = 0L;
            this.currentXP = 0L;
            this.cappedLevel = 0;
            this.forcedLevel = 0;
            this.obstacleId = 0;
            this.DOUBLEINVOC = null;
            this.IMAGEINVOC = null;
            this.direction = 0;
            this.summonerId = 0L;
        }
        
        public void pushResult() {
            RawInvocationCharacteristic.this.typeid = this.typeid;
            RawInvocationCharacteristic.this.name = this.name;
            RawInvocationCharacteristic.this.currentHp = this.currentHp;
            RawInvocationCharacteristic.this.summonId = this.summonId;
            RawInvocationCharacteristic.this.currentXP = this.currentXP;
            RawInvocationCharacteristic.this.cappedLevel = this.cappedLevel;
            RawInvocationCharacteristic.this.forcedLevel = this.forcedLevel;
            RawInvocationCharacteristic.this.obstacleId = this.obstacleId;
            RawInvocationCharacteristic.this.DOUBLEINVOC = this.DOUBLEINVOC;
            RawInvocationCharacteristic.this.IMAGEINVOC = this.IMAGEINVOC;
            RawInvocationCharacteristic.this.direction = this.direction;
            RawInvocationCharacteristic.this.summonerId = this.summonerId;
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            this.typeid = buffer.getShort();
            final int name_size = buffer.getShort() & 0xFFFF;
            final byte[] serialized_name = new byte[name_size];
            buffer.get(serialized_name);
            this.name = StringUtils.fromUTF8(serialized_name);
            this.currentHp = buffer.getInt();
            this.summonId = buffer.getLong();
            this.currentXP = buffer.getLong();
            this.cappedLevel = buffer.getShort();
            this.obstacleId = buffer.get();
            final boolean DOUBLEINVOC_present = buffer.get() == 1;
            if (DOUBLEINVOC_present) {
                this.DOUBLEINVOC = new DOUBLEINVOC();
                final boolean DOUBLEINVOC_ok = this.DOUBLEINVOC.unserializeVersion(buffer, 1);
                if (!DOUBLEINVOC_ok) {
                    return false;
                }
            }
            else {
                this.DOUBLEINVOC = null;
            }
            final boolean IMAGEINVOC_present = buffer.get() == 1;
            if (IMAGEINVOC_present) {
                this.IMAGEINVOC = new IMAGEINVOC();
                final boolean IMAGEINVOC_ok = this.IMAGEINVOC.unserializeVersion(buffer, 1);
                if (!IMAGEINVOC_ok) {
                    return false;
                }
            }
            else {
                this.IMAGEINVOC = null;
            }
            this.direction = buffer.getInt();
            this.summonerId = buffer.getLong();
            return true;
        }
        
        private boolean unserialize_v10013(final ByteBuffer buffer) {
            this.typeid = buffer.getShort();
            final int name_size = buffer.getShort() & 0xFFFF;
            final byte[] serialized_name = new byte[name_size];
            buffer.get(serialized_name);
            this.name = StringUtils.fromUTF8(serialized_name);
            this.currentHp = buffer.getInt();
            this.summonId = buffer.getLong();
            this.currentXP = buffer.getLong();
            this.cappedLevel = buffer.getShort();
            this.forcedLevel = buffer.getShort();
            this.obstacleId = buffer.get();
            final boolean DOUBLEINVOC_present = buffer.get() == 1;
            if (DOUBLEINVOC_present) {
                this.DOUBLEINVOC = new DOUBLEINVOC();
                final boolean DOUBLEINVOC_ok = this.DOUBLEINVOC.unserializeVersion(buffer, 10013);
                if (!DOUBLEINVOC_ok) {
                    return false;
                }
            }
            else {
                this.DOUBLEINVOC = null;
            }
            final boolean IMAGEINVOC_present = buffer.get() == 1;
            if (IMAGEINVOC_present) {
                this.IMAGEINVOC = new IMAGEINVOC();
                final boolean IMAGEINVOC_ok = this.IMAGEINVOC.unserializeVersion(buffer, 10013);
                if (!IMAGEINVOC_ok) {
                    return false;
                }
            }
            else {
                this.IMAGEINVOC = null;
            }
            this.direction = buffer.getInt();
            this.summonerId = buffer.getLong();
            return true;
        }
        
        public void convert_v0_to_v1() {
        }
        
        public void convert_v1_to_v10013() {
        }
        
        public void convert_v10013_to_v10036003() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 0) {
                return false;
            }
            if (version < 1) {
                final boolean ok = this.unserialize_v0(buffer);
                if (ok) {
                    this.convert_v0_to_v1();
                    this.convert_v1_to_v10013();
                    this.convert_v10013_to_v10036003();
                    return true;
                }
                return false;
            }
            else if (version < 10013) {
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v10013();
                    this.convert_v10013_to_v10036003();
                    return true;
                }
                return false;
            }
            else {
                if (version >= 10036003) {
                    return false;
                }
                final boolean ok = this.unserialize_v10013(buffer);
                if (ok) {
                    this.convert_v10013_to_v10036003();
                    return true;
                }
                return false;
            }
        }
    }
}
