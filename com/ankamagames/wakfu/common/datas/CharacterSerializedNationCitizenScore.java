package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import java.util.*;

public class CharacterSerializedNationCitizenScore extends CharacterSerializedPart implements VersionableObject
{
    public final ArrayList<NationCitizenScoreInfo> nationCitizenScores;
    public final ArrayList<OffendedNations> offendedNations;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedNationCitizenScore() {
        super();
        this.nationCitizenScores = new ArrayList<NationCitizenScoreInfo>(0);
        this.offendedNations = new ArrayList<OffendedNations>(0);
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedNationCitizenScore.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedNationCitizenScore");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedNationCitizenScore", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedNationCitizenScore.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedNationCitizenScore");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedNationCitizenScore", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedNationCitizenScore.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.nationCitizenScores.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.nationCitizenScores.size());
        for (int i = 0; i < this.nationCitizenScores.size(); ++i) {
            final NationCitizenScoreInfo nationCitizenScores_element = this.nationCitizenScores.get(i);
            final boolean nationCitizenScores_element_ok = nationCitizenScores_element.serialize(buffer);
            if (!nationCitizenScores_element_ok) {
                return false;
            }
        }
        if (this.offendedNations.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.offendedNations.size());
        for (int i = 0; i < this.offendedNations.size(); ++i) {
            final OffendedNations offendedNations_element = this.offendedNations.get(i);
            final boolean offendedNations_element_ok = offendedNations_element.serialize(buffer);
            if (!offendedNations_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int nationCitizenScores_size = buffer.getShort() & 0xFFFF;
        this.nationCitizenScores.clear();
        this.nationCitizenScores.ensureCapacity(nationCitizenScores_size);
        for (int i = 0; i < nationCitizenScores_size; ++i) {
            final NationCitizenScoreInfo nationCitizenScores_element = new NationCitizenScoreInfo();
            final boolean nationCitizenScores_element_ok = nationCitizenScores_element.unserialize(buffer);
            if (!nationCitizenScores_element_ok) {
                return false;
            }
            this.nationCitizenScores.add(nationCitizenScores_element);
        }
        final int offendedNations_size = buffer.getShort() & 0xFFFF;
        this.offendedNations.clear();
        this.offendedNations.ensureCapacity(offendedNations_size);
        for (int j = 0; j < offendedNations_size; ++j) {
            final OffendedNations offendedNations_element = new OffendedNations();
            final boolean offendedNations_element_ok = offendedNations_element.unserialize(buffer);
            if (!offendedNations_element_ok) {
                return false;
            }
            this.offendedNations.add(offendedNations_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.nationCitizenScores.clear();
        this.offendedNations.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 222) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedNationCitizenScoreConverter converter = new CharacterSerializedNationCitizenScoreConverter();
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
        for (int i = 0; i < this.nationCitizenScores.size(); ++i) {
            final NationCitizenScoreInfo nationCitizenScores_element = this.nationCitizenScores.get(i);
            size += nationCitizenScores_element.serializedSize();
        }
        size += 2;
        for (int i = 0; i < this.offendedNations.size(); ++i) {
            final OffendedNations offendedNations_element = this.offendedNations.get(i);
            size += offendedNations_element.serializedSize();
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
        repr.append(prefix).append("nationCitizenScores=");
        if (this.nationCitizenScores.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.nationCitizenScores.size()).append(" elements)...\n");
            for (int i = 0; i < this.nationCitizenScores.size(); ++i) {
                final NationCitizenScoreInfo nationCitizenScores_element = this.nationCitizenScores.get(i);
                nationCitizenScores_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("offendedNations=");
        if (this.offendedNations.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.offendedNations.size()).append(" elements)...\n");
            for (int i = 0; i < this.offendedNations.size(); ++i) {
                final OffendedNations offendedNations_element = this.offendedNations.get(i);
                offendedNations_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class NationCitizenScoreInfo implements VersionableObject
    {
        public int nationId;
        public int citizenScore;
        public static final int SERIALIZED_SIZE = 8;
        
        public NationCitizenScoreInfo() {
            super();
            this.nationId = -1;
            this.citizenScore = -1;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.nationId);
            buffer.putInt(this.citizenScore);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.nationId = buffer.getInt();
            this.citizenScore = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.nationId = -1;
            this.citizenScore = -1;
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
            repr.append(prefix).append("nationId=").append(this.nationId).append('\n');
            repr.append(prefix).append("citizenScore=").append(this.citizenScore).append('\n');
        }
    }
    
    public static class OffendedNations implements VersionableObject
    {
        public int offendedNationId;
        public static final int SERIALIZED_SIZE = 4;
        
        public OffendedNations() {
            super();
            this.offendedNationId = -1;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.offendedNationId);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.offendedNationId = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.offendedNationId = -1;
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
            repr.append(prefix).append("offendedNationId=").append(this.offendedNationId).append('\n');
        }
    }
    
    private final class CharacterSerializedNationCitizenScoreConverter
    {
        private final ArrayList<NationCitizenScoreInfo> nationCitizenScores;
        private final ArrayList<OffendedNations> offendedNations;
        
        private CharacterSerializedNationCitizenScoreConverter() {
            super();
            this.nationCitizenScores = new ArrayList<NationCitizenScoreInfo>(0);
            this.offendedNations = new ArrayList<OffendedNations>(0);
        }
        
        public void pushResult() {
            CharacterSerializedNationCitizenScore.this.nationCitizenScores.clear();
            CharacterSerializedNationCitizenScore.this.nationCitizenScores.ensureCapacity(this.nationCitizenScores.size());
            CharacterSerializedNationCitizenScore.this.nationCitizenScores.addAll(this.nationCitizenScores);
            CharacterSerializedNationCitizenScore.this.offendedNations.clear();
            CharacterSerializedNationCitizenScore.this.offendedNations.ensureCapacity(this.offendedNations.size());
            CharacterSerializedNationCitizenScore.this.offendedNations.addAll(this.offendedNations);
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            final int nationCitizenScores_size = buffer.getShort() & 0xFFFF;
            this.nationCitizenScores.clear();
            this.nationCitizenScores.ensureCapacity(nationCitizenScores_size);
            for (int i = 0; i < nationCitizenScores_size; ++i) {
                final NationCitizenScoreInfo nationCitizenScores_element = new NationCitizenScoreInfo();
                final boolean nationCitizenScores_element_ok = nationCitizenScores_element.unserializeVersion(buffer, 1);
                if (!nationCitizenScores_element_ok) {
                    return false;
                }
                this.nationCitizenScores.add(nationCitizenScores_element);
            }
            return true;
        }
        
        public void convert_v1_to_v222() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 1) {
                return false;
            }
            if (version >= 222) {
                return false;
            }
            final boolean ok = this.unserialize_v1(buffer);
            if (ok) {
                this.convert_v1_to_v222();
                return true;
            }
            return false;
        }
    }
}
