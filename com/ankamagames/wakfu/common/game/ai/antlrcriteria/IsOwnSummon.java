package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.Breed.*;

public final class IsOwnSummon extends FunctionCriterion
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    private static final int DO_NOT_USE_BREED = 0;
    private short m_breedId;
    
    public IsOwnSummon(final ArrayList<ParserObject> args) {
        super();
        final byte sigIndex = this.checkType(args);
        this.m_breedId = 0;
        if (sigIndex == 1) {
            this.m_breedId = (short)args.get(0).getLongValue(null, null, null, null);
        }
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsOwnSummon.SIGNATURES;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (criterionUser == null || !(criterionUser instanceof CriterionUser)) {
            throw new CriteriaExecutionException("on essaie de savoir si la cible est une invoc d'un user invalide " + criterionUser);
        }
        final CriterionUser targetCharacter = CriteriaUtils.getTargetCriterionUserFromParameters(true, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (targetCharacter == null) {
            return -1;
        }
        if (this.m_breedId != 0) {
            final Breed breed = targetCharacter.getBreed();
            if (breed == null) {
                return -1;
            }
            if (breed.getBreedId() != this.m_breedId) {
                return -1;
            }
        }
        if (targetCharacter.isSummoned() && targetCharacter.getOriginalControllerId() == ((CriterionUser)criterionUser).getId()) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_OWN_SUMMON;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
        IsOwnSummon.SIGNATURES.add(CriterionConstants.ONE_NUMBER_SIGNATURE);
    }
}
