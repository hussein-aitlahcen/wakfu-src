package com.ankamagames.baseImpl.common.clientAndServer.rawData;

import java.nio.*;
import java.util.*;

public class RawChallengeInfo implements VersionableObject
{
    public int scenarioId;
    public final ArrayList<ActiveGoal> activeGoals;
    public final ArrayList<ExecutedGoal> executedGoals;
    public final ArrayList<GlobalVar> globalVars;
    public final ArrayList<WatchedVar> watchedVars;
    public RemainingTime remainingTime;
    
    public RawChallengeInfo() {
        super();
        this.scenarioId = 0;
        this.activeGoals = new ArrayList<ActiveGoal>(0);
        this.executedGoals = new ArrayList<ExecutedGoal>(0);
        this.globalVars = new ArrayList<GlobalVar>(0);
        this.watchedVars = new ArrayList<WatchedVar>(0);
        this.remainingTime = null;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putInt(this.scenarioId);
        if (this.activeGoals.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.activeGoals.size());
        for (int i = 0; i < this.activeGoals.size(); ++i) {
            final ActiveGoal activeGoals_element = this.activeGoals.get(i);
            final boolean activeGoals_element_ok = activeGoals_element.serialize(buffer);
            if (!activeGoals_element_ok) {
                return false;
            }
        }
        if (this.executedGoals.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.executedGoals.size());
        for (int i = 0; i < this.executedGoals.size(); ++i) {
            final ExecutedGoal executedGoals_element = this.executedGoals.get(i);
            final boolean executedGoals_element_ok = executedGoals_element.serialize(buffer);
            if (!executedGoals_element_ok) {
                return false;
            }
        }
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
        if (this.watchedVars.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.watchedVars.size());
        for (int i = 0; i < this.watchedVars.size(); ++i) {
            final WatchedVar watchedVars_element = this.watchedVars.get(i);
            final boolean watchedVars_element_ok = watchedVars_element.serialize(buffer);
            if (!watchedVars_element_ok) {
                return false;
            }
        }
        if (this.remainingTime != null) {
            buffer.put((byte)1);
            final boolean remainingTime_ok = this.remainingTime.serialize(buffer);
            if (!remainingTime_ok) {
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
        final int activeGoals_size = buffer.getShort() & 0xFFFF;
        this.activeGoals.clear();
        this.activeGoals.ensureCapacity(activeGoals_size);
        for (int i = 0; i < activeGoals_size; ++i) {
            final ActiveGoal activeGoals_element = new ActiveGoal();
            final boolean activeGoals_element_ok = activeGoals_element.unserialize(buffer);
            if (!activeGoals_element_ok) {
                return false;
            }
            this.activeGoals.add(activeGoals_element);
        }
        final int executedGoals_size = buffer.getShort() & 0xFFFF;
        this.executedGoals.clear();
        this.executedGoals.ensureCapacity(executedGoals_size);
        for (int j = 0; j < executedGoals_size; ++j) {
            final ExecutedGoal executedGoals_element = new ExecutedGoal();
            final boolean executedGoals_element_ok = executedGoals_element.unserialize(buffer);
            if (!executedGoals_element_ok) {
                return false;
            }
            this.executedGoals.add(executedGoals_element);
        }
        final int globalVars_size = buffer.getShort() & 0xFFFF;
        this.globalVars.clear();
        this.globalVars.ensureCapacity(globalVars_size);
        for (int k = 0; k < globalVars_size; ++k) {
            final GlobalVar globalVars_element = new GlobalVar();
            final boolean globalVars_element_ok = globalVars_element.unserialize(buffer);
            if (!globalVars_element_ok) {
                return false;
            }
            this.globalVars.add(globalVars_element);
        }
        final int watchedVars_size = buffer.getShort() & 0xFFFF;
        this.watchedVars.clear();
        this.watchedVars.ensureCapacity(watchedVars_size);
        for (int l = 0; l < watchedVars_size; ++l) {
            final WatchedVar watchedVars_element = new WatchedVar();
            final boolean watchedVars_element_ok = watchedVars_element.unserialize(buffer);
            if (!watchedVars_element_ok) {
                return false;
            }
            this.watchedVars.add(watchedVars_element);
        }
        final boolean remainingTime_present = buffer.get() == 1;
        if (remainingTime_present) {
            this.remainingTime = new RemainingTime();
            final boolean remainingTime_ok = this.remainingTime.unserialize(buffer);
            if (!remainingTime_ok) {
                return false;
            }
        }
        else {
            this.remainingTime = null;
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.scenarioId = 0;
        this.activeGoals.clear();
        this.executedGoals.clear();
        this.globalVars.clear();
        this.watchedVars.clear();
        this.remainingTime = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 1) {
            return this.unserialize(buffer);
        }
        final RawChallengeInfoConverter converter = new RawChallengeInfoConverter();
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
        for (int i = 0; i < this.activeGoals.size(); ++i) {
            final ActiveGoal activeGoals_element = this.activeGoals.get(i);
            size += activeGoals_element.serializedSize();
        }
        size += 2;
        for (int i = 0; i < this.executedGoals.size(); ++i) {
            final ExecutedGoal executedGoals_element = this.executedGoals.get(i);
            size += executedGoals_element.serializedSize();
        }
        size += 2;
        for (int i = 0; i < this.globalVars.size(); ++i) {
            final GlobalVar globalVars_element = this.globalVars.get(i);
            size += globalVars_element.serializedSize();
        }
        size += 2;
        for (int i = 0; i < this.watchedVars.size(); ++i) {
            final WatchedVar watchedVars_element = this.watchedVars.get(i);
            size += watchedVars_element.serializedSize();
        }
        ++size;
        if (this.remainingTime != null) {
            size += this.remainingTime.serializedSize();
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
        repr.append(prefix).append("activeGoals=");
        if (this.activeGoals.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.activeGoals.size()).append(" elements)...\n");
            for (int i = 0; i < this.activeGoals.size(); ++i) {
                final ActiveGoal activeGoals_element = this.activeGoals.get(i);
                activeGoals_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("executedGoals=");
        if (this.executedGoals.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.executedGoals.size()).append(" elements)...\n");
            for (int i = 0; i < this.executedGoals.size(); ++i) {
                final ExecutedGoal executedGoals_element = this.executedGoals.get(i);
                executedGoals_element.internalToString(repr, prefix + i + "/ ");
            }
        }
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
        repr.append(prefix).append("watchedVars=");
        if (this.watchedVars.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.watchedVars.size()).append(" elements)...\n");
            for (int i = 0; i < this.watchedVars.size(); ++i) {
                final WatchedVar watchedVars_element = this.watchedVars.get(i);
                watchedVars_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("remainingTime=");
        if (this.remainingTime == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.remainingTime.internalToString(repr, prefix + "  ");
        }
    }
    
    public static class ActiveGoal implements VersionableObject
    {
        public int actionGroupId;
        public static final int SERIALIZED_SIZE = 4;
        
        public ActiveGoal() {
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
    
    public static class ExecutedGoal implements VersionableObject
    {
        public int actionGroupId;
        public static final int SERIALIZED_SIZE = 4;
        
        public ExecutedGoal() {
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
    
    public static class WatchedVar implements VersionableObject
    {
        public byte varId;
        public long value;
        public static final int SERIALIZED_SIZE = 9;
        
        public WatchedVar() {
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
    
    public static class RemainingTime implements VersionableObject
    {
        public long value;
        public static final int SERIALIZED_SIZE = 8;
        
        public RemainingTime() {
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
    
    private final class RawChallengeInfoConverter
    {
        private int scenarioId;
        private final ArrayList<ActiveGoal> activeGoals;
        private final ArrayList<ExecutedGoal> executedGoals;
        private final ArrayList<GlobalVar> globalVars;
        private final ArrayList<WatchedVar> watchedVars;
        private RemainingTime remainingTime;
        
        private RawChallengeInfoConverter() {
            super();
            this.scenarioId = 0;
            this.activeGoals = new ArrayList<ActiveGoal>(0);
            this.executedGoals = new ArrayList<ExecutedGoal>(0);
            this.globalVars = new ArrayList<GlobalVar>(0);
            this.watchedVars = new ArrayList<WatchedVar>(0);
            this.remainingTime = null;
        }
        
        public void pushResult() {
            RawChallengeInfo.this.scenarioId = this.scenarioId;
            RawChallengeInfo.this.activeGoals.clear();
            RawChallengeInfo.this.activeGoals.ensureCapacity(this.activeGoals.size());
            RawChallengeInfo.this.activeGoals.addAll(this.activeGoals);
            RawChallengeInfo.this.executedGoals.clear();
            RawChallengeInfo.this.executedGoals.ensureCapacity(this.executedGoals.size());
            RawChallengeInfo.this.executedGoals.addAll(this.executedGoals);
            RawChallengeInfo.this.globalVars.clear();
            RawChallengeInfo.this.globalVars.ensureCapacity(this.globalVars.size());
            RawChallengeInfo.this.globalVars.addAll(this.globalVars);
            RawChallengeInfo.this.watchedVars.clear();
            RawChallengeInfo.this.watchedVars.ensureCapacity(this.watchedVars.size());
            RawChallengeInfo.this.watchedVars.addAll(this.watchedVars);
            RawChallengeInfo.this.remainingTime = this.remainingTime;
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
