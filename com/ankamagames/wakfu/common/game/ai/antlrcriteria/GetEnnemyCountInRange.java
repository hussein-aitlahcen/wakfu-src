package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.datas.*;
import java.util.*;

public class GetEnnemyCountInRange extends GetFightersCountInRange
{
    public GetEnnemyCountInRange(final ArrayList<ParserObject> args) {
        super(args);
    }
    
    @Override
    protected Collection<? extends BasicCharacterInfo> getFighters(final AbstractFight<?> fight, final CriterionUser user) {
        if (fight == null || user == null) {
            return (Collection<? extends BasicCharacterInfo>)Collections.emptyList();
        }
        return fight.getFightersInPlayNotInTeam(user.getTeamId());
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GETENNEMYCOUNTINRANGE;
    }
}
