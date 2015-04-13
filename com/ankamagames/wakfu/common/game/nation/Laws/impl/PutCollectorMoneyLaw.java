package com.ankamagames.wakfu.common.game.nation.Laws.impl;

import com.ankamagames.wakfu.common.game.nation.Laws.impl.events.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.external.*;

public class PutCollectorMoneyLaw extends NationLaw<PutCollectorContentLawEvent>
{
    private static final NationLawListSet PARAMETERS;
    public static final NationLawModel<PutCollectorMoneyLaw> MODEL;
    private int m_minAmount;
    private final TLongHashSet m_elementIds;
    
    private PutCollectorMoneyLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
        super(id, cost, lawPointCost, lawLocked, lawApplications);
        this.m_elementIds = new TLongHashSet();
    }
    
    @Override
    public void initialize(final List<ParserObject> parameters) {
        final NumericalValue amount = parameters.get(0);
        this.m_minAmount = (int)amount.getLongValue(null, null, null, null);
        final NumberList list = parameters.get(1);
        this.m_elementIds.addAll(list.getValue(null, null, null, null).toNativeArray());
    }
    
    @Override
    public NationLawModelConstant getModel() {
        return NationLawModelConstant.PUT_COLLECTOR_MONEY;
    }
    
    @Override
    public boolean isTriggering(final PutCollectorContentLawEvent event) {
        return this.m_elementIds.contains(event.getElementId()) && event.getMoneyAmount() >= this.m_minAmount;
    }
    
    static {
        PARAMETERS = new NationLawListSet(new ParameterList[] { new DefaultParameterList("Default", new Parameter[] { new ParserObjectParameter("Somme minimum", ParserType.NUMBER), new ParserObjectParameter("Ids des Elements interactifs", ParserType.NUMBERLIST) }) });
        MODEL = new NationLawModel<PutCollectorMoneyLaw>() {
            @Override
            public PutCollectorMoneyLaw createNewLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
                return new PutCollectorMoneyLaw(id, cost, lawPointCost, lawLocked, lawApplications, null);
            }
            
            @Override
            public NationLawListSet getParameters() {
                return PutCollectorMoneyLaw.PARAMETERS;
            }
        };
    }
}
