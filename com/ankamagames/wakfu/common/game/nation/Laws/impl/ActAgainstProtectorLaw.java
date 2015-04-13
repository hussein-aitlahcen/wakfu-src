package com.ankamagames.wakfu.common.game.nation.Laws.impl;

import com.ankamagames.wakfu.common.game.nation.Laws.impl.events.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.framework.external.*;

public class ActAgainstProtectorLaw extends NationLaw<ProtectorWishLawEvent>
{
    private static final NationLawListSet PARAMETERS;
    public static final NationLawModel<ActAgainstProtectorLaw> MODEL;
    
    private ActAgainstProtectorLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
        super(id, cost, lawPointCost, lawLocked, lawApplications);
    }
    
    @Override
    public void initialize(final List<ParserObject> parameters) {
    }
    
    @Override
    public NationLawModelConstant getModel() {
        return NationLawModelConstant.ACT_AGAINST_PROTECTOR;
    }
    
    @Override
    public boolean isTriggering(final ProtectorWishLawEvent event) {
        return event.getActionType() == ProtectorWishLawEvent.ActionType.AGAINST;
    }
    
    static {
        PARAMETERS = new NationLawListSet(new ParameterList[] { new DefaultParameterList("Nothing", new Parameter[0]) });
        MODEL = new NationLawModel<ActAgainstProtectorLaw>() {
            @Override
            public ActAgainstProtectorLaw createNewLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
                return new ActAgainstProtectorLaw(id, cost, lawPointCost, lawLocked, lawApplications, null);
            }
            
            @Override
            public NationLawListSet getParameters() {
                return ActAgainstProtectorLaw.PARAMETERS;
            }
        };
    }
}
