package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;

public class GetStasisGauge extends FunctionValue
{
    private boolean m_target;
    private static ArrayList<ParserType[]> signatures;
    
    public GetStasisGauge(final ArrayList<ParserObject> args) {
        super();
        this.m_target = false;
        final short paramType = this.checkType(args);
        if (paramType == 0) {
            this.m_target = false;
        }
        if (paramType == 1) {
            this.m_target = args.get(0).getValue().equalsIgnoreCase("target");
        }
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetStasisGauge.signatures;
    }
    
    public boolean isTarget() {
        return this.m_target;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GETSTASISGAUGE;
    }
    
    @Override
    public boolean isInteger() {
        return false;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo targetCharacter = CriteriaUtils.getTargetCharacterInfoFromParameters(this.m_target, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (targetCharacter == null) {
            return -1L;
        }
        final long value = (long)(targetCharacter.getWakfuGaugeValue() * 100.0f);
        return super.getSign() * value * -1L;
    }
    
    static {
        GetStasisGauge.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = new ParserType[0];
        GetStasisGauge.signatures.add(sig);
        sig = new ParserType[] { ParserType.STRING };
        GetStasisGauge.signatures.add(sig);
    }
}
