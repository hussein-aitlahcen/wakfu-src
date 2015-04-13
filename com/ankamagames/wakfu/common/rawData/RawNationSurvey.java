package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.util.*;
import java.nio.*;

public class RawNationSurvey implements VersionableObject
{
    public final ArrayList<Opinion> governmentOpinions;
    
    public RawNationSurvey() {
        super();
        this.governmentOpinions = new ArrayList<Opinion>(0);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.governmentOpinions.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.governmentOpinions.size());
        for (int i = 0; i < this.governmentOpinions.size(); ++i) {
            final Opinion governmentOpinions_element = this.governmentOpinions.get(i);
            final boolean governmentOpinions_element_ok = governmentOpinions_element.serialize(buffer);
            if (!governmentOpinions_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int governmentOpinions_size = buffer.getShort() & 0xFFFF;
        this.governmentOpinions.clear();
        this.governmentOpinions.ensureCapacity(governmentOpinions_size);
        for (int i = 0; i < governmentOpinions_size; ++i) {
            final Opinion governmentOpinions_element = new Opinion();
            final boolean governmentOpinions_element_ok = governmentOpinions_element.unserialize(buffer);
            if (!governmentOpinions_element_ok) {
                return false;
            }
            this.governmentOpinions.add(governmentOpinions_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.governmentOpinions.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 2;
        for (int i = 0; i < this.governmentOpinions.size(); ++i) {
            final Opinion governmentOpinions_element = this.governmentOpinions.get(i);
            size += governmentOpinions_element.serializedSize();
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
        repr.append(prefix).append("governmentOpinions=");
        if (this.governmentOpinions.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.governmentOpinions.size()).append(" elements)...\n");
            for (int i = 0; i < this.governmentOpinions.size(); ++i) {
                final Opinion governmentOpinions_element = this.governmentOpinions.get(i);
                governmentOpinions_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class Opinion implements VersionableObject
    {
        public byte opinionId;
        public int nbBallots;
        public static final int SERIALIZED_SIZE = 5;
        
        public Opinion() {
            super();
            this.opinionId = 0;
            this.nbBallots = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.put(this.opinionId);
            buffer.putInt(this.nbBallots);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.opinionId = buffer.get();
            this.nbBallots = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.opinionId = 0;
            this.nbBallots = 0;
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
            repr.append(prefix).append("opinionId=").append(this.opinionId).append('\n');
            repr.append(prefix).append("nbBallots=").append(this.nbBallots).append('\n');
        }
    }
}
