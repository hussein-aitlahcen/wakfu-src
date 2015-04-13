package com.ankamagames.wakfu.client.core.game.background.gazette;

import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.constants.*;

class GazetteConstants
{
    private static final TIntObjectHashMap<GameDateConst> m_gazetteUnlockDate;
    private static final int GAZETTE_ALMANAXX_ID = 185;
    
    static GameDateConst getUnlockDate(final int gazetteId) {
        final GameDateConst unlockDate = GazetteConstants.m_gazetteUnlockDate.get(gazetteId);
        return (unlockDate == null) ? GameDate.NULL_DATE : unlockDate;
    }
    
    static {
        (m_gazetteUnlockDate = new TIntObjectHashMap<GameDateConst>()).put(185, ActivationConstants.ALMANAX_UNLOCK_DATE);
    }
}
