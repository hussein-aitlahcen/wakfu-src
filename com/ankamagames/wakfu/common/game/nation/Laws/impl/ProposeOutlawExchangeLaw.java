package com.ankamagames.wakfu.common.game.nation.Laws.impl;

import com.ankamagames.wakfu.common.game.nation.Laws.impl.events.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.framework.external.*;

public class ProposeOutlawExchangeLaw extends NationLaw<ExchangeLawEvent>
{
    private static final NationLawListSet PARAMETERS;
    public static final NationLawModel<ProposeOutlawExchangeLaw> MODEL;
    
    private ProposeOutlawExchangeLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
        super(id, cost, lawPointCost, lawLocked, lawApplications);
    }
    
    @Override
    public void initialize(final List<ParserObject> parameters) {
    }
    
    @Override
    public NationLawModelConstant getModel() {
        return NationLawModelConstant.PROPOSE_OUTLAW_EXCHANGE;
    }
    
    @Override
    public boolean isTriggering(final ExchangeLawEvent event) {
        return event.getRequestedCitizen().getCitizenComportment().isOutlaw();
    }
    
    static {
        PARAMETERS = new NationLawListSet(new ParameterList[] { new DefaultParameterList("Nothing", new Parameter[0]) });
        MODEL = new NationLawModel<ProposeOutlawExchangeLaw>() {
            @Override
            public ProposeOutlawExchangeLaw createNewLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
                return new ProposeOutlawExchangeLaw(id, cost, lawPointCost, lawLocked, lawApplications, null);
            }
            
            @Override
            public NationLawListSet getParameters() {
                return ProposeOutlawExchangeLaw.PARAMETERS;
            }
        };
    }
}
