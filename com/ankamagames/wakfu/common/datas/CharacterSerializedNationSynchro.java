package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public class CharacterSerializedNationSynchro extends CharacterSerializedPart implements VersionableObject
{
    public long rank;
    public long jobs;
    public long voteDate;
    public byte governmentOpinion;
    public boolean isCandidate;
    public byte pvpState;
    public long pvpDate;
    public byte pvpRank;
    private final BinarSerialPart m_binarPart;
    public static final int SERIALIZED_SIZE = 36;
    
    public CharacterSerializedNationSynchro() {
        super();
        this.rank = 0L;
        this.jobs = 0L;
        this.voteDate = 0L;
        this.governmentOpinion = 0;
        this.isCandidate = false;
        this.pvpState = 0;
        this.pvpDate = 0L;
        this.pvpRank = 0;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedNationSynchro.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedNationSynchro");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedNationSynchro", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedNationSynchro.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedNationSynchro");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedNationSynchro", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedNationSynchro.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putLong(this.rank);
        buffer.putLong(this.jobs);
        buffer.putLong(this.voteDate);
        buffer.put(this.governmentOpinion);
        buffer.put((byte)(this.isCandidate ? 1 : 0));
        buffer.put(this.pvpState);
        buffer.putLong(this.pvpDate);
        buffer.put(this.pvpRank);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.rank = buffer.getLong();
        this.jobs = buffer.getLong();
        this.voteDate = buffer.getLong();
        this.governmentOpinion = buffer.get();
        this.isCandidate = (buffer.get() == 1);
        this.pvpState = buffer.get();
        this.pvpDate = buffer.getLong();
        this.pvpRank = buffer.get();
        return true;
    }
    
    @Override
    public void clear() {
        this.rank = 0L;
        this.jobs = 0L;
        this.voteDate = 0L;
        this.governmentOpinion = 0;
        this.isCandidate = false;
        this.pvpState = 0;
        this.pvpDate = 0L;
        this.pvpRank = 0;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10036009) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedNationSynchroConverter converter = new CharacterSerializedNationSynchroConverter();
        final boolean ok = converter.unserializeVersion(buffer, version);
        if (ok) {
            converter.pushResult();
            return true;
        }
        return false;
    }
    
    @Override
    public int serializedSize() {
        return 36;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("rank=").append(this.rank).append('\n');
        repr.append(prefix).append("jobs=").append(this.jobs).append('\n');
        repr.append(prefix).append("voteDate=").append(this.voteDate).append('\n');
        repr.append(prefix).append("governmentOpinion=").append(this.governmentOpinion).append('\n');
        repr.append(prefix).append("isCandidate=").append(this.isCandidate).append('\n');
        repr.append(prefix).append("pvpState=").append(this.pvpState).append('\n');
        repr.append(prefix).append("pvpDate=").append(this.pvpDate).append('\n');
        repr.append(prefix).append("pvpRank=").append(this.pvpRank).append('\n');
    }
    
    private final class CharacterSerializedNationSynchroConverter
    {
        private long rank;
        private long jobs;
        private long voteDate;
        private byte governmentOpinion;
        private boolean isCandidate;
        private byte pvpState;
        private long pvpDate;
        private byte pvpRank;
        
        private CharacterSerializedNationSynchroConverter() {
            super();
            this.rank = 0L;
            this.jobs = 0L;
            this.voteDate = 0L;
            this.governmentOpinion = 0;
            this.isCandidate = false;
            this.pvpState = 0;
            this.pvpDate = 0L;
            this.pvpRank = 0;
        }
        
        public void pushResult() {
            CharacterSerializedNationSynchro.this.rank = this.rank;
            CharacterSerializedNationSynchro.this.jobs = this.jobs;
            CharacterSerializedNationSynchro.this.voteDate = this.voteDate;
            CharacterSerializedNationSynchro.this.governmentOpinion = this.governmentOpinion;
            CharacterSerializedNationSynchro.this.isCandidate = this.isCandidate;
            CharacterSerializedNationSynchro.this.pvpState = this.pvpState;
            CharacterSerializedNationSynchro.this.pvpDate = this.pvpDate;
            CharacterSerializedNationSynchro.this.pvpRank = this.pvpRank;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            this.rank = buffer.getLong();
            this.jobs = buffer.getLong();
            this.voteDate = buffer.getLong();
            this.governmentOpinion = buffer.get();
            this.isCandidate = (buffer.get() == 1);
            return true;
        }
        
        private boolean unserialize_v10036007(final ByteBuffer buffer) {
            this.rank = buffer.getLong();
            this.jobs = buffer.getLong();
            this.voteDate = buffer.getLong();
            this.governmentOpinion = buffer.get();
            this.isCandidate = (buffer.get() == 1);
            this.pvpState = buffer.get();
            this.pvpDate = buffer.getLong();
            return true;
        }
        
        public void convert_v1_to_v10036007() {
        }
        
        public void convert_v10036007_to_v10036009() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 1) {
                return false;
            }
            if (version < 10036007) {
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v10036007();
                    this.convert_v10036007_to_v10036009();
                    return true;
                }
                return false;
            }
            else {
                if (version >= 10036009) {
                    return false;
                }
                final boolean ok = this.unserialize_v10036007(buffer);
                if (ok) {
                    this.convert_v10036007_to_v10036009();
                    return true;
                }
                return false;
            }
        }
    }
}
