package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fight.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

public final class IsCharacterWithHighestStateLevel extends FunctionCriterion
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    private boolean m_useTarget;
    private long m_stateId;
    
    public IsCharacterWithHighestStateLevel(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_useTarget = args.get(0).getValue().equalsIgnoreCase("target");
        this.m_stateId = args.get(1).getLongValue(null, null, null, null);
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsCharacterWithHighestStateLevel.SIGNATURES;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final CriterionUser target = CriteriaUtils.getTargetCriterionUserFromParameters(this.m_useTarget, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (target == null) {
            return -1;
        }
        if (target instanceof BasicCharacterInfo && !this.isValidForCriterion((BasicCharacterInfo)target)) {
            return -1;
        }
        final int stateLevel = target.getStateLevel(this.m_stateId);
        if (stateLevel < 0) {
            return -1;
        }
        if (criterionContext == null) {
            throw new CriteriaExecutionException("Pas de contexte...");
        }
        final AbstractFight<?> fight = CriteriaUtils.getFight(criterionUser, criterionContext);
        if (fight == null) {
            throw new CriteriaExecutionException("Impossible de r\u00e9cup\u00e9rer le combat");
        }
        final Collection<? extends BasicCharacterInfo> fightersInPlay = fight.getFightersInPlay();
        for (final BasicCharacterInfo fighter : fightersInPlay) {
            if (this.isValidForCriterion(fighter) && fighter.getStateLevel(this.m_stateId) > stateLevel) {
                return -1;
            }
        }
        return 0;
    }
    
    private boolean isValidForCriterion(final BasicCharacterInfo fighter) {
        return !fighter.isActiveProperty(FightPropertyType.CANNOT_BE_EFFECT_TARGET) && !fighter.isActiveProperty(FightPropertyType.UNTARGETTABLE_BY_OTHER) && !fighter.isCarried();
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_CHARACTER_WITH_HIGHEST_STATE_LEVEL;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(new ParserType[] { ParserType.STRING, ParserType.NUMBER });
    }
}
