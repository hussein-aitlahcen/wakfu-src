package com.ankamagames.wakfu.client.core.game.group.party.member;

import com.ankamagames.wakfu.common.game.group.member.serialization.*;
import com.ankamagames.wakfu.common.game.group.member.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public final class HpPartListener implements PartyMemberPartListener
{
    private final PartyMemberInterface m_memberModel;
    
    public HpPartListener(final PartyMemberInterface memberModel) {
        super();
        this.m_memberModel = memberModel;
    }
    
    @Override
    public void onDataChanged() {
        final PlayerCharacter playerCharacter = (PlayerCharacter)CharacterInfoManager.getInstance().getCharacter(this.m_memberModel.getCharacterId());
        if (playerCharacter == null || playerCharacter == WakfuGameEntity.getInstance().getLocalPlayer()) {
            return;
        }
        final FighterCharacteristic hp = playerCharacter.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP);
        final int currentHp = this.m_memberModel.getCurrentHp();
        final int maxHp = this.m_memberModel.getMaxHp();
        hp.setMax(maxHp);
        hp.set(currentHp);
        final CharacteristicRegenHandler hpRegenHandler = playerCharacter.getHpRegenHandler();
        hpRegenHandler.synchronizeValue(currentHp, this.m_memberModel.getRegen());
    }
}
