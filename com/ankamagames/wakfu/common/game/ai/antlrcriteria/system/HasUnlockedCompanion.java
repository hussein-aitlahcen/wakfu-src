package com.ankamagames.wakfu.common.game.ai.antlrcriteria.system;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.companion.*;
import com.ankamagames.wakfu.common.datas.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.*;

public final class HasUnlockedCompanion extends FunctionCriterion
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    private final long m_breedId;
    
    public HasUnlockedCompanion(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_breedId = args.get(0).getLongValue(null, null, null, null);
    }
    
    @Override
    protected ArrayList<ParserType[]> getSignatures() {
        return HasUnlockedCompanion.SIGNATURES;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final CriterionUser target = CriteriaUtils.getTargetCriterionUserFromParameters(false, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (!(target instanceof BasicCharacterInfo)) {
            return -1;
        }
        final BasicCharacterInfo info = (BasicCharacterInfo)target;
        if (CompanionManager.INSTANCE.hasUnlockedCompanionWithBreed(info.getClientId(), (int)this.m_breedId)) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HAS_UNLOCKED_COMPANION;
    }
    
    public short getBreedId() {
        return (short)this.m_breedId;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(CriterionConstants.ONE_NUMBER_SIGNATURE);
    }
}
