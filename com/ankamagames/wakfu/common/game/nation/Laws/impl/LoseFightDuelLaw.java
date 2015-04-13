package com.ankamagames.wakfu.common.game.nation.Laws.impl;

import com.ankamagames.wakfu.common.game.nation.Laws.impl.events.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.framework.external.*;

public class LoseFightDuelLaw extends NationLaw<LoseFightLawEvent>
{
    private static final NationLawListSet PARAMETERS;
    public static final NationLawModel<LoseFightDuelLaw> MODEL;
    
    private LoseFightDuelLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
        super(id, cost, lawPointCost, lawLocked, lawApplications);
    }
    
    @Override
    public void initialize(final List<ParserObject> parameters) {
    }
    
    @Override
    public NationLawModelConstant getModel() {
        return NationLawModelConstant.LOSE_FIGHT_DUEL;
    }
    
    @Override
    public boolean isTriggering(final LoseFightLawEvent event) {
        return event.getFightModel() == FightModel.DUEL;
    }
    
    static {
        PARAMETERS = new NationLawListSet(new ParameterList[] { new DefaultParameterList("Nothing", new Parameter[0]) });
        MODEL = new NationLawModel<LoseFightDuelLaw>() {
            @Override
            public LoseFightDuelLaw createNewLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
                return new LoseFightDuelLaw(id, cost, lawPointCost, lawLocked, lawApplications, null);
            }
            
            @Override
            public NationLawListSet getParameters() {
                return LoseFightDuelLaw.PARAMETERS;
            }
        };
    }
}
