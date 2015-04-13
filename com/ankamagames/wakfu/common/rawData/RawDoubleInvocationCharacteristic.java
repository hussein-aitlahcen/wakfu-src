package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;
import java.util.*;

public class RawDoubleInvocationCharacteristic implements VersionableObject
{
    public int power;
    public int gfxId;
    public byte sex;
    public byte haircolorindex;
    public byte haircolorfactor;
    public byte skincolorindex;
    public byte skincolorfactor;
    public byte pupilcolorindex;
    public byte clothIndex;
    public byte faceIndex;
    public byte doubleType;
    public final RawSpellLevelInventory doublespells;
    public final RawCharacteristics doubleCharac;
    public final ArrayList<EquipmentAppareance> equipmentAppareances;
    
    public RawDoubleInvocationCharacteristic() {
        super();
        this.power = 0;
        this.gfxId = 0;
        this.sex = 0;
        this.haircolorindex = 0;
        this.haircolorfactor = 0;
        this.skincolorindex = 0;
        this.skincolorfactor = 0;
        this.pupilcolorindex = 0;
        this.clothIndex = 0;
        this.faceIndex = 0;
        this.doubleType = 0;
        this.doublespells = new RawSpellLevelInventory();
        this.doubleCharac = new RawCharacteristics();
        this.equipmentAppareances = new ArrayList<EquipmentAppareance>(0);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putInt(this.power);
        buffer.putInt(this.gfxId);
        buffer.put(this.sex);
        buffer.put(this.haircolorindex);
        buffer.put(this.haircolorfactor);
        buffer.put(this.skincolorindex);
        buffer.put(this.skincolorfactor);
        buffer.put(this.pupilcolorindex);
        buffer.put(this.clothIndex);
        buffer.put(this.faceIndex);
        buffer.put(this.doubleType);
        final boolean doublespells_ok = this.doublespells.serialize(buffer);
        if (!doublespells_ok) {
            return false;
        }
        final boolean doubleCharac_ok = this.doubleCharac.serialize(buffer);
        if (!doubleCharac_ok) {
            return false;
        }
        if (this.equipmentAppareances.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.equipmentAppareances.size());
        for (int i = 0; i < this.equipmentAppareances.size(); ++i) {
            final EquipmentAppareance equipmentAppareances_element = this.equipmentAppareances.get(i);
            final boolean equipmentAppareances_element_ok = equipmentAppareances_element.serialize(buffer);
            if (!equipmentAppareances_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.power = buffer.getInt();
        this.gfxId = buffer.getInt();
        this.sex = buffer.get();
        this.haircolorindex = buffer.get();
        this.haircolorfactor = buffer.get();
        this.skincolorindex = buffer.get();
        this.skincolorfactor = buffer.get();
        this.pupilcolorindex = buffer.get();
        this.clothIndex = buffer.get();
        this.faceIndex = buffer.get();
        this.doubleType = buffer.get();
        final boolean doublespells_ok = this.doublespells.unserialize(buffer);
        if (!doublespells_ok) {
            return false;
        }
        final boolean doubleCharac_ok = this.doubleCharac.unserialize(buffer);
        if (!doubleCharac_ok) {
            return false;
        }
        final int equipmentAppareances_size = buffer.getShort() & 0xFFFF;
        this.equipmentAppareances.clear();
        this.equipmentAppareances.ensureCapacity(equipmentAppareances_size);
        for (int i = 0; i < equipmentAppareances_size; ++i) {
            final EquipmentAppareance equipmentAppareances_element = new EquipmentAppareance();
            final boolean equipmentAppareances_element_ok = equipmentAppareances_element.unserialize(buffer);
            if (!equipmentAppareances_element_ok) {
                return false;
            }
            this.equipmentAppareances.add(equipmentAppareances_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.power = 0;
        this.gfxId = 0;
        this.sex = 0;
        this.haircolorindex = 0;
        this.haircolorfactor = 0;
        this.skincolorindex = 0;
        this.skincolorfactor = 0;
        this.pupilcolorindex = 0;
        this.clothIndex = 0;
        this.faceIndex = 0;
        this.doubleType = 0;
        this.doublespells.clear();
        this.doubleCharac.clear();
        this.equipmentAppareances.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10036003) {
            return this.unserialize(buffer);
        }
        final RawDoubleInvocationCharacteristicConverter converter = new RawDoubleInvocationCharacteristicConverter();
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
        size += 4;
        size += 4;
        ++size;
        ++size;
        ++size;
        ++size;
        ++size;
        ++size;
        ++size;
        ++size;
        size = ++size + this.doublespells.serializedSize();
        size += this.doubleCharac.serializedSize();
        size += 2;
        for (int i = 0; i < this.equipmentAppareances.size(); ++i) {
            final EquipmentAppareance equipmentAppareances_element = this.equipmentAppareances.get(i);
            size += equipmentAppareances_element.serializedSize();
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
        repr.append(prefix).append("power=").append(this.power).append('\n');
        repr.append(prefix).append("gfxId=").append(this.gfxId).append('\n');
        repr.append(prefix).append("sex=").append(this.sex).append('\n');
        repr.append(prefix).append("haircolorindex=").append(this.haircolorindex).append('\n');
        repr.append(prefix).append("haircolorfactor=").append(this.haircolorfactor).append('\n');
        repr.append(prefix).append("skincolorindex=").append(this.skincolorindex).append('\n');
        repr.append(prefix).append("skincolorfactor=").append(this.skincolorfactor).append('\n');
        repr.append(prefix).append("pupilcolorindex=").append(this.pupilcolorindex).append('\n');
        repr.append(prefix).append("clothIndex=").append(this.clothIndex).append('\n');
        repr.append(prefix).append("faceIndex=").append(this.faceIndex).append('\n');
        repr.append(prefix).append("doubleType=").append(this.doubleType).append('\n');
        repr.append(prefix).append("doublespells=...\n");
        this.doublespells.internalToString(repr, prefix + "  ");
        repr.append(prefix).append("doubleCharac=...\n");
        this.doubleCharac.internalToString(repr, prefix + "  ");
        repr.append(prefix).append("equipmentAppareances=");
        if (this.equipmentAppareances.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.equipmentAppareances.size()).append(" elements)...\n");
            for (int i = 0; i < this.equipmentAppareances.size(); ++i) {
                final EquipmentAppareance equipmentAppareances_element = this.equipmentAppareances.get(i);
                equipmentAppareances_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class EquipmentAppareance implements VersionableObject
    {
        public byte position;
        public int refId;
        public static final int SERIALIZED_SIZE = 5;
        
        public EquipmentAppareance() {
            super();
            this.position = 0;
            this.refId = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.put(this.position);
            buffer.putInt(this.refId);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.position = buffer.get();
            this.refId = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.position = 0;
            this.refId = 0;
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
            repr.append(prefix).append("position=").append(this.position).append('\n');
            repr.append(prefix).append("refId=").append(this.refId).append('\n');
        }
    }
    
    private final class RawDoubleInvocationCharacteristicConverter
    {
        private int power;
        private int gfxId;
        private byte sex;
        private byte haircolorindex;
        private byte haircolorfactor;
        private byte skincolorindex;
        private byte skincolorfactor;
        private byte pupilcolorindex;
        private byte clothIndex;
        private byte faceIndex;
        private byte doubleType;
        private final RawSpellLevelInventory doublespells;
        private final RawCharacteristics doubleCharac;
        private final ArrayList<EquipmentAppareance> equipmentAppareances;
        
        private RawDoubleInvocationCharacteristicConverter() {
            super();
            this.power = 0;
            this.gfxId = 0;
            this.sex = 0;
            this.haircolorindex = 0;
            this.haircolorfactor = 0;
            this.skincolorindex = 0;
            this.skincolorfactor = 0;
            this.pupilcolorindex = 0;
            this.clothIndex = 0;
            this.faceIndex = 0;
            this.doubleType = 0;
            this.doublespells = new RawSpellLevelInventory();
            this.doubleCharac = new RawCharacteristics();
            this.equipmentAppareances = new ArrayList<EquipmentAppareance>(0);
        }
        
        public void pushResult() {
            RawDoubleInvocationCharacteristic.this.power = this.power;
            RawDoubleInvocationCharacteristic.this.gfxId = this.gfxId;
            RawDoubleInvocationCharacteristic.this.sex = this.sex;
            RawDoubleInvocationCharacteristic.this.haircolorindex = this.haircolorindex;
            RawDoubleInvocationCharacteristic.this.haircolorfactor = this.haircolorfactor;
            RawDoubleInvocationCharacteristic.this.skincolorindex = this.skincolorindex;
            RawDoubleInvocationCharacteristic.this.skincolorfactor = this.skincolorfactor;
            RawDoubleInvocationCharacteristic.this.pupilcolorindex = this.pupilcolorindex;
            RawDoubleInvocationCharacteristic.this.clothIndex = this.clothIndex;
            RawDoubleInvocationCharacteristic.this.faceIndex = this.faceIndex;
            RawDoubleInvocationCharacteristic.this.doubleType = this.doubleType;
            RawDoubleInvocationCharacteristic.this.doublespells.contents.clear();
            RawDoubleInvocationCharacteristic.this.doublespells.contents.ensureCapacity(this.doublespells.contents.size());
            RawDoubleInvocationCharacteristic.this.doublespells.contents.addAll(this.doublespells.contents);
            RawDoubleInvocationCharacteristic.this.doubleCharac.characteristics.clear();
            RawDoubleInvocationCharacteristic.this.doubleCharac.characteristics.ensureCapacity(this.doubleCharac.characteristics.size());
            RawDoubleInvocationCharacteristic.this.doubleCharac.characteristics.addAll(this.doubleCharac.characteristics);
            RawDoubleInvocationCharacteristic.this.equipmentAppareances.clear();
            RawDoubleInvocationCharacteristic.this.equipmentAppareances.ensureCapacity(this.equipmentAppareances.size());
            RawDoubleInvocationCharacteristic.this.equipmentAppareances.addAll(this.equipmentAppareances);
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            this.power = buffer.getInt();
            this.gfxId = buffer.getInt();
            this.sex = buffer.get();
            this.haircolorindex = buffer.get();
            this.haircolorfactor = buffer.get();
            this.skincolorindex = buffer.get();
            this.skincolorfactor = buffer.get();
            this.pupilcolorindex = buffer.get();
            this.clothIndex = buffer.get();
            this.faceIndex = buffer.get();
            this.doubleType = buffer.get();
            final boolean doublespells_ok = this.doublespells.unserializeVersion(buffer, 1);
            if (!doublespells_ok) {
                return false;
            }
            final boolean doubleCharac_ok = this.doubleCharac.unserializeVersion(buffer, 1);
            if (!doubleCharac_ok) {
                return false;
            }
            final int equipmentAppareances_size = buffer.getShort() & 0xFFFF;
            this.equipmentAppareances.clear();
            this.equipmentAppareances.ensureCapacity(equipmentAppareances_size);
            for (int i = 0; i < equipmentAppareances_size; ++i) {
                final EquipmentAppareance equipmentAppareances_element = new EquipmentAppareance();
                final boolean equipmentAppareances_element_ok = equipmentAppareances_element.unserializeVersion(buffer, 1);
                if (!equipmentAppareances_element_ok) {
                    return false;
                }
                this.equipmentAppareances.add(equipmentAppareances_element);
            }
            return true;
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
