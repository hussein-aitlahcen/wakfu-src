package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.inFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.movementSelector.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public final class CharacterPublicCharacteristicsDataMessageHandler extends UsingFightMessageHandler<CharacterPublicCharacteristicsDataMessage, Fight>
{
    @Override
    public boolean onMessage(final CharacterPublicCharacteristicsDataMessage msg) {
        final long characterId = msg.getCharacterId();
        final byte[] data = msg.getData();
        final CharacterInfo fighterFromId = ((Fight)this.m_concernedFight).getFighterFromId(characterId);
        if (fighterFromId == null) {
            return false;
        }
        final CharacterActor actor = fighterFromId.getActor();
        final MovementSelector movementSelector = actor.getMovementSelector();
        actor.setMovementSelector(NoneMovementSelector.getInstance());
        fighterFromId.fromBuild(data);
        final FighterCharacteristicType[] values = BasicCharacterInfo.PUBLIC_TYPES;
        for (int i = 0; i < values.length; ++i) {
            final FighterCharacteristicType value = values[i];
            final FighterCharacteristic characteristic = fighterFromId.getCharacteristic((CharacteristicType)value);
            characteristic.dispatchUpdate();
        }
        if (((Fight)this.m_concernedFight).getStatus() == AbstractFight.FightStatus.PLACEMENT) {
            ((Fight)this.m_concernedFight).getTimeline().updateDynamicOrder();
            fighterFromId.setApPmPwToMax();
        }
        actor.setMovementSelector(movementSelector);
        return false;
    }
}
