package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class RawKrosmozFigure implements VersionableObject
{
    public String guid;
    public int character;
    public int pedestal;
    public long acquiredOn;
    public String note;
    public boolean bound;
    
    public RawKrosmozFigure() {
        super();
        this.guid = null;
        this.character = 0;
        this.pedestal = 0;
        this.acquiredOn = 0L;
        this.note = null;
        this.bound = false;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.guid != null) {
            final byte[] serialized_guid = StringUtils.toUTF8(this.guid);
            if (serialized_guid.length > 65535) {
                return false;
            }
            buffer.putShort((short)serialized_guid.length);
            buffer.put(serialized_guid);
        }
        else {
            buffer.putShort((short)0);
        }
        buffer.putInt(this.character);
        buffer.putInt(this.pedestal);
        buffer.putLong(this.acquiredOn);
        if (this.note != null) {
            final byte[] serialized_note = StringUtils.toUTF8(this.note);
            if (serialized_note.length > 65535) {
                return false;
            }
            buffer.putShort((short)serialized_note.length);
            buffer.put(serialized_note);
        }
        else {
            buffer.putShort((short)0);
        }
        buffer.put((byte)(this.bound ? 1 : 0));
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int guid_size = buffer.getShort() & 0xFFFF;
        final byte[] serialized_guid = new byte[guid_size];
        buffer.get(serialized_guid);
        this.guid = StringUtils.fromUTF8(serialized_guid);
        this.character = buffer.getInt();
        this.pedestal = buffer.getInt();
        this.acquiredOn = buffer.getLong();
        final int note_size = buffer.getShort() & 0xFFFF;
        final byte[] serialized_note = new byte[note_size];
        buffer.get(serialized_note);
        this.note = StringUtils.fromUTF8(serialized_note);
        this.bound = (buffer.get() == 1);
        return true;
    }
    
    @Override
    public void clear() {
        this.guid = null;
        this.character = 0;
        this.pedestal = 0;
        this.acquiredOn = 0L;
        this.note = null;
        this.bound = false;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 2;
        size += ((this.guid != null) ? StringUtils.toUTF8(this.guid).length : 0);
        size += 4;
        size += 4;
        size += 8;
        size += 2;
        size += ((this.note != null) ? StringUtils.toUTF8(this.note).length : 0);
        return ++size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("guid=").append(this.guid).append('\n');
        repr.append(prefix).append("character=").append(this.character).append('\n');
        repr.append(prefix).append("pedestal=").append(this.pedestal).append('\n');
        repr.append(prefix).append("acquiredOn=").append(this.acquiredOn).append('\n');
        repr.append(prefix).append("note=").append(this.note).append('\n');
        repr.append(prefix).append("bound=").append(this.bound).append('\n');
    }
}
