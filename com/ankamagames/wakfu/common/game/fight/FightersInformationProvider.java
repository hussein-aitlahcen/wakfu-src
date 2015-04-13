package com.ankamagames.wakfu.common.game.fight;

import java.util.*;

public interface FightersInformationProvider<F extends BasicFighter>
{
    int getId();
    
    Collection<F> getFighters();
    
    F getFighterFromId(long p0);
    
    Collection<F> getFightersInTeam(byte p0);
    
    Collection<F> getFightersPresentInTimelineInPlayInTeam(byte p0);
}
