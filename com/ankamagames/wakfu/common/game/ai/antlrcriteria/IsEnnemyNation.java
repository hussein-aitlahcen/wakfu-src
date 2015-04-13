package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.nation.crime.constants.*;
import com.ankamagames.wakfu.common.datas.*;

public final class IsEnnemyNation extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private NumericalValue m_nationId;
    
    public IsEnnemyNation() {
        super();
    }
    
    public IsEnnemyNation(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_nationId = args.get(0);
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsEnnemyNation.signatures;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_ENNEMY_NATION;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo user = CriteriaUtils.getTargetCharacterInfoFromParameters(criterionUser, criterionTarget, criterionContent, criterionContext);
        if (user == null || !(user instanceof Citizen)) {
            throw new CriteriaExecutionException("Impossible de r\u00e9cup\u00e9rer la cible du crit\u00e8re");
        }
        return (user.getCitizenComportment().getNation().getDiplomacyManager().getAlignment((int)this.m_nationId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext)) == NationAlignement.ENEMY) ? 0 : -1;
    }
    
    static {
        IsEnnemyNation.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.NUMBER };
        IsEnnemyNation.signatures.add(sig);
    }
}
