package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.occupation.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.network.protocol.frame.*;

public class SimpleOccupationModificationAction extends TimedAction
{
    private final long m_concernedPlayerId;
    private final short m_modificationType;
    private final short m_occupationType;
    private final byte[] m_data;
    
    public SimpleOccupationModificationAction(final SimpleOccupationModificationMessage message) {
        super(TimedAction.getNextUid(), FightActionType.OCCUPATION_MODIFICATION.getId(), 0);
        this.m_concernedPlayerId = message.getConcernedPlayerId();
        this.m_modificationType = message.getModificationType();
        this.m_occupationType = message.getOccupationType();
        this.m_data = message.getData();
    }
    
    @Override
    protected long onRun() {
        NetOccupationFrame.getInstance().simpleOccupationModification(this.m_occupationType, this.m_data, this.m_concernedPlayerId, this.m_modificationType);
        return 0L;
    }
    
    @Override
    protected void onActionFinished() {
    }
}
