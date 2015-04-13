package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.datas.specific.*;

public final class IsCarryingOwnBomb extends FunctionCriterion
{
    private boolean m_target;
    private static final ArrayList<ParserType[]> SIGNATURES;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsCarryingOwnBomb.SIGNATURES;
    }
    
    public IsCarryingOwnBomb(final ArrayList<ParserObject> args) {
        super();
        final byte type = this.checkType(args);
        if (type == 0) {
            this.m_target = false;
        }
        else if (type == 1) {
            final String isTarget = args.get(0).getValue();
            if (isTarget.equalsIgnoreCase("target")) {
                this.m_target = true;
            }
        }
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo targetCharacter = CriteriaUtils.getTargetCharacterInfoFromParameters(this.m_target, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (targetCharacter == null) {
            return -1;
        }
        final CarryTarget carryTarget = targetCharacter.getCurrentCarryTarget();
        if (!(carryTarget instanceof AbstractBombEffectArea)) {
            return -1;
        }
        if (((AreaOwnerProvider)carryTarget).getOwner() == targetCharacter) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_CARRYING_OWN_BOMB;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
        IsCarryingOwnBomb.SIGNATURES.add(CriterionConstants.ONE_STRING_SIGNATURE);
    }
}
