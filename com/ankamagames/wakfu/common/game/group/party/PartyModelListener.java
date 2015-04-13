package com.ankamagames.wakfu.common.game.group.party;

import com.ankamagames.wakfu.common.game.group.member.*;

public interface PartyModelListener
{
    void onMemberAdded(PartyModelInterface p0, PartyMemberInterface p1);
    
    void onMemberRemoved(PartyModelInterface p0, PartyMemberInterface p1);
    
    void onLeaderChange(PartyModelInterface p0, long p1, long p2);
}
