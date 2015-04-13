package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.wakfu.common.game.nation.handlers.*;
import com.ankamagames.wakfu.common.game.nation.*;

public class CNationJusticeHandler extends NationJusticeHandler
{
    public CNationJusticeHandler(final Nation nation) {
        super(nation);
    }
    
    @Override
    public void onCriminalCandidate(final long citizenId) {
    }
    
    @Override
    public void onRevalidateCandidate(final long citizenId) {
    }
}
