package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public final class CasterAndTargetHaveSameOriginalController extends FunctionCriterion
{
    private static final ArrayList<ParserType[]> signatures;
    
    public CasterAndTargetHaveSameOriginalController(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return CasterAndTargetHaveSameOriginalController.signatures;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (criterionUser == null || !(criterionUser instanceof CriterionUser)) {
            throw new CriteriaExecutionException("on essaie de savoir si la cible est une invoc d'un user invalide " + criterionUser);
        }
        final List<CriterionUser> targetCharacter = CriteriaUtils.getTargetListCriterionUserFromParameters(true, criterionUser, criterionTarget, criterionContext, criterionContent);
        for (int i = 0, n = targetCharacter.size(); i < n; ++i) {
            final CriterionUser user = targetCharacter.get(i);
            if (user != null) {
                if (user.getOriginalControllerId() == ((CriterionUser)criterionUser).getOriginalControllerId()) {
                    return 0;
                }
            }
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.CASTER_AND_TARGET_HAVE_SAME_ORIGINAL_CONTROLLER;
    }
    
    static {
        signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = new ParserType[0];
        CasterAndTargetHaveSameOriginalController.signatures.add(sig);
    }
}
