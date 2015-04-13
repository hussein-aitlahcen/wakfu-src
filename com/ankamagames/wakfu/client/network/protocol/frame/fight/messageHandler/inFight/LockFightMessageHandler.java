package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.inFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class LockFightMessageHandler extends UsingFightMessageHandler<LockFightMessage, Fight>
{
    private static Logger m_logger;
    
    @Override
    public boolean onMessage(final LockFightMessage msg) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (!localPlayer.isOnFight() || msg.getFightId() != ((Fight)this.m_concernedFight).getId()) {
            LockFightMessageHandler.m_logger.warn((Object)"Message LOCK_FIGHT_MESSAGE re\u00e7u hors combat.");
            return false;
        }
        if (((Fight)this.m_concernedFight).getTeamId(localPlayer) != msg.getLockModification().getTeamId()) {
            return false;
        }
        ((Fight)this.m_concernedFight).lockFight(msg.getLockModification());
        return false;
    }
    
    static {
        LockFightMessageHandler.m_logger = Logger.getLogger((Class)LockFightMessageHandler.class);
    }
}
