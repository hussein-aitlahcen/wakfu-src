package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.inFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class PlayerChangeConnectionStateMessageHandler extends UsingFightMessageHandler<PlayerChangeConnectionStateMessage, Fight>
{
    private static final FightLogger m_fightLogger;
    
    @Override
    public boolean onMessage(final PlayerChangeConnectionStateMessage msg) {
        final long playerId = msg.getPlayerId();
        final byte connectionState = msg.getConnectionState();
        final CharacterInfo fighter = ((Fight)this.m_concernedFight).getFighterFromId(playerId);
        if (fighter == null) {
            return false;
        }
        String message = null;
        final TextWidgetFormater sb = new TextWidgetFormater();
        sb.addColor(Color.RED.getRGBtoHex());
        if (connectionState == 0) {
            message = WakfuTranslator.getInstance().getString("fight.disconnection", fighter.getName());
        }
        else if (connectionState == 1) {
            message = WakfuTranslator.getInstance().getString("fight.reconnection", fighter.getName());
        }
        if (message != null) {
            sb.append(message);
            PlayerChangeConnectionStateMessageHandler.m_fightLogger.info(sb.finishAndToString());
        }
        return false;
    }
    
    static {
        m_fightLogger = new FightLogger();
    }
}
