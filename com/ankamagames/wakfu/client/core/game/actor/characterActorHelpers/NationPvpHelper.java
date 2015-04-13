package com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.nation.crime.constants.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.nation.*;

public final class NationPvpHelper
{
    public static int getPvpParticle(final CharacterInfo citizen) {
        final int nationId = citizen.getNationId();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null) {
            return -1;
        }
        final Nation localPlayerNation = NationManager.INSTANCE.getNationById(localPlayer.getCitizenComportment().getNationId());
        if (localPlayerNation == null) {
            return -1;
        }
        final int travellingNationId = citizen.getTravellingNationId();
        final NationAlignement align1 = citizen.getCitizenComportment().getNation().getDiplomacyManager().getAlignment(travellingNationId);
        final NationAlignement align2 = localPlayerNation.getDiplomacyManager().getAlignment(travellingNationId);
        final NationAlignement align3 = localPlayerNation.getDiplomacyManager().getAlignment(citizen.getNationId());
        if (citizen.getCitizenComportment().getPvpState().isActive() && align3 == NationAlignement.ENEMY) {
            switch (nationId) {
                case 30: {
                    return 800455;
                }
                case 31: {
                    return 800457;
                }
                case 33: {
                    return 800460;
                }
                case 32: {
                    return 800458;
                }
                case 34: {
                    return 800459;
                }
            }
        }
        if (!citizen.getCitizenComportment().isOutlaw()) {
            return -1;
        }
        if (align1 == align2) {
            return 800456;
        }
        return -1;
    }
}
