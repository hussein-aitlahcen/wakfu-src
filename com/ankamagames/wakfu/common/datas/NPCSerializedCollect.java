package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public class NPCSerializedCollect extends CharacterSerializedPart implements VersionableObject
{
    public final ArrayList<CollectAction> unavailableActions;
    private final BinarSerialPart m_binarPart;
    
    public NPCSerializedCollect() {
        super();
        this.unavailableActions = new ArrayList<CollectAction>(0);
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = NPCSerializedCollect.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de NPCSerializedCollect");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de NPCSerializedCollect", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = NPCSerializedCollect.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de NPCSerializedCollect");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de NPCSerializedCollect", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return NPCSerializedCollect.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.unavailableActions.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.unavailableActions.size());
        for (int i = 0; i < this.unavailableActions.size(); ++i) {
            final CollectAction unavailableActions_element = this.unavailableActions.get(i);
            final boolean unavailableActions_element_ok = unavailableActions_element.serialize(buffer);
            if (!unavailableActions_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int unavailableActions_size = buffer.getShort() & 0xFFFF;
        this.unavailableActions.clear();
        this.unavailableActions.ensureCapacity(unavailableActions_size);
        for (int i = 0; i < unavailableActions_size; ++i) {
            final CollectAction unavailableActions_element = new CollectAction();
            final boolean unavailableActions_element_ok = unavailableActions_element.unserialize(buffer);
            if (!unavailableActions_element_ok) {
                return false;
            }
            this.unavailableActions.add(unavailableActions_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.unavailableActions.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 2;
        for (int i = 0; i < this.unavailableActions.size(); ++i) {
            final CollectAction unavailableActions_element = this.unavailableActions.get(i);
            size += unavailableActions_element.serializedSize();
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
        repr.append(prefix).append("unavailableActions=");
        if (this.unavailableActions.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.unavailableActions.size()).append(" elements)...\n");
            for (int i = 0; i < this.unavailableActions.size(); ++i) {
                final CollectAction unavailableActions_element = this.unavailableActions.get(i);
                unavailableActions_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class CollectAction implements VersionableObject
    {
        public int collectId;
        public static final int SERIALIZED_SIZE = 4;
        
        public CollectAction() {
            super();
            this.collectId = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.collectId);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.collectId = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.collectId = 0;
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
            repr.append(prefix).append("collectId=").append(this.collectId).append('\n');
        }
    }
}
