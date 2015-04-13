package com.ankamagames.wakfu.common.game.nation.Laws.impl;

import com.ankamagames.wakfu.common.game.nation.Laws.impl.events.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.external.*;

public class UseInteractiveElementsLaw extends NationLaw<UseInteractiveElementsLawEvent>
{
    private static final NationLawListSet PARAMETERS;
    public static final NationLawModel<UseInteractiveElementsLaw> MODEL;
    private final TLongHashSet m_elementIds;
    
    private UseInteractiveElementsLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
        super(id, cost, lawPointCost, lawLocked, lawApplications);
        this.m_elementIds = new TLongHashSet();
    }
    
    @Override
    public void initialize(final List<ParserObject> parameters) {
        final NumberList list = parameters.get(0);
        this.m_elementIds.addAll(list.getValue(null, null, null, null).toNativeArray());
    }
    
    @Override
    public NationLawModelConstant getModel() {
        return NationLawModelConstant.USE_INTERACTIVE_ELEMENTS;
    }
    
    @Override
    public boolean isTriggering(final UseInteractiveElementsLawEvent event) {
        return this.m_elementIds.contains(event.getElementId());
    }
    
    static {
        PARAMETERS = new NationLawListSet(new ParameterList[] { new DefaultParameterList("Default", new Parameter[] { new ParserObjectParameter("Ids des Elements interactifs", ParserType.NUMBERLIST) }) });
        MODEL = new NationLawModel<UseInteractiveElementsLaw>() {
            @Override
            public UseInteractiveElementsLaw createNewLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
                return new UseInteractiveElementsLaw(id, cost, lawPointCost, lawLocked, lawApplications, null);
            }
            
            @Override
            public NationLawListSet getParameters() {
                return UseInteractiveElementsLaw.PARAMETERS;
            }
        };
    }
}
