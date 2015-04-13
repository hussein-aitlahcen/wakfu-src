package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.scenario;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class FailChallengeMessage extends InputOnlyProxyMessage
{
    private int m_scenarioId;
    private int m_nbWinners;
    private String m_winnerName;
    private int m_winnerScore;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_scenarioId = buff.getInt();
        this.m_nbWinners = buff.getInt();
        final byte winnerNameLength = buff.get();
        if (winnerNameLength > 0) {
            final byte[] winnerNameData = new byte[winnerNameLength];
            buff.get(winnerNameData);
            this.m_winnerName = StringUtils.fromUTF8(winnerNameData);
        }
        this.m_winnerScore = buff.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 11222;
    }
    
    public int getScenarioId() {
        return this.m_scenarioId;
    }
    
    public int getNbWinners() {
        return this.m_nbWinners;
    }
    
    public String getWinnerName() {
        return this.m_winnerName;
    }
    
    public int getWinnerScore() {
        return this.m_winnerScore;
    }
}
