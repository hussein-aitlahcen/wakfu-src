package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public class HasntEvolvedSince extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private NumericalValue m_durationToCheck;
    
    public HasntEvolvedSince(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_durationToCheck = args.get(0);
    }
    
    public long getDurationToCheck() {
        return (this.m_durationToCheck.isConstant() && this.m_durationToCheck.isInteger()) ? (this.m_durationToCheck.getLongValue(null, null, null, null) * 1000L) : -1L;
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasntEvolvedSince.signatures;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (!(criterionUser instanceof EvolvingCharacter)) {
            throw new CriteriaExecutionException("Le user d'un crit\u00e8re HasWorldProperty doit \u00eatre un character.");
        }
        final EvolvingCharacter character = (EvolvingCharacter)criterionUser;
        if (this.m_durationToCheck != null) {
            final long duration = this.getDurationToCheck();
            if (System.currentTimeMillis() - character.getLastEvolutionDate() >= duration) {
                return 0;
            }
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HAS_NOT_EVOLVED_SINCE;
    }
    
    static {
        HasntEvolvedSince.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.NUMBER };
        HasntEvolvedSince.signatures.add(sig);
    }
}
