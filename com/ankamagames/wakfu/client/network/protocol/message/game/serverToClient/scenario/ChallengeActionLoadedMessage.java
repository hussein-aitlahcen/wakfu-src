package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.scenario;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import java.nio.*;

public class ChallengeActionLoadedMessage extends InputOnlyProxyMessage
{
    private int m_scenarioId;
    private TIntArrayList m_startedActions;
    private long m_remainingTime;
    private IntLongLightWeightMap m_vars;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_scenarioId = buff.getInt();
        final byte size = buff.get();
        this.m_startedActions = new TIntArrayList(size);
        for (byte i = 0; i < size; ++i) {
            this.m_startedActions.add(buff.getInt());
        }
        if (buff.get() == 1) {
            this.m_remainingTime = buff.getInt();
        }
        final int numVars = buff.getInt();
        if (numVars != 0) {
            this.m_vars = new IntLongLightWeightMap(numVars);
            for (int j = 0; j < numVars; ++j) {
                this.m_vars.put(buff.get(), buff.getLong());
            }
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 11202;
    }
    
    public int getScenarioId() {
        return this.m_scenarioId;
    }
    
    public TIntArrayList getActions() {
        return this.m_startedActions;
    }
    
    public long getRemainingTime() {
        return this.m_remainingTime;
    }
    
    public IntLongLightWeightMap getVars() {
        return this.m_vars;
    }
}
