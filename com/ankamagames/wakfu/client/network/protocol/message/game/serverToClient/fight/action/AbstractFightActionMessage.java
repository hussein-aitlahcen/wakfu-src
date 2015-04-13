package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action;

import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.common.game.fight.*;
import java.nio.*;

public abstract class AbstractFightActionMessage extends AbstractFightMessage
{
    protected static final int FIGHT_ACTION_HEADER_SIZE = 12;
    private int m_uniqueId;
    private int m_triggeringActionUniqueId;
    
    public AbstractFightActionMessage() {
        super();
        this.m_triggeringActionUniqueId = -1;
    }
    
    public abstract int getActionId();
    
    public abstract FightActionType getFightActionType();
    
    protected void decodeFightActionHeader(final ByteBuffer buff) {
        this.decodeFightHeader(buff);
        this.m_uniqueId = buff.getInt();
        this.m_triggeringActionUniqueId = buff.getInt();
    }
    
    public int getUniqueId() {
        return this.m_uniqueId;
    }
    
    public int getTriggeringActionUniqueId() {
        return this.m_triggeringActionUniqueId;
    }
}
