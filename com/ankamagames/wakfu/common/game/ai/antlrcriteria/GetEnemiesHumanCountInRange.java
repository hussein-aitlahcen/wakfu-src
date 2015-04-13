package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.datas.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.fight.protagonists.*;

public final class GetEnemiesHumanCountInRange extends GetFightersCountInRange
{
    public GetEnemiesHumanCountInRange(final ArrayList<ParserObject> args) {
        super(args);
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_ENEMIES_HUMAN_COUNT_IN_RANGE;
    }
    
    @Override
    protected Collection<? extends BasicCharacterInfo> getFighters(final AbstractFight<?> fight, final CriterionUser user) {
        if (fight == null || user == null) {
            return (Collection<? extends BasicCharacterInfo>)Collections.emptyList();
        }
        return fight.getFighters(ProtagonistFilter.not(ProtagonistFilter.inTeam(user.getTeamId())), ProtagonistFilter.inPlay(), ProtagonistFilter.or(ProtagonistFilter.ofType((byte)5), ProtagonistFilter.ofType((byte)0)));
    }
}
