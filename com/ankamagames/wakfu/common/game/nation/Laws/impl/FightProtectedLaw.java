package com.ankamagames.wakfu.common.game.nation.Laws.impl;

import com.ankamagames.wakfu.common.game.nation.Laws.impl.events.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.protector.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.framework.external.*;

public class FightProtectedLaw extends NationLaw<FightLawEvent>
{
    private static final NationLawListSet PARAMETERS;
    public static final NationLawModel<FightProtectedLaw> MODEL;
    
    private FightProtectedLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
        super(id, cost, lawPointCost, lawLocked, lawApplications);
    }
    
    @Override
    public void initialize(final List<ParserObject> parameters) {
    }
    
    @Override
    public NationLawModelConstant getModel() {
        return NationLawModelConstant.FIGHT_ECO_PROTECTED;
    }
    
    @Override
    public boolean isTriggering(final FightLawEvent event) {
        if (event.getFightModel() != FightModel.PVE) {
            return false;
        }
        final AbstractProtectorEcosystemHandler ecosystemHandler = event.getProtectorEcosystemHandler();
        if (ecosystemHandler == null) {
            return false;
        }
        final BasicCharacterInfo character = (BasicCharacterInfo)event.getCitizen();
        final BasicCharacterInfo defender = event.getDefender();
        if (character == defender) {
            return false;
        }
        final Collection opponents = event.getOpponents();
        for (final Object opponent : opponents) {
            final BasicCharacterInfo monster = (BasicCharacterInfo)opponent;
            if (monster.getType() != 1) {
                continue;
            }
            if (ecosystemHandler.isProtectedMonsterFamily(monster.getBreed().getFamilyId())) {
                return true;
            }
        }
        return false;
    }
    
    static {
        PARAMETERS = new NationLawListSet(new ParameterList[] { new DefaultParameterList("Nothing", new Parameter[0]) });
        MODEL = new NationLawModel<FightProtectedLaw>() {
            @Override
            public FightProtectedLaw createNewLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
                return new FightProtectedLaw(id, cost, lawPointCost, lawLocked, lawApplications, null);
            }
            
            @Override
            public NationLawListSet getParameters() {
                return FightProtectedLaw.PARAMETERS;
            }
        };
    }
}
