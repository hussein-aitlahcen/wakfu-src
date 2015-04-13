package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action;

import java.nio.*;
import com.ankamagames.wakfu.common.game.fight.*;

public class RunningEffectApplicationMessage extends AbstractFightActionMessage
{
    private int m_runningEffectId;
    byte[] m_serializedRunningEffect;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.decodeFightActionHeader(bb);
        this.m_runningEffectId = bb.getInt();
        bb.get(this.m_serializedRunningEffect = new byte[bb.getShort()]);
        return true;
    }
    
    @Override
    public int getId() {
        return 8124;
    }
    
    @Override
    public int getActionId() {
        return this.m_runningEffectId;
    }
    
    @Override
    public FightActionType getFightActionType() {
        return FightActionType.EFFECT_APPLICATION;
    }
    
    public int getRunningEffectId() {
        return this.m_runningEffectId;
    }
    
    public byte[] getSerializedRunningEffect() {
        return this.m_serializedRunningEffect;
    }
}
