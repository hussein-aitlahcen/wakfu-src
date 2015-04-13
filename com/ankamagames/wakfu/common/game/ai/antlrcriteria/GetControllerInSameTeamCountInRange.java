package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.datas.*;
import java.util.*;

public final class GetControllerInSameTeamCountInRange extends GetFightersCountInRange
{
    public GetControllerInSameTeamCountInRange(final ArrayList<ParserObject> args) {
        super(args);
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_CONTROLLER_IN_SAME_TEAM_COUNT_IN_RANGE;
    }
    
    @Override
    protected Collection<? extends BasicCharacterInfo> getFighters(final AbstractFight<?> fight, final CriterionUser user) {
        if (fight == null || user == null) {
            return (Collection<? extends BasicCharacterInfo>)Collections.emptyList();
        }
        return fight.getFightersWithControllerInTeam(user.getTeamId());
    }
}
