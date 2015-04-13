package com.ankamagames.baseImpl.common.clientAndServer.rawData;

import java.nio.*;
import java.util.*;

public class RawScenario implements VersionableObject
{
    public int scenarioId;
    public final ArrayList<GlobalVar> globalVars;
    public final ArrayList<VarList> currentVarsForAddedUsers;
    public final ArrayList<ActiveActionGroup> activeActionGroups;
    public final ArrayList<ExecutedActionGroup> executedActionGroups;
    public StartTime startTime;
    
    public RawScenario() {
        super();
        this.scenarioId = 0;
        this.globalVars = new ArrayList<GlobalVar>(0);
        this.currentVarsForAddedUsers = new ArrayList<VarList>(0);
        this.activeActionGroups = new ArrayList<ActiveActionGroup>(0);
        this.executedActionGroups = new ArrayList<ExecutedActionGroup>(0);
        this.startTime = null;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putInt(this.scenarioId);
        if (this.globalVars.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.globalVars.size());
        for (int i = 0; i < this.globalVars.size(); ++i) {
            final GlobalVar globalVars_element = this.globalVars.get(i);
            final boolean globalVars_element_ok = globalVars_element.serialize(buffer);
            if (!globalVars_element_ok) {
                return false;
            }
        }
        if (this.currentVarsForAddedUsers.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.currentVarsForAddedUsers.size());
        for (int i = 0; i < this.currentVarsForAddedUsers.size(); ++i) {
            final VarList currentVarsForAddedUsers_element = this.currentVarsForAddedUsers.get(i);
            final boolean currentVarsForAddedUsers_element_ok = currentVarsForAddedUsers_element.serialize(buffer);
            if (!currentVarsForAddedUsers_element_ok) {
                return false;
            }
        }
        if (this.activeActionGroups.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.activeActionGroups.size());
        for (int i = 0; i < this.activeActionGroups.size(); ++i) {
            final ActiveActionGroup activeActionGroups_element = this.activeActionGroups.get(i);
            final boolean activeActionGroups_element_ok = activeActionGroups_element.serialize(buffer);
            if (!activeActionGroups_element_ok) {
                return false;
            }
        }
        if (this.executedActionGroups.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.executedActionGroups.size());
        for (int i = 0; i < this.executedActionGroups.size(); ++i) {
            final ExecutedActionGroup executedActionGroups_element = this.executedActionGroups.get(i);
            final boolean executedActionGroups_element_ok = executedActionGroups_element.serialize(buffer);
            if (!executedActionGroups_element_ok) {
                return false;
            }
        }
        if (this.startTime != null) {
            buffer.put((byte)1);
            final boolean startTime_ok = this.startTime.serialize(buffer);
            if (!startTime_ok) {
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
        this.scenarioId = buffer.getInt();
        final int globalVars_size = buffer.getShort() & 0xFFFF;
        this.globalVars.clear();
        this.globalVars.ensureCapacity(globalVars_size);
        for (int i = 0; i < globalVars_size; ++i) {
            final GlobalVar globalVars_element = new GlobalVar();
            final boolean globalVars_element_ok = globalVars_element.unserialize(buffer);
            if (!globalVars_element_ok) {
                return false;
            }
            this.globalVars.add(globalVars_element);
        }
        final int currentVarsForAddedUsers_size = buffer.getShort() & 0xFFFF;
        this.currentVarsForAddedUsers.clear();
        this.currentVarsForAddedUsers.ensureCapacity(currentVarsForAddedUsers_size);
        for (int j = 0; j < currentVarsForAddedUsers_size; ++j) {
            final VarList currentVarsForAddedUsers_element = new VarList();
            final boolean currentVarsForAddedUsers_element_ok = currentVarsForAddedUsers_element.unserialize(buffer);
            if (!currentVarsForAddedUsers_element_ok) {
                return false;
            }
            this.currentVarsForAddedUsers.add(currentVarsForAddedUsers_element);
        }
        final int activeActionGroups_size = buffer.getShort() & 0xFFFF;
        this.activeActionGroups.clear();
        this.activeActionGroups.ensureCapacity(activeActionGroups_size);
        for (int k = 0; k < activeActionGroups_size; ++k) {
            final ActiveActionGroup activeActionGroups_element = new ActiveActionGroup();
            final boolean activeActionGroups_element_ok = activeActionGroups_element.unserialize(buffer);
            if (!activeActionGroups_element_ok) {
                return false;
            }
            this.activeActionGroups.add(activeActionGroups_element);
        }
        final int executedActionGroups_size = buffer.getShort() & 0xFFFF;
        this.executedActionGroups.clear();
        this.executedActionGroups.ensureCapacity(executedActionGroups_size);
        for (int l = 0; l < executedActionGroups_size; ++l) {
            final ExecutedActionGroup executedActionGroups_element = new ExecutedActionGroup();
            final boolean executedActionGroups_element_ok = executedActionGroups_element.unserialize(buffer);
            if (!executedActionGroups_element_ok) {
                return false;
            }
            this.executedActionGroups.add(executedActionGroups_element);
        }
        final boolean startTime_present = buffer.get() == 1;
        if (startTime_present) {
            this.startTime = new StartTime();
            final boolean startTime_ok = this.startTime.unserialize(buffer);
            if (!startTime_ok) {
                return false;
            }
        }
        else {
            this.startTime = null;
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.scenarioId = 0;
        this.globalVars.clear();
        this.currentVarsForAddedUsers.clear();
        this.activeActionGroups.clear();
        this.executedActionGroups.clear();
        this.startTime = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 1) {
            return this.unserialize(buffer);
        }
        final RawScenarioConverter converter = new RawScenarioConverter();
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
        size += 2;
        for (int i = 0; i < this.globalVars.size(); ++i) {
            final GlobalVar globalVars_element = this.globalVars.get(i);
            size += globalVars_element.serializedSize();
        }
        size += 2;
        for (int i = 0; i < this.currentVarsForAddedUsers.size(); ++i) {
            final VarList currentVarsForAddedUsers_element = this.currentVarsForAddedUsers.get(i);
            size += currentVarsForAddedUsers_element.serializedSize();
        }
        size += 2;
        for (int i = 0; i < this.activeActionGroups.size(); ++i) {
            final ActiveActionGroup activeActionGroups_element = this.activeActionGroups.get(i);
            size += activeActionGroups_element.serializedSize();
        }
        size += 2;
        for (int i = 0; i < this.executedActionGroups.size(); ++i) {
            final ExecutedActionGroup executedActionGroups_element = this.executedActionGroups.get(i);
            size += executedActionGroups_element.serializedSize();
        }
        ++size;
        if (this.startTime != null) {
            size += this.startTime.serializedSize();
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
        repr.append(prefix).append("scenarioId=").append(this.scenarioId).append('\n');
        repr.append(prefix).append("globalVars=");
        if (this.globalVars.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.globalVars.size()).append(" elements)...\n");
            for (int i = 0; i < this.globalVars.size(); ++i) {
                final GlobalVar globalVars_element = this.globalVars.get(i);
                globalVars_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("currentVarsForAddedUsers=");
        if (this.currentVarsForAddedUsers.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.currentVarsForAddedUsers.size()).append(" elements)...\n");
            for (int i = 0; i < this.currentVarsForAddedUsers.size(); ++i) {
                final VarList currentVarsForAddedUsers_element = this.currentVarsForAddedUsers.get(i);
                currentVarsForAddedUsers_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("activeActionGroups=");
        if (this.activeActionGroups.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.activeActionGroups.size()).append(" elements)...\n");
            for (int i = 0; i < this.activeActionGroups.size(); ++i) {
                final ActiveActionGroup activeActionGroups_element = this.activeActionGroups.get(i);
                activeActionGroups_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("executedActionGroups=");
        if (this.executedActionGroups.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.executedActionGroups.size()).append(" elements)...\n");
            for (int i = 0; i < this.executedActionGroups.size(); ++i) {
                final ExecutedActionGroup executedActionGroups_element = this.executedActionGroups.get(i);
                executedActionGroups_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("startTime=");
        if (this.startTime == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.startTime.internalToString(repr, prefix + "  ");
        }
    }
    
    public static class GlobalVar implements VersionableObject
    {
        public byte varId;
        public long value;
        public static final int SERIALIZED_SIZE = 9;
        
        public GlobalVar() {
            super();
            this.varId = 0;
            this.value = 0L;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.put(this.varId);
            buffer.putLong(this.value);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.varId = buffer.get();
            this.value = buffer.getLong();
            return true;
        }
        
        @Override
        public void clear() {
            this.varId = 0;
            this.value = 0L;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 9;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("varId=").append(this.varId).append('\n');
            repr.append(prefix).append("value=").append(this.value).append('\n');
        }
    }
    
    public static class VarList implements VersionableObject
    {
        public long userId;
        public final ArrayList<UserVar> vars;
        
        public VarList() {
            super();
            this.userId = 0L;
            this.vars = new ArrayList<UserVar>(0);
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putLong(this.userId);
            if (this.vars.size() > 65535) {
                return false;
            }
            buffer.putShort((short)this.vars.size());
            for (int i = 0; i < this.vars.size(); ++i) {
                final UserVar vars_element = this.vars.get(i);
                final boolean vars_element_ok = vars_element.serialize(buffer);
                if (!vars_element_ok) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.userId = buffer.getLong();
            final int vars_size = buffer.getShort() & 0xFFFF;
            this.vars.clear();
            this.vars.ensureCapacity(vars_size);
            for (int i = 0; i < vars_size; ++i) {
                final UserVar vars_element = new UserVar();
                final boolean vars_element_ok = vars_element.unserialize(buffer);
                if (!vars_element_ok) {
                    return false;
                }
                this.vars.add(vars_element);
            }
            return true;
        }
        
        @Override
        public void clear() {
            this.userId = 0L;
            this.vars.clear();
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
            for (int i = 0; i < this.vars.size(); ++i) {
                final UserVar vars_element = this.vars.get(i);
                size += vars_element.serializedSize();
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
            repr.append(prefix).append("userId=").append(this.userId).append('\n');
            repr.append(prefix).append("vars=");
            if (this.vars.isEmpty()) {
                repr.append("{}").append('\n');
            }
            else {
                repr.append("(").append(this.vars.size()).append(" elements)...\n");
                for (int i = 0; i < this.vars.size(); ++i) {
                    final UserVar vars_element = this.vars.get(i);
                    vars_element.internalToString(repr, prefix + i + "/ ");
                }
            }
        }
        
        public static class UserVar implements VersionableObject
        {
            public byte varId;
            public long value;
            public static final int SERIALIZED_SIZE = 9;
            
            public UserVar() {
                super();
                this.varId = 0;
                this.value = 0L;
            }
            
            @Override
            public boolean serialize(final ByteBuffer buffer) {
                buffer.put(this.varId);
                buffer.putLong(this.value);
                return true;
            }
            
            @Override
            public boolean unserialize(final ByteBuffer buffer) {
                this.varId = buffer.get();
                this.value = buffer.getLong();
                return true;
            }
            
            @Override
            public void clear() {
                this.varId = 0;
                this.value = 0L;
            }
            
            @Override
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                return this.unserialize(buffer);
            }
            
            @Override
            public int serializedSize() {
                return 9;
            }
            
            @Override
            public final String toString() {
                final StringBuilder repr = new StringBuilder();
                this.internalToString(repr, "");
                return repr.toString();
            }
            
            public final void internalToString(final StringBuilder repr, final String prefix) {
                repr.append(prefix).append("varId=").append(this.varId).append('\n');
                repr.append(prefix).append("value=").append(this.value).append('\n');
            }
        }
    }
    
    public static class ActiveActionGroup implements VersionableObject
    {
        public int actionGroupId;
        public static final int SERIALIZED_SIZE = 4;
        
        public ActiveActionGroup() {
            super();
            this.actionGroupId = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.actionGroupId);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.actionGroupId = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.actionGroupId = 0;
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
            repr.append(prefix).append("actionGroupId=").append(this.actionGroupId).append('\n');
        }
    }
    
    public static class ExecutedActionGroup implements VersionableObject
    {
        public int actionGroupId;
        public final RawActionGroup actionGroup;
        
        public ExecutedActionGroup() {
            super();
            this.actionGroupId = 0;
            this.actionGroup = new RawActionGroup();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.actionGroupId);
            final boolean actionGroup_ok = this.actionGroup.serialize(buffer);
            return actionGroup_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.actionGroupId = buffer.getInt();
            final boolean actionGroup_ok = this.actionGroup.unserialize(buffer);
            return actionGroup_ok;
        }
        
        @Override
        public void clear() {
            this.actionGroupId = 0;
            this.actionGroup.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 1) {
                return this.unserialize(buffer);
            }
            final ExecutedActionGroupConverter converter = new ExecutedActionGroupConverter();
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
            size += this.actionGroup.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("actionGroupId=").append(this.actionGroupId).append('\n');
            repr.append(prefix).append("actionGroup=...\n");
            this.actionGroup.internalToString(repr, prefix + "  ");
        }
        
        private final class ExecutedActionGroupConverter
        {
            private int actionGroupId;
            private final RawActionGroup actionGroup;
            
            private ExecutedActionGroupConverter() {
                super();
                this.actionGroupId = 0;
                this.actionGroup = new RawActionGroup();
            }
            
            public void pushResult() {
                ExecutedActionGroup.this.actionGroupId = this.actionGroupId;
                ExecutedActionGroup.this.actionGroup.actions.clear();
                ExecutedActionGroup.this.actionGroup.actions.ensureCapacity(this.actionGroup.actions.size());
                ExecutedActionGroup.this.actionGroup.actions.addAll(this.actionGroup.actions);
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
    
    public static class StartTime implements VersionableObject
    {
        public long value;
        public static final int SERIALIZED_SIZE = 8;
        
        public StartTime() {
            super();
            this.value = 0L;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putLong(this.value);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.value = buffer.getLong();
            return true;
        }
        
        @Override
        public void clear() {
            this.value = 0L;
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
            repr.append(prefix).append("value=").append(this.value).append('\n');
        }
    }
    
    private final class RawScenarioConverter
    {
        private int scenarioId;
        private final ArrayList<GlobalVar> globalVars;
        private final ArrayList<VarList> currentVarsForAddedUsers;
        private final ArrayList<ActiveActionGroup> activeActionGroups;
        private final ArrayList<ExecutedActionGroup> executedActionGroups;
        private StartTime startTime;
        
        private RawScenarioConverter() {
            super();
            this.scenarioId = 0;
            this.globalVars = new ArrayList<GlobalVar>(0);
            this.currentVarsForAddedUsers = new ArrayList<VarList>(0);
            this.activeActionGroups = new ArrayList<ActiveActionGroup>(0);
            this.executedActionGroups = new ArrayList<ExecutedActionGroup>(0);
            this.startTime = null;
        }
        
        public void pushResult() {
            RawScenario.this.scenarioId = this.scenarioId;
            RawScenario.this.globalVars.clear();
            RawScenario.this.globalVars.ensureCapacity(this.globalVars.size());
            RawScenario.this.globalVars.addAll(this.globalVars);
            RawScenario.this.currentVarsForAddedUsers.clear();
            RawScenario.this.currentVarsForAddedUsers.ensureCapacity(this.currentVarsForAddedUsers.size());
            RawScenario.this.currentVarsForAddedUsers.addAll(this.currentVarsForAddedUsers);
            RawScenario.this.activeActionGroups.clear();
            RawScenario.this.activeActionGroups.ensureCapacity(this.activeActionGroups.size());
            RawScenario.this.activeActionGroups.addAll(this.activeActionGroups);
            RawScenario.this.executedActionGroups.clear();
            RawScenario.this.executedActionGroups.ensureCapacity(this.executedActionGroups.size());
            RawScenario.this.executedActionGroups.addAll(this.executedActionGroups);
            RawScenario.this.startTime = this.startTime;
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
