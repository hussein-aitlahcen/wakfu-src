package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action;

import java.util.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.fight.*;

public class EndFightMessage extends AbstractFightActionMessage
{
    private final List<Long> m_winnerTeamMates;
    private final List<Long> m_looserTeamMates;
    private final List<Long> m_escapees;
    private boolean m_flee;
    
    public EndFightMessage() {
        super();
        this.m_winnerTeamMates = new ArrayList<Long>();
        this.m_looserTeamMates = new ArrayList<Long>();
        this.m_escapees = new ArrayList<Long>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (!this.checkMessageSize(rawDatas.length, 13, false)) {
            return false;
        }
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.decodeFightActionHeader(buffer);
        if (!(this.m_flee = (buffer.get() == 1))) {
            for (int count = buffer.get(), i = 0; i < count; ++i) {
                final long playerId = buffer.getLong();
                this.m_winnerTeamMates.add(playerId);
            }
            for (int count = buffer.get(), i = 0; i < count; ++i) {
                final long playerId = buffer.getLong();
                this.m_looserTeamMates.add(playerId);
            }
            for (int count = buffer.get(), i = 0; i < count; ++i) {
                final long playerId = buffer.getLong();
                this.m_escapees.add(playerId);
            }
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 8300;
    }
    
    public List<Long> getWinnerTeamMates() {
        return this.m_winnerTeamMates;
    }
    
    public List<Long> getLooserTeamMates() {
        return this.m_looserTeamMates;
    }
    
    public List<Long> getEscapees() {
        return this.m_escapees;
    }
    
    @Override
    public int getActionId() {
        return 0;
    }
    
    @Override
    public FightActionType getFightActionType() {
        return FightActionType.FIGHT_END;
    }
    
    public boolean isFlee() {
        return this.m_flee;
    }
}
