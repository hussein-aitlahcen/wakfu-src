package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.util.*;
import java.nio.*;

public class RawSpellLevel implements VersionableObject
{
    public byte type;
    public long uniqueId;
    public int spellId;
    public short level;
    public long xp;
    public final ArrayList<Skill> skills;
    
    public RawSpellLevel() {
        super();
        this.type = 0;
        this.uniqueId = 0L;
        this.spellId = 0;
        this.level = 0;
        this.xp = 0L;
        this.skills = new ArrayList<Skill>(0);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.put(this.type);
        buffer.putLong(this.uniqueId);
        buffer.putInt(this.spellId);
        buffer.putShort(this.level);
        buffer.putLong(this.xp);
        if (this.skills.size() > 255) {
            return false;
        }
        buffer.put((byte)this.skills.size());
        for (int i = 0; i < this.skills.size(); ++i) {
            final Skill skills_element = this.skills.get(i);
            final boolean skills_element_ok = skills_element.serialize(buffer);
            if (!skills_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.type = buffer.get();
        this.uniqueId = buffer.getLong();
        this.spellId = buffer.getInt();
        this.level = buffer.getShort();
        this.xp = buffer.getLong();
        final int skills_size = buffer.get() & 0xFF;
        this.skills.clear();
        this.skills.ensureCapacity(skills_size);
        for (int i = 0; i < skills_size; ++i) {
            final Skill skills_element = new Skill();
            final boolean skills_element_ok = skills_element.unserialize(buffer);
            if (!skills_element_ok) {
                return false;
            }
            this.skills.add(skills_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.type = 0;
        this.uniqueId = 0L;
        this.spellId = 0;
        this.level = 0;
        this.xp = 0L;
        this.skills.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        ++size;
        size += 8;
        size += 4;
        size += 2;
        size += 8;
        ++size;
        for (int i = 0; i < this.skills.size(); ++i) {
            final Skill skills_element = this.skills.get(i);
            size += skills_element.serializedSize();
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
        repr.append(prefix).append("type=").append(this.type).append('\n');
        repr.append(prefix).append("uniqueId=").append(this.uniqueId).append('\n');
        repr.append(prefix).append("spellId=").append(this.spellId).append('\n');
        repr.append(prefix).append("level=").append(this.level).append('\n');
        repr.append(prefix).append("xp=").append(this.xp).append('\n');
        repr.append(prefix).append("skills=");
        if (this.skills.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.skills.size()).append(" elements)...\n");
            for (int i = 0; i < this.skills.size(); ++i) {
                final Skill skills_element = this.skills.get(i);
                skills_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class Skill implements VersionableObject
    {
        public int skillId;
        public static final int SERIALIZED_SIZE = 4;
        
        public Skill() {
            super();
            this.skillId = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.skillId);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.skillId = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.skillId = 0;
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
            repr.append(prefix).append("skillId=").append(this.skillId).append('\n');
        }
    }
}
