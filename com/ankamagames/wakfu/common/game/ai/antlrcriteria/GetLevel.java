package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;

public class GetLevel extends FunctionValue
{
    private boolean m_target;
    private boolean m_useEventTarget;
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetLevel.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    public GetLevel(final ArrayList<ParserObject> args) {
        super();
        final short paramType = this.checkType(args);
        if (paramType == 1) {
            final String targetType = args.get(0).getValue();
            if (targetType.equalsIgnoreCase("target")) {
                this.m_target = true;
            }
            else {
                this.m_target = false;
            }
            if (targetType.equals("eventTarget")) {
                this.m_useEventTarget = true;
            }
        }
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (this.m_useEventTarget) {
            EventTargetable target;
            if (criterionContext instanceof TargetedEvent) {
                target = ((TargetedEvent)criterionContext).getTarget();
            }
            else {
                if (!(criterionContent instanceof TargetedEvent)) {
                    return -1L;
                }
                target = ((TargetedEvent)criterionContent).getTarget();
            }
            if (!(target instanceof BasicCharacterInfo)) {
                return -1L;
            }
            return this.getSign() * ((BasicCharacterInfo)target).getLevel();
        }
        else {
            final BasicCharacterInfo targetCharacter = CriteriaUtils.getTargetCharacterInfoFromParameters(this.m_target, criterionUser, criterionTarget, criterionContext, criterionContent);
            if (targetCharacter == null) {
                return -1L;
            }
            final long value = targetCharacter.getLevel();
            return this.getSign() * value;
        }
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_LEVEL;
    }
    
    static {
        (GetLevel.signatures = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
        GetLevel.signatures.add(CriterionConstants.ONE_STRING_SIGNATURE);
    }
}
