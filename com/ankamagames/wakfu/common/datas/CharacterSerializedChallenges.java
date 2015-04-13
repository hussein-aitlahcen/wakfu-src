package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import java.util.*;

public class CharacterSerializedChallenges extends CharacterSerializedPart implements VersionableObject
{
    public final RawScenarioManager challenges;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedChallenges() {
        super();
        this.challenges = new RawScenarioManager();
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedChallenges.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedChallenges");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedChallenges", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedChallenges.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedChallenges");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedChallenges", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedChallenges.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        final boolean challenges_ok = this.challenges.serialize(buffer);
        return challenges_ok;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final boolean challenges_ok = this.challenges.unserialize(buffer);
        return challenges_ok;
    }
    
    @Override
    public void clear() {
        this.challenges.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 1) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedChallengesConverter converter = new CharacterSerializedChallengesConverter();
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
        size += this.challenges.serializedSize();
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("challenges=...\n");
        this.challenges.internalToString(repr, prefix + "  ");
    }
    
    private final class CharacterSerializedChallengesConverter
    {
        private final RawScenarioManager challenges;
        
        private CharacterSerializedChallengesConverter() {
            super();
            this.challenges = new RawScenarioManager();
        }
        
        public void pushResult() {
            CharacterSerializedChallenges.this.challenges.currentScenarii = this.challenges.currentScenarii;
            CharacterSerializedChallenges.this.challenges.currentChallengeInfo = this.challenges.currentChallengeInfo;
            CharacterSerializedChallenges.this.challenges.pastScenarii.clear();
            CharacterSerializedChallenges.this.challenges.pastScenarii.ensureCapacity(this.challenges.pastScenarii.size());
            CharacterSerializedChallenges.this.challenges.pastScenarii.addAll(this.challenges.pastScenarii);
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
