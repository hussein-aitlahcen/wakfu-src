package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public class NbSummons extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    private boolean m_target;
    private ParserObject m_summonId;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return NbSummons.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    public NbSummons(final ArrayList<ParserObject> args) {
        super();
        this.m_target = true;
        final short paramType = this.checkType(args);
        if (paramType == 0) {
            this.m_target = false;
        }
        if (paramType == 1) {
            this.m_target = args.get(0).getValue().equalsIgnoreCase("target");
        }
        if (paramType == 2) {
            this.m_target = args.get(0).getValue().equalsIgnoreCase("target");
            this.m_summonId = args.get(1);
        }
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (criterionContext == null) {
            throw new CriteriaExecutionException("Pas de contexte...");
        }
        final boolean isFight = criterionContext instanceof AbstractFight;
        final boolean isFightContext = criterionContext instanceof WakfuFightEffectContext;
        if (!isFight && !isFightContext) {
            throw new CriteriaExecutionException("On essaie de compter les invocations en dehors d'un combat...");
        }
        final CriterionUser targetCharacter = CriteriaUtils.getTargetCriterionUserFromParameters(this.m_target, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (targetCharacter == null) {
            return -1L;
        }
        final long value = this.getSummoningCount(targetCharacter);
        return this.getSign() * value;
    }
    
    protected int getSummoningCount(final CriterionUser targetCharacter) {
        int res;
        if (this.m_summonId == null) {
            res = targetCharacter.getSummoningsCount();
        }
        else {
            res = targetCharacter.getSummoningsCount((int)((NumericalValue)this.m_summonId).getLongValue(null, null, null, null));
        }
        return res;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.NBSUMMONS;
    }
    
    static {
        (NbSummons.signatures = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
        NbSummons.signatures.add(CriterionConstants.ONE_STRING_SIGNATURE);
        NbSummons.signatures.add(new ParserType[] { ParserType.STRING, ParserType.NUMBER });
    }
}
