package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.wakfu.common.game.fighter.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.datas.*;

public class GetCharacteristicInPct extends FunctionValue implements Targetable
{
    private FighterCharacteristicType m_type;
    private boolean m_target;
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetCharacteristicInPct.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return false;
    }
    
    public GetCharacteristicInPct(final ArrayList<ParserObject> args) {
        super();
        final short paramType = this.checkType(args);
        if (paramType == 0) {
            this.m_type = GetCharacteristic.getCharacteristicTypeFromString(args.get(0).getValue());
        }
        if (paramType == 1) {
            this.m_type = GetCharacteristic.getCharacteristicTypeFromString(args.get(0).getValue());
            if (args.get(1).getValue().equalsIgnoreCase("target")) {
                this.m_target = true;
            }
            else {
                this.m_target = false;
            }
        }
    }
    
    @Override
    public double getDoubleValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        double value = 0.0;
        final CriterionUser targetCharacter = CriteriaUtils.getTargetCriterionUserFromParameters(this.m_target, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (targetCharacter == null) {
            return -1.0;
        }
        if (targetCharacter.hasCharacteristic(this.m_type)) {
            final int characValue = targetCharacter.getCharacteristicValue(this.m_type);
            final int maxValue = targetCharacter.getCharacteristicMax(this.m_type);
            value = characValue / maxValue * 100.0f;
            return super.getSign() * value;
        }
        GetCharacteristicInPct.m_logger.error((Object)("Trying to get GetCharacteristicInPct for a character without this charac. Charac=" + this.m_type + " criterionUser=" + criterionUser + " criterionTarget=" + criterionTarget + " criterionContent=" + criterionContent));
        return -1.0;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        throw new UnsupportedOperationException("Can't get Long value of a double");
    }
    
    public FighterCharacteristicType getCharacType() {
        return this.m_type;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_CHARACTERISTIC_PCT;
    }
    
    @Override
    public boolean isTarget() {
        return this.m_target;
    }
    
    static {
        GetCharacteristicInPct.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = { ParserType.STRING };
        GetCharacteristicInPct.signatures.add(sig);
        sig = new ParserType[] { ParserType.STRING, ParserType.STRING };
        GetCharacteristicInPct.signatures.add(sig);
    }
}
