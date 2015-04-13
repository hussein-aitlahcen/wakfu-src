package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;

public final class IsHostile extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private NumericalValue m_nationId;
    
    public IsHostile() {
        super();
    }
    
    public IsHostile(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_nationId = args.get(0);
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsHostile.signatures;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_HOSTILE;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo user = CriteriaUtils.getTargetCharacterInfoFromParameters(criterionUser, criterionTarget, criterionContent, criterionContext);
        if (user == null || !(user instanceof Citizen)) {
            throw new CriteriaExecutionException("Impossible de r\u00e9cup\u00e9rer la cible du crit\u00e8re");
        }
        return user.getCitizenComportment().isNationEnemy((int)this.m_nationId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext)) ? 0 : -1;
    }
    
    static {
        IsHostile.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.NUMBER };
        IsHostile.signatures.add(sig);
    }
}
