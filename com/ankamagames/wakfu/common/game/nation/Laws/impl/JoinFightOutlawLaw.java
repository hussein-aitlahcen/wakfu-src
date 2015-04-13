package com.ankamagames.wakfu.common.game.nation.Laws.impl;

import com.ankamagames.wakfu.common.game.nation.Laws.impl.events.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.nation.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.framework.external.*;

public class JoinFightOutlawLaw extends NationLaw<JoinFightLawEvent>
{
    private static final NationLawListSet PARAMETERS;
    public static final NationLawModel<JoinFightOutlawLaw> MODEL;
    
    private JoinFightOutlawLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
        super(id, cost, lawPointCost, lawLocked, lawApplications);
    }
    
    @Override
    public void initialize(final List<ParserObject> parameters) {
    }
    
    @Override
    public NationLawModelConstant getModel() {
        return NationLawModelConstant.JOIN_FIGHT_OUTLAW;
    }
    
    @Override
    public boolean isTriggering(final JoinFightLawEvent event) {
        if (!event.getFightModel().isPvp()) {
            return false;
        }
        final Collection opponents = event.getTeammates();
        for (final Object opponent : opponents) {
            final Citizen player = (Citizen)opponent;
            if (player.getCitizenComportment().isOutlaw()) {
                return true;
            }
        }
        return false;
    }
    
    static {
        PARAMETERS = new NationLawListSet(new ParameterList[] { new DefaultParameterList("Nothing", new Parameter[0]) });
        MODEL = new NationLawModel<JoinFightOutlawLaw>() {
            @Override
            public JoinFightOutlawLaw createNewLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
                return new JoinFightOutlawLaw(id, cost, lawPointCost, lawLocked, lawApplications, null);
            }
            
            @Override
            public NationLawListSet getParameters() {
                return JoinFightOutlawLaw.PARAMETERS;
            }
        };
    }
}
