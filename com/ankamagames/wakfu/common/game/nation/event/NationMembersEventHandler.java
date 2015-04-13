package com.ankamagames.wakfu.common.game.nation.event;

import com.ankamagames.wakfu.common.game.nation.*;

public interface NationMembersEventHandler extends NationEventHandler
{
    void onMemberAdded(Citizen p0);
    
    void onMemberRemoved(Nation p0, Citizen p1);
}
