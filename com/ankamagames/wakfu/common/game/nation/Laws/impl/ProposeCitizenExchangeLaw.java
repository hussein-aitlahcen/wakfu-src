package com.ankamagames.wakfu.common.game.nation.Laws.impl;

import com.ankamagames.wakfu.common.game.nation.Laws.impl.events.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.framework.external.*;

public class ProposeCitizenExchangeLaw extends NationLaw<ExchangeLawEvent>
{
    private static final NationLawListSet PARAMETERS;
    public static final NationLawModel<ProposeCitizenExchangeLaw> MODEL;
    
    private ProposeCitizenExchangeLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
        super(id, cost, lawPointCost, lawLocked, lawApplications);
    }
    
    @Override
    public void initialize(final List<ParserObject> parameters) {
    }
    
    @Override
    public NationLawModelConstant getModel() {
        return NationLawModelConstant.PROPOSE_CITIZEN_EXCHANGE;
    }
    
    @Override
    public boolean isTriggering(final ExchangeLawEvent event) {
        final BasicCharacterInfo citizen = (BasicCharacterInfo)event.getCitizen();
        final int refNationId = citizen.getTravellingNationId();
        return event.getRequestedCitizen().getCitizenComportment().getNationId() == refNationId;
    }
    
    static {
        PARAMETERS = new NationLawListSet(new ParameterList[] { new DefaultParameterList("Nothing", new Parameter[0]) });
        MODEL = new NationLawModel<ProposeCitizenExchangeLaw>() {
            @Override
            public ProposeCitizenExchangeLaw createNewLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
                return new ProposeCitizenExchangeLaw(id, cost, lawPointCost, lawLocked, lawApplications, null);
            }
            
            @Override
            public NationLawListSet getParameters() {
                return ProposeCitizenExchangeLaw.PARAMETERS;
            }
        };
    }
}
