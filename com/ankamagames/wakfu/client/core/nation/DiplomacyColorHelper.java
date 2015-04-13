package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.crime.constants.*;
import com.ankamagames.wakfu.client.core.game.protector.*;

public final class DiplomacyColorHelper
{
    public static Color getColor(final CitizenComportment comp, final Nation nation) {
        if (NationManager.INSTANCE.getNationById(comp.getNationId()).getDiplomacyManager().getAlignment(nation.getNationId()) == NationAlignement.ENEMY) {
            return TerritoryViewConstants.ENNEMY;
        }
        return (comp.getNation() == nation || nation.getDiplomacyManager().getAlignment(comp.getNation().getNationId()) == NationAlignement.ALLIED) ? TerritoryViewConstants.FRIENDLY : TerritoryViewConstants.NEUTRAL;
    }
}
