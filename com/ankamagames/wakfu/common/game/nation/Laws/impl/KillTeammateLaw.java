package com.ankamagames.wakfu.common.game.nation.Laws.impl;

import com.ankamagames.wakfu.common.game.nation.Laws.impl.events.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.framework.external.*;

public class KillTeammateLaw extends NationLaw<KillLawEvent>
{
    private static final NationLawListSet PARAMETERS;
    public static final NationLawModel<KillTeammateLaw> MODEL;
    
    private KillTeammateLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
        super(id, cost, lawPointCost, lawLocked, lawApplications);
    }
    
    @Override
    public void initialize(final List<ParserObject> parameters) {
    }
    
    @Override
    public NationLawModelConstant getModel() {
        return NationLawModelConstant.KILL_TEAMMATE;
    }
    
    @Override
    public boolean isTriggering(final KillLawEvent event) {
        return event.getCitizen() != event.getKilled() && event.getCitizenTeamId() == event.getKilledTeamId();
    }
    
    static {
        PARAMETERS = new NationLawListSet(new ParameterList[] { new DefaultParameterList("Nothing", new Parameter[0]) });
        MODEL = new NationLawModel<KillTeammateLaw>() {
            @Override
            public KillTeammateLaw createNewLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
                return new KillTeammateLaw(id, cost, lawPointCost, lawLocked, lawApplications, null);
            }
            
            @Override
            public NationLawListSet getParameters() {
                return KillTeammateLaw.PARAMETERS;
            }
        };
    }
}
