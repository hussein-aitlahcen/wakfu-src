package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;

public final class IsPasseportActive extends FunctionCriterion
{
    private static final ArrayList<ParserType[]> signatures;
    
    public IsPasseportActive(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsPasseportActive.signatures;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (criterionUser == null || !(criterionUser instanceof BasicCharacterInfo)) {
            throw new CriteriaExecutionException("On essaie d'avoir une info sur une cible invalide " + criterionUser);
        }
        final CriterionUser target = CriteriaUtils.getTargetCharacterInfoFromParameters(criterionUser, criterionTarget, criterionContext, criterionContent);
        BasicCharacterInfo targetCharacter = null;
        if (criterionTarget instanceof BasicCharacterInfo) {
            targetCharacter = (BasicCharacterInfo)target;
        }
        if (targetCharacter == null) {
            return -1;
        }
        if (targetCharacter.getCitizenComportment().isPasseportActive()) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_PASSEPORT_ACTIVE;
    }
    
    static {
        signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = new ParserType[0];
        IsPasseportActive.signatures.add(sig);
    }
}
