package com.ankamagames.wakfu.common.game.nation.Laws.impl;

import com.ankamagames.wakfu.common.game.nation.Laws.impl.events.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.framework.external.*;

public class CollectLaw extends NationLaw<CollectLawEvent>
{
    private static final NationLawListSet PARAMETERS;
    public static final NationLawModel<CollectLaw> MODEL;
    
    private CollectLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
        super(id, cost, lawPointCost, lawLocked, lawApplications);
    }
    
    @Override
    public void initialize(final List<ParserObject> parameters) {
    }
    
    @Override
    public NationLawModelConstant getModel() {
        return NationLawModelConstant.COLLECT_ECO;
    }
    
    @Override
    public boolean isTriggering(final CollectLawEvent event) {
        return true;
    }
    
    static {
        PARAMETERS = new NationLawListSet(new ParameterList[] { new DefaultParameterList("Nothing", new Parameter[0]) });
        MODEL = new NationLawModel<CollectLaw>() {
            @Override
            public CollectLaw createNewLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
                return new CollectLaw(id, cost, lawPointCost, lawLocked, lawApplications, null);
            }
            
            @Override
            public NationLawListSet getParameters() {
                return CollectLaw.PARAMETERS;
            }
        };
    }
}
