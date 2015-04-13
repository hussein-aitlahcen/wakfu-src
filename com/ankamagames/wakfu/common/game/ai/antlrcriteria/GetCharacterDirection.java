package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.maths.*;

public final class GetCharacterDirection extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    private boolean m_target;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetCharacterDirection.signatures;
    }
    
    public GetCharacterDirection(final ArrayList<ParserObject> args) {
        super();
        this.m_target = false;
        final byte type = this.checkType(args);
        switch (type) {
            case 0: {
                this.m_target = false;
                break;
            }
            case 1: {
                final String isTarget = args.get(0).getValue();
                if (isTarget.equalsIgnoreCase("target")) {
                    this.m_target = true;
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_FIGHTER_DIRECTION;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo targetCharacter = CriteriaUtils.getTargetCharacterInfoFromParameters(this.m_target, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (targetCharacter == null) {
            return -1L;
        }
        final Direction8 direction = targetCharacter.getDirection();
        if (direction == null) {
            return -1L;
        }
        return direction.m_index;
    }
    
    static {
        GetCharacterDirection.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = new ParserType[0];
        GetCharacterDirection.signatures.add(sig);
        sig = new ParserType[] { ParserType.STRING };
        GetCharacterDirection.signatures.add(sig);
    }
}
