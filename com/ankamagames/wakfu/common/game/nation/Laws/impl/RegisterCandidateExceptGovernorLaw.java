package com.ankamagames.wakfu.common.game.nation.Laws.impl;

import com.ankamagames.wakfu.common.game.nation.Laws.impl.events.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.framework.external.*;

public class RegisterCandidateExceptGovernorLaw extends NationLaw<RegisterCandidateLawEvent>
{
    private static final NationLawListSet PARAMETERS;
    public static final NationLawModel<RegisterCandidateExceptGovernorLaw> MODEL;
    
    private RegisterCandidateExceptGovernorLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
        super(id, cost, lawPointCost, lawLocked, lawApplications);
    }
    
    @Override
    public void initialize(final List<ParserObject> parameters) {
    }
    
    @Override
    public NationLawModelConstant getModel() {
        return NationLawModelConstant.REGISTER_CANDIDATE_EXCEPT_GOVERNOR;
    }
    
    @Override
    public boolean isTriggering(final RegisterCandidateLawEvent event) {
        final Citizen citizen = event.getCitizen();
        final Nation citizenNation = citizen.getCitizenComportment().getNation();
        final GovernmentInfo nationGovernor = citizenNation.getGovernment().getGovernor();
        return nationGovernor != null && nationGovernor.getId() != citizen.getId();
    }
    
    static {
        PARAMETERS = new NationLawListSet(new ParameterList[] { new DefaultParameterList("Nothing", new Parameter[0]) });
        MODEL = new NationLawModel<RegisterCandidateExceptGovernorLaw>() {
            @Override
            public RegisterCandidateExceptGovernorLaw createNewLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
                return new RegisterCandidateExceptGovernorLaw(id, cost, lawPointCost, lawLocked, lawApplications, null);
            }
            
            @Override
            public NationLawListSet getParameters() {
                return RegisterCandidateExceptGovernorLaw.PARAMETERS;
            }
        };
    }
}
