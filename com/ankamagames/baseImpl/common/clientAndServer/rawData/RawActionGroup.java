package com.ankamagames.baseImpl.common.clientAndServer.rawData;

import java.nio.*;
import java.util.*;

public class RawActionGroup implements VersionableObject
{
    public final ArrayList<ActionGroup> actions;
    
    public RawActionGroup() {
        super();
        this.actions = new ArrayList<ActionGroup>(0);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.actions.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.actions.size());
        for (int i = 0; i < this.actions.size(); ++i) {
            final ActionGroup actions_element = this.actions.get(i);
            final boolean actions_element_ok = actions_element.serialize(buffer);
            if (!actions_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int actions_size = buffer.getShort() & 0xFFFF;
        this.actions.clear();
        this.actions.ensureCapacity(actions_size);
        for (int i = 0; i < actions_size; ++i) {
            final ActionGroup actions_element = new ActionGroup();
            final boolean actions_element_ok = actions_element.unserialize(buffer);
            if (!actions_element_ok) {
                return false;
            }
            this.actions.add(actions_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.actions.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 1) {
            return this.unserialize(buffer);
        }
        final RawActionGroupConverter converter = new RawActionGroupConverter();
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
        for (int i = 0; i < this.actions.size(); ++i) {
            final ActionGroup actions_element = this.actions.get(i);
            size += actions_element.serializedSize();
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
        repr.append(prefix).append("actions=");
        if (this.actions.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.actions.size()).append(" elements)...\n");
            for (int i = 0; i < this.actions.size(); ++i) {
                final ActionGroup actions_element = this.actions.get(i);
                actions_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class ActionGroup implements VersionableObject
    {
        public int actionUid;
        public final RawAction action;
        
        public ActionGroup() {
            super();
            this.actionUid = 0;
            this.action = new RawAction();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.actionUid);
            final boolean action_ok = this.action.serialize(buffer);
            return action_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.actionUid = buffer.getInt();
            final boolean action_ok = this.action.unserialize(buffer);
            return action_ok;
        }
        
        @Override
        public void clear() {
            this.actionUid = 0;
            this.action.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 1) {
                return this.unserialize(buffer);
            }
            final ActionGroupConverter converter = new ActionGroupConverter();
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
            size += this.action.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("actionUid=").append(this.actionUid).append('\n');
            repr.append(prefix).append("action=...\n");
            this.action.internalToString(repr, prefix + "  ");
        }
        
        private final class ActionGroupConverter
        {
            private int actionUid;
            private final RawAction action;
            
            private ActionGroupConverter() {
                super();
                this.actionUid = 0;
                this.action = new RawAction();
            }
            
            public void pushResult() {
                ActionGroup.this.actionUid = this.actionUid;
                ActionGroup.this.action.spawnedCharacter = this.action.spawnedCharacter;
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
    
    private final class RawActionGroupConverter
    {
        private final ArrayList<ActionGroup> actions;
        
        private RawActionGroupConverter() {
            super();
            this.actions = new ArrayList<ActionGroup>(0);
        }
        
        public void pushResult() {
            RawActionGroup.this.actions.clear();
            RawActionGroup.this.actions.ensureCapacity(this.actions.size());
            RawActionGroup.this.actions.addAll(this.actions);
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
