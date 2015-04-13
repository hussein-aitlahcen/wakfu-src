package com.ankamagames.wakfu.common.game.nation.event;

import com.ankamagames.wakfu.common.game.nation.*;

public interface NationBuffEventHandler extends NationEventHandler
{
    void onNationBuffsChanged(int p0, Nation p1);
}
