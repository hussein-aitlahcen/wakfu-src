package com.ankamagames.wakfu.common.game.nation.Laws.impl;

import com.ankamagames.wakfu.common.game.nation.Laws.impl.events.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.global.group.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.framework.external.*;

public class JoinFightNotGroupedLaw extends NationLaw<JoinFightLawEvent>
{
    private static final NationLawListSet PARAMETERS;
    public static final NationLawModel<JoinFightNotGroupedLaw> MODEL;
    
    private JoinFightNotGroupedLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
        super(id, cost, lawPointCost, lawLocked, lawApplications);
    }
    
    @Override
    public void initialize(final List<ParserObject> parameters) {
    }
    
    @Override
    public NationLawModelConstant getModel() {
        return NationLawModelConstant.JOIN_FIGHT_NOT_GROUPED;
    }
    
    @Override
    public boolean isTriggering(final JoinFightLawEvent event) {
        final BasicCharacterInfo joiningCharacter = (BasicCharacterInfo)event.getCitizen();
        final BasicCharacterInfo attacker = event.getAttacker();
        if (attacker == null) {
            JoinFightNotGroupedLaw.m_logger.error((Object)"Attacker null lors du test de loi d'incruste !!!");
            return false;
        }
        final BasicCharacterInfo defender = event.getDefender();
        if (attacker == null) {
            JoinFightNotGroupedLaw.m_logger.error((Object)"Defender null lors du test de loi d'incruste !!!");
            return false;
        }
        final BasicCharacterInfo teamLeader = (joiningCharacter.getTeamId() == attacker.getTeamId()) ? attacker : defender;
        if (!(joiningCharacter instanceof GroupUser) || !(teamLeader instanceof GroupUser)) {
            return false;
        }
        final long partyId = ((GroupUser)joiningCharacter).getGroupId(GroupType.PARTY);
        final long partyId2 = ((GroupUser)teamLeader).getGroupId(GroupType.PARTY);
        return event.getFightModel() != FightModel.DUEL && (partyId <= 0L || partyId2 <= 0L || partyId != partyId2);
    }
    
    static {
        PARAMETERS = new NationLawListSet(new ParameterList[] { new DefaultParameterList("Nothing", new Parameter[0]) });
        MODEL = new NationLawModel<JoinFightNotGroupedLaw>() {
            @Override
            public JoinFightNotGroupedLaw createNewLaw(final long id, final int cost, final int lawPointCost, final boolean lawLocked, final Iterable<NationLawApplication> lawApplications) {
                return new JoinFightNotGroupedLaw(id, cost, lawPointCost, lawLocked, lawApplications, null);
            }
            
            @Override
            public NationLawListSet getParameters() {
                return JoinFightNotGroupedLaw.PARAMETERS;
            }
        };
    }
}
