package com.ankamagames.wakfu.common.game.nation.Laws.impl;

import com.ankamagames.wakfu.common.game.nation.Laws.impl.events.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.framework.external.*;

public class VoteForGovernorLaw extends NationLaw<VoteLawEvent>
{
    private static final NationLawListSet PARAMETERS;
    public static final NationLawModel<VoteForGovernorLaw> MODEL;
    
    private VoteForGovernorLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
        super(id, cost, lawPointCost, lawLocked, lawApplications);
    }
    
    @Override
    public void initialize(final List<ParserObject> parameters) {
    }
    
    @Override
    public NationLawModelConstant getModel() {
        return NationLawModelConstant.VOTE_FOR_GOVERNOR;
    }
    
    @Override
    public boolean isTriggering(final VoteLawEvent event) {
        final Citizen citizen = event.getCitizen();
        final Nation citizenNation = citizen.getCitizenComportment().getNation();
        final GovernmentInfo nationGovernor = citizenNation.getGovernment().getGovernor();
        return nationGovernor != null && nationGovernor.getId() == event.getCandidateId();
    }
    
    static {
        PARAMETERS = new NationLawListSet(new ParameterList[] { new DefaultParameterList("Nothing", new Parameter[0]) });
        MODEL = new NationLawModel<VoteForGovernorLaw>() {
            @Override
            public VoteForGovernorLaw createNewLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
                return new VoteForGovernorLaw(id, cost, lawPointCost, lawLocked, lawApplications, null);
            }
            
            @Override
            public NationLawListSet getParameters() {
                return VoteForGovernorLaw.PARAMETERS;
            }
        };
    }
}
