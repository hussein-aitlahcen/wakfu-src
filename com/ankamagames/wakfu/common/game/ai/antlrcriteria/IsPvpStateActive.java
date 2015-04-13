package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;

public final class IsPvpStateActive extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private NumericalValue m_pvpRankId;
    
    public IsPvpStateActive() {
        super();
    }
    
    public IsPvpStateActive(final ArrayList<ParserObject> args) {
        super();
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsPvpStateActive.signatures;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_PVP_STATE_ACTIVE;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo user = CriteriaUtils.getTargetCharacterInfoFromParameters(criterionUser, criterionTarget, criterionContent, criterionContext);
        if (user == null || !(user instanceof Citizen)) {
            throw new CriteriaExecutionException("Impossible de r\u00e9cup\u00e9rer la cible du crit\u00e8re");
        }
        return user.getCitizenComportment().getPvpState().isActive() ? 0 : -1;
    }
    
    static {
        IsPvpStateActive.signatures = new ArrayList<ParserType[]>();
    }
}
