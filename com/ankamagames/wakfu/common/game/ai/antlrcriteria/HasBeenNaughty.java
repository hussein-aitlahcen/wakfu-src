package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;

public final class HasBeenNaughty extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private NumericalValue m_nationId;
    
    public HasBeenNaughty() {
        super();
    }
    
    public HasBeenNaughty(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_nationId = args.get(0);
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasBeenNaughty.signatures;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HAS_BEEN_NAUGHTY;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo user = CriteriaUtils.getTargetCharacterInfoFromParameters(criterionUser, criterionTarget, criterionContent, criterionContext);
        if (user == null || !(user instanceof Citizen)) {
            throw new CriteriaExecutionException("Impossible de r\u00e9cup\u00e9rer la cible du crit\u00e8re");
        }
        return user.getCitizenComportment().isOutlaw((int)this.m_nationId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext)) ? 0 : -1;
    }
    
    static {
        HasBeenNaughty.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.NUMBER };
        HasBeenNaughty.signatures.add(sig);
    }
}
