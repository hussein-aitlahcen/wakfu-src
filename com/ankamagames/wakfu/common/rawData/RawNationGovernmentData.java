package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class RawNationGovernmentData implements VersionableObject
{
    public String speech;
    public Governor governor;
    
    public RawNationGovernmentData() {
        super();
        this.speech = null;
        this.governor = null;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.speech != null) {
            final byte[] serialized_speech = StringUtils.toUTF8(this.speech);
            if (serialized_speech.length > 65535) {
                return false;
            }
            buffer.putShort((short)serialized_speech.length);
            buffer.put(serialized_speech);
        }
        else {
            buffer.putShort((short)0);
        }
        if (this.governor != null) {
            buffer.put((byte)1);
            final boolean governor_ok = this.governor.serialize(buffer);
            if (!governor_ok) {
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
        final int speech_size = buffer.getShort() & 0xFFFF;
        final byte[] serialized_speech = new byte[speech_size];
        buffer.get(serialized_speech);
        this.speech = StringUtils.fromUTF8(serialized_speech);
        final boolean governor_present = buffer.get() == 1;
        if (governor_present) {
            this.governor = new Governor();
            final boolean governor_ok = this.governor.unserialize(buffer);
            if (!governor_ok) {
                return false;
            }
        }
        else {
            this.governor = null;
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.speech = null;
        this.governor = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 2;
        size += ((this.speech != null) ? StringUtils.toUTF8(this.speech).length : 0);
        ++size;
        if (this.governor != null) {
            size += this.governor.serializedSize();
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
        repr.append(prefix).append("speech=").append(this.speech).append('\n');
        repr.append(prefix).append("governor=");
        if (this.governor == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.governor.internalToString(repr, prefix + "  ");
        }
    }
    
    public static class Governor implements VersionableObject
    {
        public short titleId;
        public int nbMandate;
        public static final int SERIALIZED_SIZE = 6;
        
        public Governor() {
            super();
            this.titleId = 0;
            this.nbMandate = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putShort(this.titleId);
            buffer.putInt(this.nbMandate);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.titleId = buffer.getShort();
            this.nbMandate = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.titleId = 0;
            this.nbMandate = 0;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 6;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("titleId=").append(this.titleId).append('\n');
            repr.append(prefix).append("nbMandate=").append(this.nbMandate).append('\n');
        }
    }
}
