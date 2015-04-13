package com.ankamagames.wakfu.common.game.nation.Laws.impl;

import com.ankamagames.wakfu.common.game.nation.Laws.impl.events.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.framework.external.*;

public class LoseFightInitiatedLaw extends NationLaw<LoseFightLawEvent>
{
    private static final NationLawListSet PARAMETERS;
    public static final NationLawModel<LoseFightInitiatedLaw> MODEL;
    
    private LoseFightInitiatedLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
        super(id, cost, lawPointCost, lawLocked, lawApplications);
    }
    
    @Override
    public void initialize(final List<ParserObject> parameters) {
    }
    
    @Override
    public NationLawModelConstant getModel() {
        return NationLawModelConstant.LOSE_FIGHT_INITIATED;
    }
    
    @Override
    public boolean isTriggering(final LoseFightLawEvent event) {
        return event.getFightModel() != FightModel.DUEL && event.getCitizen() == event.getInitiatingFighter();
    }
    
    static {
        PARAMETERS = new NationLawListSet(new ParameterList[] { new DefaultParameterList("Nothing", new Parameter[0]) });
        MODEL = new NationLawModel<LoseFightInitiatedLaw>() {
            @Override
            public LoseFightInitiatedLaw createNewLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
                return new LoseFightInitiatedLaw(id, cost, lawPointCost, lawLocked, lawApplications, null);
            }
            
            @Override
            public NationLawListSet getParameters() {
                return LoseFightInitiatedLaw.PARAMETERS;
            }
        };
    }
}
