package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public class NPCSerializedGroup extends CharacterSerializedPart implements VersionableObject
{
    public long groupId;
    public final ArrayList<Member> members;
    private final BinarSerialPart m_binarPart;
    
    public NPCSerializedGroup() {
        super();
        this.groupId = 0L;
        this.members = new ArrayList<Member>(0);
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = NPCSerializedGroup.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de NPCSerializedGroup");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de NPCSerializedGroup", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = NPCSerializedGroup.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de NPCSerializedGroup");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de NPCSerializedGroup", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return NPCSerializedGroup.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putLong(this.groupId);
        if (this.members.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.members.size());
        for (int i = 0; i < this.members.size(); ++i) {
            final Member members_element = this.members.get(i);
            final boolean members_element_ok = members_element.serialize(buffer);
            if (!members_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.groupId = buffer.getLong();
        final int members_size = buffer.getShort() & 0xFFFF;
        this.members.clear();
        this.members.ensureCapacity(members_size);
        for (int i = 0; i < members_size; ++i) {
            final Member members_element = new Member();
            final boolean members_element_ok = members_element.unserialize(buffer);
            if (!members_element_ok) {
                return false;
            }
            this.members.add(members_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.groupId = 0L;
        this.members.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 8;
        size += 2;
        for (int i = 0; i < this.members.size(); ++i) {
            final Member members_element = this.members.get(i);
            size += members_element.serializedSize();
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
        repr.append(prefix).append("groupId=").append(this.groupId).append('\n');
        repr.append(prefix).append("members=");
        if (this.members.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.members.size()).append(" elements)...\n");
            for (int i = 0; i < this.members.size(); ++i) {
                final Member members_element = this.members.get(i);
                members_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class Member implements VersionableObject
    {
        public short breedId;
        public short level;
        public static final int SERIALIZED_SIZE = 4;
        
        public Member() {
            super();
            this.breedId = 0;
            this.level = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putShort(this.breedId);
            buffer.putShort(this.level);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.breedId = buffer.getShort();
            this.level = buffer.getShort();
            return true;
        }
        
        @Override
        public void clear() {
            this.breedId = 0;
            this.level = 0;
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
            repr.append(prefix).append("breedId=").append(this.breedId).append('\n');
            repr.append(prefix).append("level=").append(this.level).append('\n');
        }
    }
}
