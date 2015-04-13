package com.ankamagames.wakfu.common.game.nation.Laws.impl;

import com.ankamagames.wakfu.common.game.nation.Laws.impl.events.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.framework.external.*;

public class VoteAgainstGovernorLaw extends NationLaw<VoteLawEvent>
{
    private static final NationLawListSet PARAMETERS;
    public static final NationLawModel<VoteAgainstGovernorLaw> MODEL;
    
    private VoteAgainstGovernorLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
        super(id, cost, lawPointCost, lawLocked, lawApplications);
    }
    
    @Override
    public void initialize(final List<ParserObject> parameters) {
    }
    
    @Override
    public NationLawModelConstant getModel() {
        return NationLawModelConstant.VOTE_AGAINST_GOVERNOR;
    }
    
    @Override
    public boolean isTriggering(final VoteLawEvent event) {
        final Citizen citizen = event.getCitizen();
        final Nation citizenNation = citizen.getCitizenComportment().getNation();
        final GovernmentInfo nationGovernor = citizenNation.getGovernment().getGovernor();
        return nationGovernor != null && nationGovernor.getId() != event.getCandidateId();
    }
    
    static {
        PARAMETERS = new NationLawListSet(new ParameterList[] { new DefaultParameterList("Nothing", new Parameter[0]) });
        MODEL = new NationLawModel<VoteAgainstGovernorLaw>() {
            @Override
            public VoteAgainstGovernorLaw createNewLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
                return new VoteAgainstGovernorLaw(id, cost, lawPointCost, lawLocked, lawApplications, null);
            }
            
            @Override
            public NationLawListSet getParameters() {
                return VoteAgainstGovernorLaw.PARAMETERS;
            }
        };
    }
}
