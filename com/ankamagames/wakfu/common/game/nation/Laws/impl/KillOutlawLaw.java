package com.ankamagames.wakfu.common.game.nation.Laws.impl;

import com.ankamagames.wakfu.common.game.nation.Laws.impl.events.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.framework.external.*;

public class KillOutlawLaw extends NationLaw<KillLawEvent>
{
    private static final NationLawListSet PARAMETERS;
    public static final NationLawModel<KillOutlawLaw> MODEL;
    
    private KillOutlawLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
        super(id, cost, lawPointCost, lawLocked, lawApplications);
    }
    
    @Override
    public void initialize(final List<ParserObject> parameters) {
    }
    
    @Override
    public NationLawModelConstant getModel() {
        return NationLawModelConstant.KILL_OUTLAW;
    }
    
    @Override
    public boolean isTriggering(final KillLawEvent event) {
        if (!event.getFightModel().isPvp()) {
            return false;
        }
        final BasicCharacterInfo killed = event.getKilled();
        return event.getCitizenTeamId() != killed.getTeamId() && killed.getCitizenComportment().isOutlaw();
    }
    
    static {
        PARAMETERS = new NationLawListSet(new ParameterList[] { new DefaultParameterList("Nothing", new Parameter[0]) });
        MODEL = new NationLawModel<KillOutlawLaw>() {
            @Override
            public KillOutlawLaw createNewLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
                return new KillOutlawLaw(id, cost, lawPointCost, lawLocked, lawApplications, null);
            }
            
            @Override
            public NationLawListSet getParameters() {
                return KillOutlawLaw.PARAMETERS;
            }
        };
    }
}
