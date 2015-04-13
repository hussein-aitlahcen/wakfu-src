package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public final class GetCrimeScore extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    private NumericalValue m_nationId;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetCrimeScore.signatures;
    }
    
    public GetCrimeScore(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        if (args.size() == 1) {
            this.m_nationId = args.get(0);
        }
    }
    
    public int getNationId() {
        if (this.m_nationId != null && this.m_nationId.isConstant() && this.m_nationId.isInteger()) {
            return (int)this.m_nationId.getLongValue(null, null, null, null);
        }
        return -1;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        BasicCharacterInfo user = null;
        if (criterionUser instanceof BasicCharacterInfo) {
            user = (BasicCharacterInfo)criterionUser;
        }
        else if (criterionContent instanceof PlayerTriggered) {
            user = ((PlayerTriggered)criterionContent).getTriggerer();
        }
        if (user == null) {
            throw new CriteriaExecutionException("Impossible de r\u00e9cup\u00e9rer la cible du crit\u00e8re");
        }
        if (this.m_nationId != null) {
            final int nationId = (int)this.m_nationId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
            return user.getCitizenComportment().getCitizenScoreForNation(nationId);
        }
        return user.getCitizenComportment().getCitizenScoreForNation(user.getCitizenComportment().getNationId());
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_CRIME_SCORE;
    }
    
    static {
        GetCrimeScore.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = new ParserType[0];
        GetCrimeScore.signatures.add(sig);
        sig = new ParserType[] { ParserType.NUMBER };
        GetCrimeScore.signatures.add(sig);
    }
}
