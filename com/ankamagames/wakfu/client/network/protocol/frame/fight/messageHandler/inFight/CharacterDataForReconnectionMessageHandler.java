package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.inFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.movementSelector.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public final class CharacterDataForReconnectionMessageHandler extends UsingFightMessageHandler<CharacterDataForReconnectionMessage, Fight>
{
    @Override
    public boolean onMessage(final CharacterDataForReconnectionMessage msg) {
        final long characterId = msg.getCharacterId();
        final byte[] data = msg.getData();
        final CharacterInfo fighterFromId = ((Fight)this.m_concernedFight).getFighterFromId(characterId);
        if (fighterFromId == null) {
            return false;
        }
        final CharacterActor actor = fighterFromId.getActor();
        final MovementSelector movementSelector = actor.getMovementSelector();
        actor.setMovementSelector(NoneMovementSelector.getInstance());
        fighterFromId.recoverFighterData(data, false);
        final FighterCharacteristicType[] values = FighterCharacteristicType.values();
        for (int i = 0; i < values.length; ++i) {
            final FighterCharacteristicType value = values[i];
            final FighterCharacteristic characteristic = fighterFromId.getCharacteristic((CharacteristicType)value);
            characteristic.dispatchUpdate();
        }
        actor.setMovementSelector(movementSelector);
        this.applySacrieurArms();
        return false;
    }
    
    private void applySacrieurArms() {
        final Collection<CharacterInfo> fighters = ((Fight)this.m_concernedFight).getFighters();
        for (final CharacterInfo fighter : fighters) {
            final Breed breed = fighter.getBreed();
            if (breed != AvatarBreed.SACRIER) {
                continue;
            }
            ((PlayerCharacter)fighter).applySacrieurArms();
        }
    }
}
