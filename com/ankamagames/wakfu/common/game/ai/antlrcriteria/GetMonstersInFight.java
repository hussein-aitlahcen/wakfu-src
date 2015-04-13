package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fight.*;

public class GetMonstersInFight extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    private NumericalValue m_monsterId;
    private NumericalValue m_teamId;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetMonstersInFight.signatures;
    }
    
    public GetMonstersInFight(final ArrayList<ParserObject> args) {
        super();
        final short paramType = this.checkType(args);
        if (paramType == 0) {
            this.m_teamId = args.get(0);
            this.m_monsterId = null;
        }
        if (paramType == 1) {
            this.m_monsterId = null;
            this.m_teamId = null;
        }
        if (paramType == 2) {
            this.m_monsterId = args.get(1);
            this.m_teamId = args.get(0);
        }
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final AbstractFight fight = CriteriaUtils.getFightFromContext(criterionContext);
        if (fight == null) {
            throw new CriteriaExecutionException("On compte les monstres hors combat");
        }
        final short breedId = (short)((this.m_monsterId == null) ? -1L : this.m_monsterId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext));
        final byte tId = (byte)((this.m_teamId == null) ? -1L : this.m_teamId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext));
        final long value = fight.getMonsterCount(breedId, tId);
        return this.getSign() * value;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_MONSTERS_IN_FIGHT;
    }
    
    static {
        (GetMonstersInFight.signatures = new ArrayList<ParserType[]>()).add(CriterionConstants.ONE_NUMBER_SIGNATURE);
        GetMonstersInFight.signatures.add(CriterionConstants.EMPTY_SIGNATURE);
        GetMonstersInFight.signatures.add(new ParserType[] { ParserType.NUMBER, ParserType.NUMBER });
    }
}
