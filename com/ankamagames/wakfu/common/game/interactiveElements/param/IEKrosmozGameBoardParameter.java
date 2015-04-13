package com.ankamagames.wakfu.common.game.interactiveElements.param;

import com.ankamagames.wakfu.common.game.chaos.*;

public class IEKrosmozGameBoardParameter extends IEParameter
{
    private final byte m_gameId;
    
    public IEKrosmozGameBoardParameter(final int paramId, final int visualId, final ChaosInteractiveCategory chaos, final int chaosCollectorId, final byte gameId) {
        super(paramId, visualId, chaos, chaosCollectorId);
        this.m_gameId = gameId;
    }
    
    public byte getGameId() {
        return this.m_gameId;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("IEKrosmozGameBoardParameter");
        sb.append("{m_gameId=").append(this.m_gameId);
        sb.append('}');
        return sb.toString();
    }
}
