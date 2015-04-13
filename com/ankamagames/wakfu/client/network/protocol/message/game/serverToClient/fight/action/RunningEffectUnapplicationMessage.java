package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action;

import java.nio.*;
import com.ankamagames.wakfu.common.game.fight.*;

public class RunningEffectUnapplicationMessage extends AbstractFightActionMessage
{
    private int m_runningEffectId;
    byte[] m_serializedRunningEffect;
    long m_targetId;
    long m_ruid;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.decodeFightActionHeader(bb);
        this.m_runningEffectId = bb.getInt();
        bb.get(this.m_serializedRunningEffect = new byte[bb.getShort()]);
        this.m_targetId = bb.getLong();
        this.m_ruid = bb.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 8122;
    }
    
    @Override
    public int getActionId() {
        return this.m_runningEffectId;
    }
    
    @Override
    public FightActionType getFightActionType() {
        return FightActionType.EFFECT_UNAPPLICATION;
    }
    
    public int getRunningEffectId() {
        return this.m_runningEffectId;
    }
    
    public byte[] getSerializedRunningEffect() {
        return this.m_serializedRunningEffect;
    }
    
    public long getTargetId() {
        return this.m_targetId;
    }
    
    public long getRuid() {
        return this.m_ruid;
    }
}
