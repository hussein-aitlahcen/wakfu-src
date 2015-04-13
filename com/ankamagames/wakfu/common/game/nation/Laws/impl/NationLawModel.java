package com.ankamagames.wakfu.common.game.nation.Laws.impl;

import com.ankamagames.wakfu.common.game.nation.Laws.*;

public interface NationLawModel<Law extends NationLaw>
{
    Law createNewLaw(long p0, int p1, int p2, boolean p3, Iterable<NationLawApplication> p4);
    
    NationLawListSet getParameters();
}
