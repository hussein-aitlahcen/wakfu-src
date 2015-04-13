package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.wakfu.common.rawData.*;

public class CharacterSerializedFight extends CharacterSerializedPart implements VersionableObject
{
    public int currentFightId;
    public boolean isKo;
    public boolean isDead;
    public boolean isSummoned;
    public boolean isFleeing;
    public byte obstacleId;
    public SUMMONDATA SUMMONDATA;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedFight() {
        super();
        this.currentFightId = 0;
        this.isKo = false;
        this.isDead = false;
        this.isSummoned = false;
        this.isFleeing = false;
        this.obstacleId = -1;
        this.SUMMONDATA = null;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedFight.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedFight");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedFight", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedFight.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedFight");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedFight", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedFight.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putInt(this.currentFightId);
        buffer.put((byte)(this.isKo ? 1 : 0));
        buffer.put((byte)(this.isDead ? 1 : 0));
        buffer.put((byte)(this.isSummoned ? 1 : 0));
        buffer.put((byte)(this.isFleeing ? 1 : 0));
        buffer.put(this.obstacleId);
        if (this.SUMMONDATA != null) {
            buffer.put((byte)1);
            final boolean SUMMONDATA_ok = this.SUMMONDATA.serialize(buffer);
            if (!SUMMONDATA_ok) {
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
        this.currentFightId = buffer.getInt();
        this.isKo = (buffer.get() == 1);
        this.isDead = (buffer.get() == 1);
        this.isSummoned = (buffer.get() == 1);
        this.isFleeing = (buffer.get() == 1);
        this.obstacleId = buffer.get();
        final boolean SUMMONDATA_present = buffer.get() == 1;
        if (SUMMONDATA_present) {
            this.SUMMONDATA = new SUMMONDATA();
            final boolean SUMMONDATA_ok = this.SUMMONDATA.unserialize(buffer);
            if (!SUMMONDATA_ok) {
                return false;
            }
        }
        else {
            this.SUMMONDATA = null;
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.currentFightId = 0;
        this.isKo = false;
        this.isDead = false;
        this.isSummoned = false;
        this.isFleeing = false;
        this.obstacleId = -1;
        this.SUMMONDATA = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10036003) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedFightConverter converter = new CharacterSerializedFightConverter();
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
        ++size;
        ++size;
        ++size;
        ++size;
        ++size;
        ++size;
        if (this.SUMMONDATA != null) {
            size += this.SUMMONDATA.serializedSize();
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
        repr.append(prefix).append("currentFightId=").append(this.currentFightId).append('\n');
        repr.append(prefix).append("isKo=").append(this.isKo).append('\n');
        repr.append(prefix).append("isDead=").append(this.isDead).append('\n');
        repr.append(prefix).append("isSummoned=").append(this.isSummoned).append('\n');
        repr.append(prefix).append("isFleeing=").append(this.isFleeing).append('\n');
        repr.append(prefix).append("obstacleId=").append(this.obstacleId).append('\n');
        repr.append(prefix).append("SUMMONDATA=");
        if (this.SUMMONDATA == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.SUMMONDATA.internalToString(repr, prefix + "  ");
        }
    }
    
    public static class SUMMONDATA implements VersionableObject
    {
        public final RawInvocationCharacteristic summon;
        
        public SUMMONDATA() {
            super();
            this.summon = new RawInvocationCharacteristic();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean summon_ok = this.summon.serialize(buffer);
            return summon_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean summon_ok = this.summon.unserialize(buffer);
            return summon_ok;
        }
        
        @Override
        public void clear() {
            this.summon.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10036003) {
                return this.unserialize(buffer);
            }
            final SUMMONDATAConverter converter = new SUMMONDATAConverter();
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
            size += this.summon.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("summon=...\n");
            this.summon.internalToString(repr, prefix + "  ");
        }
        
        private final class SUMMONDATAConverter
        {
            private final RawInvocationCharacteristic summon;
            
            private SUMMONDATAConverter() {
                super();
                this.summon = new RawInvocationCharacteristic();
            }
            
            public void pushResult() {
                SUMMONDATA.this.summon.typeid = this.summon.typeid;
                SUMMONDATA.this.summon.name = this.summon.name;
                SUMMONDATA.this.summon.currentHp = this.summon.currentHp;
                SUMMONDATA.this.summon.summonId = this.summon.summonId;
                SUMMONDATA.this.summon.currentXP = this.summon.currentXP;
                SUMMONDATA.this.summon.cappedLevel = this.summon.cappedLevel;
                SUMMONDATA.this.summon.forcedLevel = this.summon.forcedLevel;
                SUMMONDATA.this.summon.obstacleId = this.summon.obstacleId;
                SUMMONDATA.this.summon.DOUBLEINVOC = this.summon.DOUBLEINVOC;
                SUMMONDATA.this.summon.IMAGEINVOC = this.summon.IMAGEINVOC;
                SUMMONDATA.this.summon.direction = this.summon.direction;
                SUMMONDATA.this.summon.summonerId = this.summon.summonerId;
            }
            
            private boolean unserialize_v0(final ByteBuffer buffer) {
                return true;
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                final boolean summon_ok = this.summon.unserializeVersion(buffer, 1);
                return summon_ok;
            }
            
            private boolean unserialize_v10013(final ByteBuffer buffer) {
                final boolean summon_ok = this.summon.unserializeVersion(buffer, 10013);
                return summon_ok;
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
    
    private final class CharacterSerializedFightConverter
    {
        private int currentFightId;
        private boolean isKo;
        private boolean isDead;
        private boolean isSummoned;
        private boolean isFleeing;
        private byte obstacleId;
        private SUMMONDATA SUMMONDATA;
        
        private CharacterSerializedFightConverter() {
            super();
            this.currentFightId = 0;
            this.isKo = false;
            this.isDead = false;
            this.isSummoned = false;
            this.isFleeing = false;
            this.obstacleId = -1;
            this.SUMMONDATA = null;
        }
        
        public void pushResult() {
            CharacterSerializedFight.this.currentFightId = this.currentFightId;
            CharacterSerializedFight.this.isKo = this.isKo;
            CharacterSerializedFight.this.isDead = this.isDead;
            CharacterSerializedFight.this.isSummoned = this.isSummoned;
            CharacterSerializedFight.this.isFleeing = this.isFleeing;
            CharacterSerializedFight.this.obstacleId = this.obstacleId;
            CharacterSerializedFight.this.SUMMONDATA = this.SUMMONDATA;
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            this.currentFightId = buffer.getInt();
            this.isKo = (buffer.get() == 1);
            this.isDead = (buffer.get() == 1);
            this.isSummoned = (buffer.get() == 1);
            this.isFleeing = (buffer.get() == 1);
            this.obstacleId = buffer.get();
            final boolean SUMMONDATA_present = buffer.get() == 1;
            if (SUMMONDATA_present) {
                this.SUMMONDATA = new SUMMONDATA();
                final boolean SUMMONDATA_ok = this.SUMMONDATA.unserializeVersion(buffer, 1);
                if (!SUMMONDATA_ok) {
                    return false;
                }
            }
            else {
                this.SUMMONDATA = null;
            }
            return true;
        }
        
        private boolean unserialize_v10013(final ByteBuffer buffer) {
            this.currentFightId = buffer.getInt();
            this.isKo = (buffer.get() == 1);
            this.isDead = (buffer.get() == 1);
            this.isSummoned = (buffer.get() == 1);
            this.isFleeing = (buffer.get() == 1);
            this.obstacleId = buffer.get();
            final boolean SUMMONDATA_present = buffer.get() == 1;
            if (SUMMONDATA_present) {
                this.SUMMONDATA = new SUMMONDATA();
                final boolean SUMMONDATA_ok = this.SUMMONDATA.unserializeVersion(buffer, 10013);
                if (!SUMMONDATA_ok) {
                    return false;
                }
            }
            else {
                this.SUMMONDATA = null;
            }
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
