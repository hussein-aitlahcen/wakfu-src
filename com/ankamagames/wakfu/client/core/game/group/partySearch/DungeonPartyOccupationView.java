package com.ankamagames.wakfu.client.core.game.group.partySearch;

import com.ankamagames.wakfu.common.game.group.partySearch.*;
import com.ankamagames.wakfu.client.core.*;

public class DungeonPartyOccupationView extends PartyOccupationView<PvePartyOccupation>
{
    public DungeonPartyOccupationView(final PvePartyOccupationGroup group, final PvePartyOccupation occupation, final short level, final boolean registration) {
        super(group, occupation, level >= occupation.getLevel(), registration);
    }
    
    @Override
    public String getName() {
        return name(((PvePartyOccupation)this.m_occupation).getReferenceId());
    }
    
    public static String name(final int referenceId) {
        return WakfuTranslator.getInstance().getString(137, referenceId, new Object[0]);
    }
    
    @Override
    public short getLevel() {
        return ((PvePartyOccupation)this.m_occupation).getLevel();
    }
    
    @Override
    public String getLevelDescription() {
        return WakfuTranslator.getInstance().getString("levelShort.custom", ((PvePartyOccupation)this.m_occupation).getLevel());
    }
}
