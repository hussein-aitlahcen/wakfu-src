package com.ankamagames.wakfu.client.core.game.hero;

import com.ankamagames.wakfu.common.game.companion.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.group.party.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.client.core.account.*;
import com.ankamagames.wakfu.common.datas.*;
import org.jetbrains.annotations.*;

public class ClientHeroUtils
{
    public static boolean canInviteHeroOrCompanion(final byte characterTypePlayer) {
        final LocalPlayerCharacter localPlayer = getLeaderCharacter();
        if (localPlayer == null) {
            return false;
        }
        final PartyModel party = localPlayer.getPartyComportment().getParty();
        final int companionsQty = (party != null) ? party.getCompanions(localPlayer.getClientId()).size() : 0;
        final int heroesQty = Math.max(HeroesManager.INSTANCE.getHeroesInPartyQuantity(localPlayer.getOwnerId()) - 1, 0);
        boolean qtyOk;
        if (characterTypePlayer == 5) {
            if (UnlockedCompanionGroupLimitManager.INSTANCE.hasUnlockedCompanionGroupLimit(localPlayer.getClientId())) {
                return true;
            }
            qtyOk = (companionsQty < 2);
        }
        else {
            if (characterTypePlayer != 0) {
                return false;
            }
            qtyOk = (heroesQty < 2);
        }
        return companionsQty + heroesQty < 2 && qtyOk;
    }
    
    @Nullable
    public static LocalPlayerCharacter getLeaderCharacter() {
        if (!SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.HEROES_ENABLED)) {
            return WakfuGameEntity.getInstance().getLocalPlayer();
        }
        final LocalAccountInformations localAccount = WakfuGameEntity.getInstance().getLocalAccount();
        if (localAccount == null) {
            return null;
        }
        final long leaderId = HeroesLeaderManager.INSTANCE.getLeader(localAccount.getAccountId());
        final BasicCharacterInfo hero = HeroesManager.INSTANCE.getHero(leaderId);
        if (!(hero instanceof LocalPlayerCharacter)) {
            return null;
        }
        return (LocalPlayerCharacter)hero;
    }
}
