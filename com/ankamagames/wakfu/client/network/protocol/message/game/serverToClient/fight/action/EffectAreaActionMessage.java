package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action;

import java.nio.*;
import com.ankamagames.wakfu.common.game.fight.*;

public class EffectAreaActionMessage extends AbstractFightActionMessage
{
    private long m_areaId;
    private long m_areaBaseId;
    private long m_targetId;
    private boolean m_apply;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.decodeFightActionHeader(buffer);
        this.m_apply = (buffer.get() == 1);
        this.m_areaId = buffer.getLong();
        this.m_areaBaseId = buffer.getLong();
        this.m_targetId = buffer.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 6200;
    }
    
    @Override
    public int getActionId() {
        return 0;
    }
    
    @Override
    public FightActionType getFightActionType() {
        return FightActionType.EFFEC_AREA_ACTION;
    }
    
    public boolean isApply() {
        return this.m_apply;
    }
    
    public long getAreaId() {
        return this.m_areaId;
    }
    
    public long getTargetId() {
        return this.m_targetId;
    }
    
    public long getAreaBaseId() {
        return this.m_areaBaseId;
    }
}
