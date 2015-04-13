package com.ankamagames.wakfu.common.game.nation.Laws.impl;

import com.ankamagames.wakfu.common.game.nation.Laws.impl.events.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.framework.external.*;

public class WonFightProtectorLaw extends NationLaw<WonFightLawEvent>
{
    private static final NationLawListSet PARAMETERS;
    public static final NationLawModel<WonFightProtectorLaw> MODEL;
    
    private WonFightProtectorLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
        super(id, cost, lawPointCost, lawLocked, lawApplications);
    }
    
    @Override
    public void initialize(final List<ParserObject> parameters) {
    }
    
    @Override
    public NationLawModelConstant getModel() {
        return NationLawModelConstant.WON_FIGHT_PROTECTOR;
    }
    
    @Override
    public boolean isTriggering(final WonFightLawEvent event) {
        return event.getFightModel() == FightModel.PROTECTOR_ASSAULT;
    }
    
    static {
        PARAMETERS = new NationLawListSet(new ParameterList[] { new DefaultParameterList("Nothing", new Parameter[0]) });
        MODEL = new NationLawModel<WonFightProtectorLaw>() {
            @Override
            public WonFightProtectorLaw createNewLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
                return new WonFightProtectorLaw(id, cost, lawPointCost, lawLocked, lawApplications, null);
            }
            
            @Override
            public NationLawListSet getParameters() {
                return WonFightProtectorLaw.PARAMETERS;
            }
        };
    }
}
