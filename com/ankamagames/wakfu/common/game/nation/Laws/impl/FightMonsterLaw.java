package com.ankamagames.wakfu.common.game.nation.Laws.impl;

import com.ankamagames.wakfu.common.game.nation.Laws.impl.events.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fight.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.framework.external.*;

public class FightMonsterLaw extends NationLaw<FightLawEvent>
{
    private static final NationLawListSet PARAMETERS;
    public static final NationLawModel<FightMonsterLaw> MODEL;
    
    private FightMonsterLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
        super(id, cost, lawPointCost, lawLocked, lawApplications);
    }
    
    @Override
    public void initialize(final List<ParserObject> parameters) {
    }
    
    @Override
    public NationLawModelConstant getModel() {
        return NationLawModelConstant.FIGHT_ECO;
    }
    
    @Override
    public boolean isTriggering(final FightLawEvent event) {
        if (event.getFightModel() != FightModel.PVE) {
            return false;
        }
        final BasicCharacterInfo character = (BasicCharacterInfo)event.getCitizen();
        final BasicCharacterInfo defender = event.getDefender();
        if (character == defender) {
            return false;
        }
        final Collection opponents = event.getOpponents();
        for (final Object opponent : opponents) {
            final BasicFighter monster = (BasicFighter)opponent;
            if (monster.getType() == 1) {
                return true;
            }
        }
        return false;
    }
    
    static {
        PARAMETERS = new NationLawListSet(new ParameterList[] { new DefaultParameterList("Nothing", new Parameter[0]) });
        MODEL = new NationLawModel<FightMonsterLaw>() {
            @Override
            public FightMonsterLaw createNewLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
                return new FightMonsterLaw(id, cost, lawPointCost, lawLocked, lawApplications, null);
            }
            
            @Override
            public NationLawListSet getParameters() {
                return FightMonsterLaw.PARAMETERS;
            }
        };
    }
}
