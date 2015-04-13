package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.datas.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.fight.protagonists.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

public final class GetHumanAlliesCountInRange extends GetFightersCountInRange
{
    public GetHumanAlliesCountInRange(final ArrayList<ParserObject> args) {
        super(args);
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_HUMAN_ALLIES_COUNT_IN_RANGE;
    }
    
    @Override
    protected Collection<? extends BasicCharacterInfo> getFighters(final AbstractFight<?> fight, final CriterionUser user) {
        if (fight == null || user == null) {
            return (Collection<? extends BasicCharacterInfo>)Collections.emptyList();
        }
        return fight.getFighters(ProtagonistFilter.inPlay(), ProtagonistFilter.inTeam(user.getTeamId()), ProtagonistFilter.or(ProtagonistFilter.ofType((byte)5), ProtagonistFilter.ofType((byte)0)), ProtagonistFilter.not(ProtagonistFilter.hasProperty(WorldPropertyType.NOT_PRESENT_IN_TIMELINE)));
    }
}
