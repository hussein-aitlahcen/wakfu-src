package com.ankamagames.wakfu.client.core.game.dimensionalBag.gfxProvider.groundPattern;

import com.ankamagames.wakfu.client.core.game.dimensionalBag.gfxProvider.*;
import org.apache.log4j.*;
import gnu.trove.*;

public interface GroundPattern extends ListElementUsed
{
    public static final Logger m_logger = Logger.getLogger((Class)GroundPattern.class);
    
    int getGfx(int p0, int p1);
    
    void getAllElementIds(TIntHashSet p0);
}
