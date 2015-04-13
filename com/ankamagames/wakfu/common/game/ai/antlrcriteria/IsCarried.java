package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;

public final class IsCarried extends FunctionCriterion
{
    private boolean m_target;
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsCarried.signatures;
    }
    
    public IsCarried(final ArrayList<ParserObject> args) {
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
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo targetCharacter = CriteriaUtils.getTargetCharacterInfoFromParameters(this.m_target, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (targetCharacter == null) {
            return -1;
        }
        if (targetCharacter.isCarried()) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_CARRIED;
    }
    
    static {
        IsCarried.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = new ParserType[0];
        IsCarried.signatures.add(sig);
        sig = new ParserType[] { ParserType.STRING };
        IsCarried.signatures.add(sig);
    }
}
