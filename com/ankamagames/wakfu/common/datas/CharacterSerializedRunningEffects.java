package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.util.*;

public class CharacterSerializedRunningEffects extends CharacterSerializedPart implements VersionableObject
{
    public InFightData inFightData;
    public OutFightData outFightData;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedRunningEffects() {
        super();
        this.inFightData = null;
        this.outFightData = null;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedRunningEffects.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedRunningEffects");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedRunningEffects", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedRunningEffects.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedRunningEffects");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedRunningEffects", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedRunningEffects.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.inFightData != null) {
            buffer.put((byte)1);
            final boolean inFightData_ok = this.inFightData.serialize(buffer);
            if (!inFightData_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        if (this.outFightData != null) {
            buffer.put((byte)1);
            final boolean outFightData_ok = this.outFightData.serialize(buffer);
            if (!outFightData_ok) {
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
        final boolean inFightData_present = buffer.get() == 1;
        if (inFightData_present) {
            this.inFightData = new InFightData();
            final boolean inFightData_ok = this.inFightData.unserialize(buffer);
            if (!inFightData_ok) {
                return false;
            }
        }
        else {
            this.inFightData = null;
        }
        final boolean outFightData_present = buffer.get() == 1;
        if (outFightData_present) {
            this.outFightData = new OutFightData();
            final boolean outFightData_ok = this.outFightData.unserialize(buffer);
            if (!outFightData_ok) {
                return false;
            }
        }
        else {
            this.outFightData = null;
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.inFightData = null;
        this.outFightData = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10034001) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedRunningEffectsConverter converter = new CharacterSerializedRunningEffectsConverter();
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
        ++size;
        if (this.inFightData != null) {
            size += this.inFightData.serializedSize();
        }
        ++size;
        if (this.outFightData != null) {
            size += this.outFightData.serializedSize();
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
        repr.append(prefix).append("inFightData=");
        if (this.inFightData == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.inFightData.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("outFightData=");
        if (this.outFightData == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.outFightData.internalToString(repr, prefix + "  ");
        }
    }
    
    public static class InFightData implements VersionableObject
    {
        public byte[] data;
        
        public InFightData() {
            super();
            this.data = null;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            if (this.data != null) {
                if (this.data.length > 65535) {
                    return false;
                }
                buffer.putShort((short)this.data.length);
                buffer.put(this.data);
            }
            else {
                buffer.putShort((short)0);
            }
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final int data_size = buffer.getShort() & 0xFFFF;
            if (data_size > 0) {
                buffer.get(this.data = new byte[data_size]);
            }
            else {
                this.data = null;
            }
            return true;
        }
        
        @Override
        public void clear() {
            this.data = null;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            int size = 0;
            size += 2;
            size += ((this.data != null) ? this.data.length : 0);
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("data=(").append((this.data != null) ? this.data.length : 0).append(" bytes)\n");
        }
    }
    
    public static class OutFightData implements VersionableObject
    {
        public final RawStateRunningEffects stateRunningEffects;
        
        public OutFightData() {
            super();
            this.stateRunningEffects = new RawStateRunningEffects();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean stateRunningEffects_ok = this.stateRunningEffects.serialize(buffer);
            return stateRunningEffects_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean stateRunningEffects_ok = this.stateRunningEffects.unserialize(buffer);
            return stateRunningEffects_ok;
        }
        
        @Override
        public void clear() {
            this.stateRunningEffects.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10034001) {
                return this.unserialize(buffer);
            }
            final OutFightDataConverter converter = new OutFightDataConverter();
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
            size += this.stateRunningEffects.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("stateRunningEffects=...\n");
            this.stateRunningEffects.internalToString(repr, prefix + "  ");
        }
        
        private final class OutFightDataConverter
        {
            private final RawStateRunningEffects stateRunningEffects;
            
            private OutFightDataConverter() {
                super();
                this.stateRunningEffects = new RawStateRunningEffects();
            }
            
            public void pushResult() {
                OutFightData.this.stateRunningEffects.effects.clear();
                OutFightData.this.stateRunningEffects.effects.ensureCapacity(this.stateRunningEffects.effects.size());
                OutFightData.this.stateRunningEffects.effects.addAll(this.stateRunningEffects.effects);
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                final boolean stateRunningEffects_ok = this.stateRunningEffects.unserializeVersion(buffer, 1);
                return stateRunningEffects_ok;
            }
            
            public void convert_v1_to_v10034001() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 1) {
                    return false;
                }
                if (version >= 10034001) {
                    return false;
                }
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v10034001();
                    return true;
                }
                return false;
            }
        }
    }
    
    private final class CharacterSerializedRunningEffectsConverter
    {
        private InFightData inFightData;
        private OutFightData outFightData;
        
        private CharacterSerializedRunningEffectsConverter() {
            super();
            this.inFightData = null;
            this.outFightData = null;
        }
        
        public void pushResult() {
            CharacterSerializedRunningEffects.this.inFightData = this.inFightData;
            CharacterSerializedRunningEffects.this.outFightData = this.outFightData;
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            final boolean inFightData_present = buffer.get() == 1;
            if (inFightData_present) {
                this.inFightData = new InFightData();
                final boolean inFightData_ok = this.inFightData.unserializeVersion(buffer, 1);
                if (!inFightData_ok) {
                    return false;
                }
            }
            else {
                this.inFightData = null;
            }
            final boolean outFightData_present = buffer.get() == 1;
            if (outFightData_present) {
                this.outFightData = new OutFightData();
                final boolean outFightData_ok = this.outFightData.unserializeVersion(buffer, 1);
                if (!outFightData_ok) {
                    return false;
                }
            }
            else {
                this.outFightData = null;
            }
            return true;
        }
        
        public void convert_v0_to_v1() {
        }
        
        public void convert_v1_to_v10034001() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 0) {
                return false;
            }
            if (version < 1) {
                final boolean ok = this.unserialize_v0(buffer);
                if (ok) {
                    this.convert_v0_to_v1();
                    this.convert_v1_to_v10034001();
                    return true;
                }
                return false;
            }
            else {
                if (version >= 10034001) {
                    return false;
                }
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v10034001();
                    return true;
                }
                return false;
            }
        }
    }
}
