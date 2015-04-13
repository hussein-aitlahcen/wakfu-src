package com.ankamagames.baseImpl.common.clientAndServer.rawData;

import java.nio.*;
import java.util.*;

public class RawScenarioManager implements VersionableObject
{
    public CurrentScenarii currentScenarii;
    public CurrentChallengeInfo currentChallengeInfo;
    public final ArrayList<PastScenario> pastScenarii;
    
    public RawScenarioManager() {
        super();
        this.currentScenarii = null;
        this.currentChallengeInfo = null;
        this.pastScenarii = new ArrayList<PastScenario>(0);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.currentScenarii != null) {
            buffer.put((byte)1);
            final boolean currentScenarii_ok = this.currentScenarii.serialize(buffer);
            if (!currentScenarii_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        if (this.currentChallengeInfo != null) {
            buffer.put((byte)1);
            final boolean currentChallengeInfo_ok = this.currentChallengeInfo.serialize(buffer);
            if (!currentChallengeInfo_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        if (this.pastScenarii.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.pastScenarii.size());
        for (int i = 0; i < this.pastScenarii.size(); ++i) {
            final PastScenario pastScenarii_element = this.pastScenarii.get(i);
            final boolean pastScenarii_element_ok = pastScenarii_element.serialize(buffer);
            if (!pastScenarii_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final boolean currentScenarii_present = buffer.get() == 1;
        if (currentScenarii_present) {
            this.currentScenarii = new CurrentScenarii();
            final boolean currentScenarii_ok = this.currentScenarii.unserialize(buffer);
            if (!currentScenarii_ok) {
                return false;
            }
        }
        else {
            this.currentScenarii = null;
        }
        final boolean currentChallengeInfo_present = buffer.get() == 1;
        if (currentChallengeInfo_present) {
            this.currentChallengeInfo = new CurrentChallengeInfo();
            final boolean currentChallengeInfo_ok = this.currentChallengeInfo.unserialize(buffer);
            if (!currentChallengeInfo_ok) {
                return false;
            }
        }
        else {
            this.currentChallengeInfo = null;
        }
        final int pastScenarii_size = buffer.getShort() & 0xFFFF;
        this.pastScenarii.clear();
        this.pastScenarii.ensureCapacity(pastScenarii_size);
        for (int i = 0; i < pastScenarii_size; ++i) {
            final PastScenario pastScenarii_element = new PastScenario();
            final boolean pastScenarii_element_ok = pastScenarii_element.unserialize(buffer);
            if (!pastScenarii_element_ok) {
                return false;
            }
            this.pastScenarii.add(pastScenarii_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.currentScenarii = null;
        this.currentChallengeInfo = null;
        this.pastScenarii.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 1) {
            return this.unserialize(buffer);
        }
        final RawScenarioManagerConverter converter = new RawScenarioManagerConverter();
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
        ++size;
        if (this.currentScenarii != null) {
            size += this.currentScenarii.serializedSize();
        }
        ++size;
        if (this.currentChallengeInfo != null) {
            size += this.currentChallengeInfo.serializedSize();
        }
        size += 2;
        for (int i = 0; i < this.pastScenarii.size(); ++i) {
            final PastScenario pastScenarii_element = this.pastScenarii.get(i);
            size += pastScenarii_element.serializedSize();
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
        repr.append(prefix).append("currentScenarii=");
        if (this.currentScenarii == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.currentScenarii.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("currentChallengeInfo=");
        if (this.currentChallengeInfo == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.currentChallengeInfo.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("pastScenarii=");
        if (this.pastScenarii.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.pastScenarii.size()).append(" elements)...\n");
            for (int i = 0; i < this.pastScenarii.size(); ++i) {
                final PastScenario pastScenarii_element = this.pastScenarii.get(i);
                pastScenarii_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class CurrentScenarii implements VersionableObject
    {
        public final ArrayList<CurrentScenario> scenarii;
        
        public CurrentScenarii() {
            super();
            this.scenarii = new ArrayList<CurrentScenario>(0);
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            if (this.scenarii.size() > 65535) {
                return false;
            }
            buffer.putShort((short)this.scenarii.size());
            for (int i = 0; i < this.scenarii.size(); ++i) {
                final CurrentScenario scenarii_element = this.scenarii.get(i);
                final boolean scenarii_element_ok = scenarii_element.serialize(buffer);
                if (!scenarii_element_ok) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final int scenarii_size = buffer.getShort() & 0xFFFF;
            this.scenarii.clear();
            this.scenarii.ensureCapacity(scenarii_size);
            for (int i = 0; i < scenarii_size; ++i) {
                final CurrentScenario scenarii_element = new CurrentScenario();
                final boolean scenarii_element_ok = scenarii_element.unserialize(buffer);
                if (!scenarii_element_ok) {
                    return false;
                }
                this.scenarii.add(scenarii_element);
            }
            return true;
        }
        
        @Override
        public void clear() {
            this.scenarii.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 1) {
                return this.unserialize(buffer);
            }
            final CurrentScenariiConverter converter = new CurrentScenariiConverter();
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
            for (int i = 0; i < this.scenarii.size(); ++i) {
                final CurrentScenario scenarii_element = this.scenarii.get(i);
                size += scenarii_element.serializedSize();
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
            repr.append(prefix).append("scenarii=");
            if (this.scenarii.isEmpty()) {
                repr.append("{}").append('\n');
            }
            else {
                repr.append("(").append(this.scenarii.size()).append(" elements)...\n");
                for (int i = 0; i < this.scenarii.size(); ++i) {
                    final CurrentScenario scenarii_element = this.scenarii.get(i);
                    scenarii_element.internalToString(repr, prefix + i + "/ ");
                }
            }
        }
        
        public static class CurrentScenario implements VersionableObject
        {
            public final RawScenario scenario;
            
            public CurrentScenario() {
                super();
                this.scenario = new RawScenario();
            }
            
            @Override
            public boolean serialize(final ByteBuffer buffer) {
                final boolean scenario_ok = this.scenario.serialize(buffer);
                return scenario_ok;
            }
            
            @Override
            public boolean unserialize(final ByteBuffer buffer) {
                final boolean scenario_ok = this.scenario.unserialize(buffer);
                return scenario_ok;
            }
            
            @Override
            public void clear() {
                this.scenario.clear();
            }
            
            @Override
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version >= 1) {
                    return this.unserialize(buffer);
                }
                final CurrentScenarioConverter converter = new CurrentScenarioConverter();
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
                size += this.scenario.serializedSize();
                return size;
            }
            
            @Override
            public final String toString() {
                final StringBuilder repr = new StringBuilder();
                this.internalToString(repr, "");
                return repr.toString();
            }
            
            public final void internalToString(final StringBuilder repr, final String prefix) {
                repr.append(prefix).append("scenario=...\n");
                this.scenario.internalToString(repr, prefix + "  ");
            }
            
            private final class CurrentScenarioConverter
            {
                private final RawScenario scenario;
                
                private CurrentScenarioConverter() {
                    super();
                    this.scenario = new RawScenario();
                }
                
                public void pushResult() {
                    CurrentScenario.this.scenario.scenarioId = this.scenario.scenarioId;
                    CurrentScenario.this.scenario.globalVars.clear();
                    CurrentScenario.this.scenario.globalVars.ensureCapacity(this.scenario.globalVars.size());
                    CurrentScenario.this.scenario.globalVars.addAll(this.scenario.globalVars);
                    CurrentScenario.this.scenario.currentVarsForAddedUsers.clear();
                    CurrentScenario.this.scenario.currentVarsForAddedUsers.ensureCapacity(this.scenario.currentVarsForAddedUsers.size());
                    CurrentScenario.this.scenario.currentVarsForAddedUsers.addAll(this.scenario.currentVarsForAddedUsers);
                    CurrentScenario.this.scenario.activeActionGroups.clear();
                    CurrentScenario.this.scenario.activeActionGroups.ensureCapacity(this.scenario.activeActionGroups.size());
                    CurrentScenario.this.scenario.activeActionGroups.addAll(this.scenario.activeActionGroups);
                    CurrentScenario.this.scenario.executedActionGroups.clear();
                    CurrentScenario.this.scenario.executedActionGroups.ensureCapacity(this.scenario.executedActionGroups.size());
                    CurrentScenario.this.scenario.executedActionGroups.addAll(this.scenario.executedActionGroups);
                    CurrentScenario.this.scenario.startTime = this.scenario.startTime;
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
        
        private final class CurrentScenariiConverter
        {
            private final ArrayList<CurrentScenario> scenarii;
            
            private CurrentScenariiConverter() {
                super();
                this.scenarii = new ArrayList<CurrentScenario>(0);
            }
            
            public void pushResult() {
                CurrentScenarii.this.scenarii.clear();
                CurrentScenarii.this.scenarii.ensureCapacity(this.scenarii.size());
                CurrentScenarii.this.scenarii.addAll(this.scenarii);
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
    
    public static class CurrentChallengeInfo implements VersionableObject
    {
        public final ArrayList<ChallengeInfo> challenges;
        
        public CurrentChallengeInfo() {
            super();
            this.challenges = new ArrayList<ChallengeInfo>(0);
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            if (this.challenges.size() > 65535) {
                return false;
            }
            buffer.putShort((short)this.challenges.size());
            for (int i = 0; i < this.challenges.size(); ++i) {
                final ChallengeInfo challenges_element = this.challenges.get(i);
                final boolean challenges_element_ok = challenges_element.serialize(buffer);
                if (!challenges_element_ok) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final int challenges_size = buffer.getShort() & 0xFFFF;
            this.challenges.clear();
            this.challenges.ensureCapacity(challenges_size);
            for (int i = 0; i < challenges_size; ++i) {
                final ChallengeInfo challenges_element = new ChallengeInfo();
                final boolean challenges_element_ok = challenges_element.unserialize(buffer);
                if (!challenges_element_ok) {
                    return false;
                }
                this.challenges.add(challenges_element);
            }
            return true;
        }
        
        @Override
        public void clear() {
            this.challenges.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 1) {
                return this.unserialize(buffer);
            }
            final CurrentChallengeInfoConverter converter = new CurrentChallengeInfoConverter();
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
            for (int i = 0; i < this.challenges.size(); ++i) {
                final ChallengeInfo challenges_element = this.challenges.get(i);
                size += challenges_element.serializedSize();
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
            repr.append(prefix).append("challenges=");
            if (this.challenges.isEmpty()) {
                repr.append("{}").append('\n');
            }
            else {
                repr.append("(").append(this.challenges.size()).append(" elements)...\n");
                for (int i = 0; i < this.challenges.size(); ++i) {
                    final ChallengeInfo challenges_element = this.challenges.get(i);
                    challenges_element.internalToString(repr, prefix + i + "/ ");
                }
            }
        }
        
        public static class ChallengeInfo implements VersionableObject
        {
            public final RawChallengeInfo challenge;
            
            public ChallengeInfo() {
                super();
                this.challenge = new RawChallengeInfo();
            }
            
            @Override
            public boolean serialize(final ByteBuffer buffer) {
                final boolean challenge_ok = this.challenge.serialize(buffer);
                return challenge_ok;
            }
            
            @Override
            public boolean unserialize(final ByteBuffer buffer) {
                final boolean challenge_ok = this.challenge.unserialize(buffer);
                return challenge_ok;
            }
            
            @Override
            public void clear() {
                this.challenge.clear();
            }
            
            @Override
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version >= 1) {
                    return this.unserialize(buffer);
                }
                final ChallengeInfoConverter converter = new ChallengeInfoConverter();
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
                size += this.challenge.serializedSize();
                return size;
            }
            
            @Override
            public final String toString() {
                final StringBuilder repr = new StringBuilder();
                this.internalToString(repr, "");
                return repr.toString();
            }
            
            public final void internalToString(final StringBuilder repr, final String prefix) {
                repr.append(prefix).append("challenge=...\n");
                this.challenge.internalToString(repr, prefix + "  ");
            }
            
            private final class ChallengeInfoConverter
            {
                private final RawChallengeInfo challenge;
                
                private ChallengeInfoConverter() {
                    super();
                    this.challenge = new RawChallengeInfo();
                }
                
                public void pushResult() {
                    ChallengeInfo.this.challenge.scenarioId = this.challenge.scenarioId;
                    ChallengeInfo.this.challenge.activeGoals.clear();
                    ChallengeInfo.this.challenge.activeGoals.ensureCapacity(this.challenge.activeGoals.size());
                    ChallengeInfo.this.challenge.activeGoals.addAll(this.challenge.activeGoals);
                    ChallengeInfo.this.challenge.executedGoals.clear();
                    ChallengeInfo.this.challenge.executedGoals.ensureCapacity(this.challenge.executedGoals.size());
                    ChallengeInfo.this.challenge.executedGoals.addAll(this.challenge.executedGoals);
                    ChallengeInfo.this.challenge.globalVars.clear();
                    ChallengeInfo.this.challenge.globalVars.ensureCapacity(this.challenge.globalVars.size());
                    ChallengeInfo.this.challenge.globalVars.addAll(this.challenge.globalVars);
                    ChallengeInfo.this.challenge.watchedVars.clear();
                    ChallengeInfo.this.challenge.watchedVars.ensureCapacity(this.challenge.watchedVars.size());
                    ChallengeInfo.this.challenge.watchedVars.addAll(this.challenge.watchedVars);
                    ChallengeInfo.this.challenge.remainingTime = this.challenge.remainingTime;
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
        
        private final class CurrentChallengeInfoConverter
        {
            private final ArrayList<ChallengeInfo> challenges;
            
            private CurrentChallengeInfoConverter() {
                super();
                this.challenges = new ArrayList<ChallengeInfo>(0);
            }
            
            public void pushResult() {
                CurrentChallengeInfo.this.challenges.clear();
                CurrentChallengeInfo.this.challenges.ensureCapacity(this.challenges.size());
                CurrentChallengeInfo.this.challenges.addAll(this.challenges);
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
    
    public static class PastScenario implements VersionableObject
    {
        public int scenarioId;
        public short executionCount;
        public byte status;
        public static final int SERIALIZED_SIZE = 7;
        
        public PastScenario() {
            super();
            this.scenarioId = 0;
            this.executionCount = 0;
            this.status = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.scenarioId);
            buffer.putShort(this.executionCount);
            buffer.put(this.status);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.scenarioId = buffer.getInt();
            this.executionCount = buffer.getShort();
            this.status = buffer.get();
            return true;
        }
        
        @Override
        public void clear() {
            this.scenarioId = 0;
            this.executionCount = 0;
            this.status = 0;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 7;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("scenarioId=").append(this.scenarioId).append('\n');
            repr.append(prefix).append("executionCount=").append(this.executionCount).append('\n');
            repr.append(prefix).append("status=").append(this.status).append('\n');
        }
    }
    
    private final class RawScenarioManagerConverter
    {
        private CurrentScenarii currentScenarii;
        private CurrentChallengeInfo currentChallengeInfo;
        private final ArrayList<PastScenario> pastScenarii;
        
        private RawScenarioManagerConverter() {
            super();
            this.currentScenarii = null;
            this.currentChallengeInfo = null;
            this.pastScenarii = new ArrayList<PastScenario>(0);
        }
        
        public void pushResult() {
            RawScenarioManager.this.currentScenarii = this.currentScenarii;
            RawScenarioManager.this.currentChallengeInfo = this.currentChallengeInfo;
            RawScenarioManager.this.pastScenarii.clear();
            RawScenarioManager.this.pastScenarii.ensureCapacity(this.pastScenarii.size());
            RawScenarioManager.this.pastScenarii.addAll(this.pastScenarii);
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
