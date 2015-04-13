package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import java.util.*;

public class CharacterSerializedScenarioManager extends CharacterSerializedPart implements VersionableObject
{
    public final RawScenarioManager scenarioManager;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedScenarioManager() {
        super();
        this.scenarioManager = new RawScenarioManager();
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedScenarioManager.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedScenarioManager");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedScenarioManager", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedScenarioManager.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedScenarioManager");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedScenarioManager", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedScenarioManager.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        final boolean scenarioManager_ok = this.scenarioManager.serialize(buffer);
        return scenarioManager_ok;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final boolean scenarioManager_ok = this.scenarioManager.unserialize(buffer);
        return scenarioManager_ok;
    }
    
    @Override
    public void clear() {
        this.scenarioManager.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 1) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedScenarioManagerConverter converter = new CharacterSerializedScenarioManagerConverter();
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
        size += this.scenarioManager.serializedSize();
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("scenarioManager=...\n");
        this.scenarioManager.internalToString(repr, prefix + "  ");
    }
    
    private final class CharacterSerializedScenarioManagerConverter
    {
        private final RawScenarioManager scenarioManager;
        
        private CharacterSerializedScenarioManagerConverter() {
            super();
            this.scenarioManager = new RawScenarioManager();
        }
        
        public void pushResult() {
            CharacterSerializedScenarioManager.this.scenarioManager.currentScenarii = this.scenarioManager.currentScenarii;
            CharacterSerializedScenarioManager.this.scenarioManager.currentChallengeInfo = this.scenarioManager.currentChallengeInfo;
            CharacterSerializedScenarioManager.this.scenarioManager.pastScenarii.clear();
            CharacterSerializedScenarioManager.this.scenarioManager.pastScenarii.ensureCapacity(this.scenarioManager.pastScenarii.size());
            CharacterSerializedScenarioManager.this.scenarioManager.pastScenarii.addAll(this.scenarioManager.pastScenarii);
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
