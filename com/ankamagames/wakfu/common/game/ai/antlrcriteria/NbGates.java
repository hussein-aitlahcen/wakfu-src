package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.fight.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.common.datas.*;

public final class NbGates extends FunctionValue
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    public static final String TEAM_FILTER = "forTeam";
    public static final String OWNER_FILTER = "forOwner";
    private final String m_targetType;
    private final String m_filterType;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return NbGates.SIGNATURES;
    }
    
    public NbGates(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_targetType = args.get(0).getValue();
        this.m_filterType = args.get(1).getValue();
    }
    
    @Override
    public long getLongValue(@Nullable final Object criterionUser, @Nullable final Object criterionTarget, @Nullable final Object criterionContent, @Nullable final Object criterionContext) {
        final BasicFight fight = CriteriaUtils.getFight(criterionUser, criterionContext);
        if (fight == null) {
            return 0L;
        }
        long value = 0L;
        for (final BasicEffectArea area : fight.getEffectAreaManager().getActiveEffectAreas()) {
            if (this.isConcernedArea(area, criterionUser, criterionTarget, criterionContent, criterionContext)) {
                ++value;
            }
        }
        return this.getSign() * value;
    }
    
    private boolean isConcernedArea(final BasicEffectArea area, final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (area.getType() != EffectAreaType.GATE.getTypeId()) {
            return false;
        }
        final CriterionUser concernedUser = CriteriaUtils.getTarget(this.m_targetType, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (this.m_filterType.equalsIgnoreCase("forTeam") && concernedUser != null) {
            return area.getTeamId() == concernedUser.getTeamId();
        }
        return this.m_filterType.equalsIgnoreCase("forOwner") && concernedUser != null && area.getOwner() != null && area.getOwner().getId() == concernedUser.getId();
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.NB_GATES;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(new ParserType[] { ParserType.STRING, ParserType.STRING });
    }
}
