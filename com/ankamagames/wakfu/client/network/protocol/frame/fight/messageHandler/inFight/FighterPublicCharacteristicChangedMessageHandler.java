package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.inFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class FighterPublicCharacteristicChangedMessageHandler extends UsingFightMessageHandler<FighterEquipmentChangedMessage, Fight>
{
    private static Logger m_logger;
    
    @Override
    public boolean onMessage(final FighterEquipmentChangedMessage msg) {
        final CharacterInfo fighter = ((Fight)this.m_concernedFight).getFighterFromId(msg.getFighterId());
        if (fighter == null) {
            FighterPublicCharacteristicChangedMessageHandler.m_logger.error((Object)(msg.getClass().getSimpleName() + " pour le fighter " + msg.getFighterId() + " qui n'est pas dans le combat"));
            return false;
        }
        fighter.getCharacteristic((CharacteristicType)FighterCharacteristicType.INIT).set(msg.getFighterInit());
        fighter.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP).set(msg.getFighterHp());
        fighter.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP).setMax(msg.getFighterMaxHp());
        ((Fight)this.m_concernedFight).getTimeline().updateDynamicOrder();
        return false;
    }
    
    static {
        FighterPublicCharacteristicChangedMessageHandler.m_logger = Logger.getLogger((Class)FighterPublicCharacteristicChangedMessageHandler.class);
    }
}
