package com.ankamagames.wakfu.common.game.fighter.specialEvent;

import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public class EffectModifiedEvent implements SpecialEvent
{
    private final HashMap<Long, RunningEffect> m_pairs;
    
    @Override
    public int getId() {
        return 1002;
    }
    
    public EffectModifiedEvent(final HashMap<Long, RunningEffect> pairs) {
        super();
        this.m_pairs = pairs;
    }
    
    public HashMap<Long, RunningEffect> getPairs() {
        return this.m_pairs;
    }
}
