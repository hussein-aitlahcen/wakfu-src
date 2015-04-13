package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;
import java.util.*;

public class RawStateRunningEffects implements VersionableObject
{
    public final ArrayList<StateRunningEffect> effects;
    
    public RawStateRunningEffects() {
        super();
        this.effects = new ArrayList<StateRunningEffect>(0);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.effects.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.effects.size());
        for (int i = 0; i < this.effects.size(); ++i) {
            final StateRunningEffect effects_element = this.effects.get(i);
            final boolean effects_element_ok = effects_element.serialize(buffer);
            if (!effects_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int effects_size = buffer.getShort() & 0xFFFF;
        this.effects.clear();
        this.effects.ensureCapacity(effects_size);
        for (int i = 0; i < effects_size; ++i) {
            final StateRunningEffect effects_element = new StateRunningEffect();
            final boolean effects_element_ok = effects_element.unserialize(buffer);
            if (!effects_element_ok) {
                return false;
            }
            this.effects.add(effects_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.effects.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10034001) {
            return this.unserialize(buffer);
        }
        final RawStateRunningEffectsConverter converter = new RawStateRunningEffectsConverter();
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
        for (int i = 0; i < this.effects.size(); ++i) {
            final StateRunningEffect effects_element = this.effects.get(i);
            size += effects_element.serializedSize();
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
        repr.append(prefix).append("effects=");
        if (this.effects.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.effects.size()).append(" elements)...\n");
            for (int i = 0; i < this.effects.size(); ++i) {
                final StateRunningEffect effects_element = this.effects.get(i);
                effects_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class StateRunningEffect implements VersionableObject
    {
        public long uid;
        public short stateBaseId;
        public short level;
        public int remainingDurationInMs;
        public long startDate;
        public static final int SERIALIZED_SIZE = 24;
        
        public StateRunningEffect() {
            super();
            this.uid = 0L;
            this.stateBaseId = 0;
            this.level = 0;
            this.remainingDurationInMs = 0;
            this.startDate = 0L;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putLong(this.uid);
            buffer.putShort(this.stateBaseId);
            buffer.putShort(this.level);
            buffer.putInt(this.remainingDurationInMs);
            buffer.putLong(this.startDate);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.uid = buffer.getLong();
            this.stateBaseId = buffer.getShort();
            this.level = buffer.getShort();
            this.remainingDurationInMs = buffer.getInt();
            this.startDate = buffer.getLong();
            return true;
        }
        
        @Override
        public void clear() {
            this.uid = 0L;
            this.stateBaseId = 0;
            this.level = 0;
            this.remainingDurationInMs = 0;
            this.startDate = 0L;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10034001) {
                return this.unserialize(buffer);
            }
            final StateRunningEffectConverter converter = new StateRunningEffectConverter();
            final boolean ok = converter.unserializeVersion(buffer, version);
            if (ok) {
                converter.pushResult();
                return true;
            }
            return false;
        }
        
        @Override
        public int serializedSize() {
            return 24;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("uid=").append(this.uid).append('\n');
            repr.append(prefix).append("stateBaseId=").append(this.stateBaseId).append('\n');
            repr.append(prefix).append("level=").append(this.level).append('\n');
            repr.append(prefix).append("remainingDurationInMs=").append(this.remainingDurationInMs).append('\n');
            repr.append(prefix).append("startDate=").append(this.startDate).append('\n');
        }
        
        private final class StateRunningEffectConverter
        {
            private long uid;
            private short stateBaseId;
            private short level;
            private int remainingDurationInMs;
            private long startDate;
            
            private StateRunningEffectConverter() {
                super();
                this.uid = 0L;
                this.stateBaseId = 0;
                this.level = 0;
                this.remainingDurationInMs = 0;
                this.startDate = 0L;
            }
            
            public void pushResult() {
                StateRunningEffect.this.uid = this.uid;
                StateRunningEffect.this.stateBaseId = this.stateBaseId;
                StateRunningEffect.this.level = this.level;
                StateRunningEffect.this.remainingDurationInMs = this.remainingDurationInMs;
                StateRunningEffect.this.startDate = this.startDate;
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                this.uid = buffer.getLong();
                this.stateBaseId = buffer.getShort();
                this.level = buffer.getShort();
                this.remainingDurationInMs = buffer.getInt();
                return true;
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
    
    private final class RawStateRunningEffectsConverter
    {
        private final ArrayList<StateRunningEffect> effects;
        
        private RawStateRunningEffectsConverter() {
            super();
            this.effects = new ArrayList<StateRunningEffect>(0);
        }
        
        public void pushResult() {
            RawStateRunningEffects.this.effects.clear();
            RawStateRunningEffects.this.effects.ensureCapacity(this.effects.size());
            RawStateRunningEffects.this.effects.addAll(this.effects);
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            final int effects_size = buffer.getShort() & 0xFFFF;
            this.effects.clear();
            this.effects.ensureCapacity(effects_size);
            for (int i = 0; i < effects_size; ++i) {
                final StateRunningEffect effects_element = new StateRunningEffect();
                final boolean effects_element_ok = effects_element.unserializeVersion(buffer, 1);
                if (!effects_element_ok) {
                    return false;
                }
                this.effects.add(effects_element);
            }
            return true;
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
