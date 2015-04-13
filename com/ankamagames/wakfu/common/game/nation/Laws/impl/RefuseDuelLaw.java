package com.ankamagames.wakfu.common.game.nation.Laws.impl;

import com.ankamagames.wakfu.common.game.nation.Laws.impl.events.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.framework.external.*;

public class RefuseDuelLaw extends NationLaw<AnswerDuelLawEvent>
{
    private static final NationLawListSet PARAMETERS;
    public static final NationLawModel<RefuseDuelLaw> MODEL;
    
    private RefuseDuelLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
        super(id, cost, lawPointCost, lawLocked, lawApplications);
    }
    
    @Override
    public void initialize(final List<ParserObject> parameters) {
    }
    
    @Override
    public NationLawModelConstant getModel() {
        return NationLawModelConstant.REFUSE_DUEL;
    }
    
    @Override
    public boolean isTriggering(final AnswerDuelLawEvent event) {
        return !event.isAccepted();
    }
    
    static {
        PARAMETERS = new NationLawListSet(new ParameterList[] { new DefaultParameterList("Nothing", new Parameter[0]) });
        MODEL = new NationLawModel<RefuseDuelLaw>() {
            @Override
            public RefuseDuelLaw createNewLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
                return new RefuseDuelLaw(id, cost, lawPointCost, lawLocked, lawApplications, null);
            }
            
            @Override
            public NationLawListSet getParameters() {
                return RefuseDuelLaw.PARAMETERS;
            }
        };
    }
}
